package de.langerhans.linkbait.tenor.model

/**
 * Created by maxke on 24.08.2016.
 * Media entry
 */
data class TenorMedia(
    val url: String,
    val preview: String,
    val dims: List<Int>,
    val duration: Float,
    val size: Int
)