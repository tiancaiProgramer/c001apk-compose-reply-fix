package com.example.c001apk.compose.logic.update

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

internal data class ReleaseVersion(val numbers: List<Long>, val preview: List<String>) : Comparable<ReleaseVersion> {
    override fun compareTo(other: ReleaseVersion): Int {
        numbers.zip(other.numbers).forEach { (a, b) -> if (a != b) return a.compareTo(b) }
        if (preview.isEmpty() || other.preview.isEmpty())
            return when { preview == other.preview -> 0; preview.isEmpty() -> 1; else -> -1 }
        preview.zip(other.preview).forEach { (a, b) ->
            val numericA = a.all(Char::isDigit)
            val numericB = b.all(Char::isDigit)
            val compared = when {
                numericA && numericB -> a.length.compareTo(b.length).takeIf { it != 0 } ?: a.compareTo(b)
                numericA != numericB -> if (numericA) -1 else 1
                else -> a.compareTo(b)
            }
            if (compared != 0) return compared
        }
        return preview.size.compareTo(other.preview.size)
    }

    companion object {
        private val pattern = Regex("^[vV]?(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-([0-9A-Za-z-]+(?:\\.[0-9A-Za-z-]+)*))?(?:\\+[0-9A-Za-z-]+(?:\\.[0-9A-Za-z-]+)*)?$")
        fun parse(text: String): ReleaseVersion? {
            val match = pattern.matchEntire(text) ?: return null
            val numbers = (1..3).map { match.groupValues[it].toLongOrNull() ?: return null }
            val preview = match.groupValues[4].takeIf(String::isNotEmpty)?.split('.') ?: emptyList()
            if (preview.any { it.all(Char::isDigit) && it.length > 1 && it.startsWith('0') }) return null
            return ReleaseVersion(numbers, preview)
        }
    }
}

data class GithubRelease(val tag: String, val notes: String, val pageUrl: String, val apkUrl: String)

internal object GithubReleases {
    const val REPOSITORY = "tiancaiProgramer/c001apk-compose-reply-fix"
    const val PAGE_URL = "https://github.com/$REPOSITORY/releases"
    const val API_URL = "https://api.github.com/repos/$REPOSITORY/releases?per_page=30"

    /** Compare versions rather than publication order, and include published preview releases. */
    fun newerRelease(json: String, installed: String): GithubRelease? {
        val current = requireNotNull(ReleaseVersion.parse(installed)) { "Unsupported installed version" }
        val releases = JsonParser.parseString(json).asJsonArray
        return releases.mapNotNull { element ->
            runCatching {
                val release = element.asJsonObject
                if (release["draft"]?.asBoolean != false) return@runCatching null
                val tag = release.string("tag_name")
                val version = ReleaseVersion.parse(tag) ?: return@runCatching null
                if (version <= current) return@runCatching null
                val page = release.string("html_url").toHttpUrlOrNull() ?: return@runCatching null
                if (page.scheme != "https" || page.host != "github.com" || page.username.isNotEmpty() ||
                    page.password.isNotEmpty() || page.port != 443 ||
                    page.pathSegments != REPOSITORY.split('/') + listOf("releases", "tag", tag)) return@runCatching null
                val versionName = tag.removePrefix("v").removePrefix("V")
                val allowedNames = setOf("c002apk-$versionName.apk", "c001apk-reply-fix-$versionName.apk")
                val asset = release.getAsJsonArray("assets").firstNotNullOfOrNull { item ->
                    val value = item.asJsonObject
                    val name = value.string("name")
                    val url = value.string("browser_download_url").toHttpUrlOrNull()
                    if (name in allowedNames && value.string("state") == "uploaded" &&
                        value["size"]?.asLong?.let { it > 0 } == true && url != null &&
                        url.scheme == "https" && url.host == "github.com" && url.port == 443 &&
                        url.username.isEmpty() && url.password.isEmpty() &&
                        url.pathSegments == REPOSITORY.split('/') + listOf("releases", "download", tag, name))
                        url.toString() else null
                } ?: return@runCatching null
                version to GithubRelease(tag, release.string("body").take(4000), page.toString(), asset)
            }.getOrNull()
        }.maxByOrNull { it.first }?.second
    }

    private fun JsonObject.string(name: String) = get(name)?.takeUnless { it.isJsonNull }?.asString.orEmpty()
}
