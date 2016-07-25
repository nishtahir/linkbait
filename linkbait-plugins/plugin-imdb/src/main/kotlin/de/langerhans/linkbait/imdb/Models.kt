package de.langerhans.linkbait.imdb

/**
 * Created by maxke on 24.04.2016.
 * API models
 */

data class OmdbResult(
        val Title: String,
        val Year: String, // I started typing and already hate the API...
        val Rated: String,
        val Released: String,
        val Runtime: String, // Wow, why is this a String too?
        val Genre: String, // Since arrays are evil!
        val Director: String,
        val Writer: String,
        val Actors: String, // Yup, pure evil...
        val Plot: String,
        val Language: String,
        val Country: String,
        val Awards: String,
        val Poster: String,
        val Metascore: String, // Kill meeeeee
        val imdbRating: String, // Oh god finally not uppercase!
        val imdbVotes: String, // YES! STRING!
        val imdbID: String,
        val Type: String,
        val Response: String, // Sample: "True"... /me flips table
        val Error: String? // I bet it also returns 200 for "Movie not found"... Just checked, yes it does -.-
)

data class ImdbResults(
        val title_popular: List<ImdbResult>?,
        val title_exact: List<ImdbResult>?,
        val title_substring: List<ImdbResult>?,
        val title_approx: List<ImdbResult>?
)

data class ImdbResult (
        val id: String,
        val title: String,
        val name: String,
        val title_description: String,
        val episode_title: String,
        val description: String
)