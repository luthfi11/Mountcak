package com.wysiwyg.mountcak.data.model

data class VideResult(
    val items: List<SearchResult?>
)

data class SearchResult(
    val kind: String? = null,
    val id: VideoId? = null
)

data class VideoId(
    val videoId: String? = null
)