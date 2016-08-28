package de.langerhans.linkbait.tenor.model

/**
 * Created by maxke on 24.08.2016.
 * Result model
 */
data class TenorResult(
        val created: Float,
        val hasaudio: Boolean,
        val hascaption: Boolean,
        val id: String,
        val media: List<TenorMediaCollection>,
        val tags: List<String>,
        val title: String,
        val url: String,
        val shares: Int,
        val composite: TenorComposite?
)