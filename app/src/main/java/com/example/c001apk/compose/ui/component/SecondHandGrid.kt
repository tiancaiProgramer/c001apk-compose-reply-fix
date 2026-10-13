package com.example.c001apk.compose.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.logic.model.secondHandFeedRows
import com.example.c001apk.compose.logic.state.FooterState
import com.example.c001apk.compose.logic.state.LoadingState
import com.example.c001apk.compose.ui.base.BaseViewModel
import com.example.c001apk.compose.ui.component.cards.LoadingCard
import com.example.c001apk.compose.ui.component.cards.SecondHandCard
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun SecondHandGrid(
    viewModel: BaseViewModel,
    paddingValues: PaddingValues,
    refreshState: Boolean?,
    onViewFeed: (String, Boolean) -> Unit,
    onViewUser: (String) -> Unit,
    onOpenLink: (String, String?) -> Unit,
    isScrollingUp: ((Boolean) -> Unit)?,
) {
    val gridState = rememberLazyStaggeredGridState()
    val response: List<HomeFeedResponse.Data> =
        (viewModel.loadingState as? LoadingState.Success)?.response.orEmpty()
    val data = remember(response) { response.secondHandFeedRows() }
    LaunchedEffect(refreshState) {
        if (refreshState == true) gridState.scrollToItem(0)
    }
    LaunchedEffect(gridState, data.size, viewModel.isEnd, viewModel.footerState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1 }
            .distinctUntilChanged().collect { lastVisible ->
                if (data.isNotEmpty() && lastVisible >= data.lastIndex - 3 &&
                    !viewModel.isEnd && !viewModel.isRefreshing &&
                    viewModel.footerState == FooterState.Success) viewModel.loadMore()
            }
    }
    LaunchedEffect(gridState, isScrollingUp) {
        var previousIndex = gridState.firstVisibleItemIndex
        var previousOffset = gridState.firstVisibleItemScrollOffset
        snapshotFlow { gridState.firstVisibleItemIndex to gridState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                isScrollingUp?.invoke(index < previousIndex ||
                    (index == previousIndex && offset <= previousOffset))
                previousIndex = index
                previousOffset = offset
            }
    }
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        state = gridState,
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalItemSpacing = 10.dp,
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp, top = 10.dp,
            bottom = 10.dp + paddingValues.calculateBottomPadding()),
    ) {
        if (viewModel.loadingState !is LoadingState.Success) {
            item(span = StaggeredGridItemSpan.FullLine) {
                LoadingCard(state = viewModel.loadingState, onClick = viewModel::refresh)
            }
        }
        itemsIndexed(data,
            key = { index, item -> "${item.entityId ?: item.id}:${item.entityType}:$index" },
            span = { _, item -> if (item.entityType == "feed") StaggeredGridItemSpan.SingleLane
                else StaggeredGridItemSpan.FullLine },
        ) { _, item ->
            if (item.entityType == "feed") {
                SecondHandCard(item, onViewFeed, onViewUser, onOpenLink)
            } else {
                // Preserve server-provided notices and navigation cards in the topic header.
                androidx.compose.foundation.layout.Column {
                    when {
                        !item.entities.isNullOrEmpty() ->
                            com.example.c001apk.compose.ui.component.cards.IconMiniScrollCard(
                                data = item, onOpenLink = onOpenLink)
                        !item.message.isNullOrBlank() -> LinkText(
                            text = item.message, onOpenLink = onOpenLink)
                        else -> LinkText(text = item.title.orEmpty(), onOpenLink = onOpenLink)
                    }
                }
            }
        }
        item(key = "footer", span = StaggeredGridItemSpan.FullLine) {
            LoadingCard(state = viewModel.footerState,
                onClick = if (viewModel.footerState is FooterState.Error) viewModel::loadMore else null)
        }
    }
}
