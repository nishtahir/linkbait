package de.langerhans.linkbait.android

/**
 * Created by maxke on 01.06.2016.
 * API models
 */

data class SearchResult (
        val searchInformation: SearchInformation,
        val items: List<SearchItem>
)

data class SearchInformation (
        val searchTime: Double,
        val formattedSearchTime: String,
        val totalResults: String,
        val formattedTotalResults: String
)

data class SearchItem (
        val title: String,
        val link: String,
        var snippet: String
)