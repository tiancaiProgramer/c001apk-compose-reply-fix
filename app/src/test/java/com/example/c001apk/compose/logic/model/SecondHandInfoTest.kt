package com.example.c001apk.compose.logic.model

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SecondHandInfoTest {
    @Test fun decodesOfficialSecondHandFields() {
        val feed = Gson().fromJson("""{
            "id":"123", "entityType":"feed", "ershou_info":{
                "product_title":"苹果air", "product_price":4700,
                "link_source":"闲鱼", "link_url":"https://example.com/item/1",
                "ershou_status":1, "is_face_deal":0,
                "province":"安徽", "city":"淮南"
            }
        }""", HomeFeedResponse.Data::class.java)
        val info = requireNotNull(feed.secondHandInfo)
        assertEquals("¥4700", info.priceText())
        assertEquals("苹果air", info.title)
        assertEquals("闲鱼", info.source)
        assertEquals("安徽 淮南", info.locationText())
        assertNull(info.statusText())
    }

    @Test fun preservesNegotiationAndExchangeMeaning() {
        assertEquals("价格面议", SecondHandInfo(price = "0", faceDeal = 1).priceText())
        assertEquals("暂无价格", SecondHandInfo().priceText())
        assertEquals("¥1900", SecondHandInfo(price = "￥1900").priceText())
        assertEquals("已售出", SecondHandInfo(status = -1).statusText())
        assertEquals("收购", SecondHandInfo(dealType = 1).statusText())
        assertEquals("换机 · 买家加钱",
            SecondHandInfo(dealType = 2, exchangePriceType = 2).statusText())
        assertEquals("上海", SecondHandInfo(province = "上海", city = "上海").locationText())
    }

    @Test fun ordinaryFeedsDoNotBecomeTrades() {
        val feed = Gson().fromJson("""{"id":"321","entityType":"feed"}""",
            HomeFeedResponse.Data::class.java)
        assertNull(feed.secondHandInfo)
    }

    @Test fun readsFeedsInsideTheColumnsCardWithoutLosingPicturesOrPrices() {
        val card = Gson().fromJson("""{
            "entityType":"card", "entityTemplate":"verticalColumnsFullPageCard",
            "entities":[{"entityType":"feed", "id":"77", "dateline":"123456",
                "picArr":["https://example.com/cover.jpg"],
                "ershou_info":{"product_price":"1900", "link_source":"闲鱼"}}]
        }""", HomeFeedResponse.Data::class.java)
        val feed = listOf(card).secondHandFeedRows().single()
        assertEquals("77", feed.id)
        assertEquals(123456L, feed.dateline)
        assertEquals(listOf("https://example.com/cover.jpg"), feed.picArr)
        assertEquals("¥1900", feed.secondHandInfo?.priceText())
    }
}
