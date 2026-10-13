package com.example.c001apk.compose.logic.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SecondHandInfo(
    @SerializedName("product_price") val price: String? = null,
    @SerializedName("product_title") val title: String? = null,
    @SerializedName("product_logo") val logo: String? = null,
    @SerializedName("link_source") val source: String? = null,
    @SerializedName("link_url") val url: String? = null,
    @SerializedName("ershou_status") val status: Int? = null,
    @SerializedName("is_face_deal") val faceDeal: Int? = null,
    @SerializedName("deal_type") val dealType: Int? = null,
    @SerializedName("exchange_price_type") val exchangePriceType: Int? = null,
    val province: String? = null,
    val city: String? = null,
) : Parcelable {
    fun priceText(): String = when {
        faceDeal == 1 -> "价格面议"
        price.isNullOrBlank() -> "暂无价格"
        else -> "¥${price.trim().removePrefix("¥").removePrefix("￥")}"
    }

    fun statusText(): String? = when {
        (status ?: 0) < 0 -> "已售出"
        dealType == 1 -> "收购"
        dealType == 2 -> when (exchangePriceType) {
            1 -> "换机 · 卖家加钱"
            2 -> "换机 · 买家加钱"
            else -> "换机"
        }
        else -> null
    }

    fun locationText(): String = listOfNotNull(province, city)
        .filter { it.isNotBlank() }.distinct().joinToString(" ")
}
