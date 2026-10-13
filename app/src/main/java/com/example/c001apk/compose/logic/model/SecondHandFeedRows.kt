package com.example.c001apk.compose.logic.model

import com.google.gson.Gson

/** The API can return trade feeds directly or inside its full-page columns card. */
fun List<HomeFeedResponse.Data>.secondHandFeedRows(): List<HomeFeedResponse.Data> = flatMap { item ->
    if (item.entityTemplate == "verticalColumnsFullPageCard" && !item.entities.isNullOrEmpty()) {
        item.entities.orEmpty().filter { it.entityType == "feed" }.map { entity ->
            // Data and Entities share API field names; keep the ordinary feed/detail navigation model.
            tradeFeedGson.fromJson(tradeFeedGson.toJson(entity), HomeFeedResponse.Data::class.java)
        }
    } else listOf(item)
}

private val tradeFeedGson = Gson()
