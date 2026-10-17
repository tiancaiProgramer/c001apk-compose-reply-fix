package com.example.c001apk.compose.logic.update

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.junit.Assert.*
import org.junit.Test

class GithubReleasesTest {
    private fun release(tag: String, draft: Boolean = false, apk: Boolean = true): JsonObject {
        val name = "c002apk-${tag.removePrefix("v")}.apk"
        return JsonObject().apply {
            addProperty("tag_name", tag)
            addProperty("draft", draft)
            addProperty("prerelease", tag.contains('-'))
            addProperty("body", "New release")
            addProperty("html_url", "https://github.com/${GithubReleases.REPOSITORY}/releases/tag/$tag")
            add("assets", JsonArray().apply { if (apk) add(JsonObject().apply {
                addProperty("name", name)
                addProperty("state", "uploaded")
                addProperty("size", 6000000)
                addProperty("browser_download_url", "https://github.com/${GithubReleases.REPOSITORY}/releases/download/$tag/$name")
            }) })
        }
    }
    private fun response(vararg releases: JsonObject) = JsonArray().apply { releases.forEach(::add) }.toString()
    private fun version(text: String) = requireNotNull(ReleaseVersion.parse(text))

    @Test fun comparesNumericVersionsAndPreviewIdentifiersWithoutLexicalMistakes() {
        assertTrue(version("0.1.10") > version("0.1.9"))
        assertTrue(version("0.1.3-rc.10") > version("0.1.3-rc.2"))
        assertTrue(version("0.1.3") > version("0.1.3-rc.99"))
        assertTrue(version("0.2.0-rc.1") > version("0.1.3"))
        assertEquals(0, version("v0.1.3+build.4").compareTo(version("0.1.3")))
        assertNull(ReleaseVersion.parse("commit-hash"))
        assertNull(ReleaseVersion.parse("0.1.3-rc.01"))
    }

    @Test fun selectsHighestPublishedVersionWithApkIncludingPreviewsRegardlessOfListOrder() {
        val json = response(release("v0.1.3-rc.2"), release("v0.1.3-rc.10"), release("v0.1.3-rc.4"))
        assertEquals("v0.1.3-rc.10", GithubReleases.newerRelease(json, "0.1.3-rc.1")?.tag)
    }

    @Test fun ignoresDraftsMissingApksAndSameOrOlderVersions() {
        val json = response(release("v9.0.0", draft = true), release("v8.0.0", apk = false), release("v0.1.3"), release("v0.1.2"))
        assertNull(GithubReleases.newerRelease(json, "0.1.3"))
        assertEquals("v0.1.3", GithubReleases.newerRelease(json, "0.1.2")?.tag)
    }

    @Test fun refusesForeignDownloadsIncompleteUploadsAndTestBuildAssets() {
        val foreign = release("v1.0.0").apply {
            getAsJsonArray("assets")[0].asJsonObject.addProperty("browser_download_url", "https://example.com/c002apk-1.0.0.apk")
        }
        val pending = release("v2.0.0").apply { getAsJsonArray("assets")[0].asJsonObject.addProperty("state", "new") }
        val test = release("v3.0.0").apply { getAsJsonArray("assets")[0].asJsonObject.addProperty("name", "c002apk-test.apk") }
        val empty = release("v4.0.0").apply { getAsJsonArray("assets")[0].asJsonObject.addProperty("size", 0) }
        assertNull(GithubReleases.newerRelease(response(foreign, pending, test, empty), "0.1.3"))
    }

    @Test fun requiresRepositoryReleasePageAndMatchingDownloadTag() {
        val fakePage = release("v1.0.0").apply { addProperty("html_url", "https://github.com/other/project/releases/tag/v1.0.0") }
        val wrongTag = release("v2.0.0").apply {
            val asset = getAsJsonArray("assets")[0].asJsonObject
            asset.addProperty("browser_download_url", asset["browser_download_url"].asString.replace("/v2.0.0/", "/v1.0.0/"))
        }
        assertNull(GithubReleases.newerRelease(response(fakePage, wrongTag), "0.1.3"))
    }

    @Test fun skipsMalformedEntriesAndSupportsPreviouslyPublishedApkNaming() {
        val olderName = release("v0.1.2-rc.1").apply {
            val asset = getAsJsonArray("assets")[0].asJsonObject
            asset.addProperty("name", asset["name"].asString.replace("c002apk-", "c001apk-reply-fix-"))
            asset.addProperty("browser_download_url", asset["browser_download_url"].asString.replace("/c002apk-", "/c001apk-reply-fix-"))
        }
        assertEquals("v0.1.2-rc.1", GithubReleases.newerRelease(response(JsonObject(), olderName), "0.1.1-rc.1")?.tag)
    }
}
