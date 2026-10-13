package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.logic.model.SecondHandInfo
import com.example.c001apk.compose.ui.component.CoilLoader
import com.example.c001apk.compose.ui.theme.cardBg
import com.example.c001apk.compose.util.DateUtils.fromToday
import com.example.c001apk.compose.util.ImageShowUtil.getImageLp
import com.example.c001apk.compose.util.Utils.richToString

@Composable
fun SecondHandInfoRow(
    info: SecondHandInfo,
    modifier: Modifier = Modifier,
    onOpenLink: (String, String?) -> Unit,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                info.priceText(), Modifier.weight(1f),
                color = if (info.price.isNullOrBlank() && info.faceDeal != 1)
                    MaterialTheme.colorScheme.outline else Color(0xFFFF5145),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            info.source?.takeIf { it.isNotBlank() }?.let { source ->
                Text(
                    source,
                    modifier = Modifier.clip(RoundedCornerShape(4.dp))
                        .background(if (source == "闲鱼") Color(0xFFFFD740)
                            else MaterialTheme.colorScheme.secondaryContainer)
                        .clickable(enabled = !info.url.isNullOrBlank()) {
                            info.url?.let { onOpenLink(it, null) }
                        }.padding(horizontal = 5.dp, vertical = 2.dp),
                    color = if (source == "闲鱼") Color(0xFF333333)
                        else MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
        info.statusText()?.let {
            Text(it, color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
fun SecondHandCard(
    data: HomeFeedResponse.Data,
    onViewFeed: (String, Boolean) -> Unit,
    onViewUser: (String) -> Unit,
    onOpenLink: (String, String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val info = data.secondHandInfo
    val cover = data.picArr?.firstOrNull()?.takeIf { it.isNotBlank() }
        ?: data.pic?.takeIf { it.isNotBlank() } ?: info?.logo
    val size = cover?.let(::getImageLp)
    val ratio = if (size != null && size.first > 0 && size.second > 0)
        (size.first.toFloat() / size.second).coerceIn(0.8f, 1.5f) else 1f
    Column(
        modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(cardBg())
            .clickable { onViewFeed(data.id.orEmpty(), false) },
    ) {
        if (!cover.isNullOrBlank()) {
            CoilLoader(url = cover, modifier = Modifier.fillMaxWidth().aspectRatio(ratio))
        }
        Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = (data.messageTitle?.takeIf { it.isNotBlank() }
                    ?: info?.title?.takeIf { it.isNotBlank() } ?: data.message.orEmpty()).richToString(),
                maxLines = 2, overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
            )
            info?.let { SecondHandInfoRow(it, onOpenLink = onOpenLink) }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                CoilLoader(url = data.userAvatar,
                    modifier = Modifier.size(28.dp).clip(CircleShape)
                        .clickable { onViewUser(data.uid.orEmpty()) })
                Column(Modifier.weight(1f)) {
                    Text(data.username.orEmpty(), maxLines = 1, overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline)
                    Text(listOf(fromToday(data.dateline ?: 0), info?.locationText().orEmpty())
                        .filter { it.isNotBlank() }.joinToString(" · "),
                        maxLines = 1, overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}
