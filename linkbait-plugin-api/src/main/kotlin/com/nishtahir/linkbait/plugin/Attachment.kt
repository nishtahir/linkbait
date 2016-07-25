package com.nishtahir.linkbait.plugin

/**
 * Attachment in a message.
 * I could not figure out how to make this
 * visible as an inner class.
 *
 * If you figure it out please fix and let me know
 *
 * Nish
 */
class Attachment(
        val title: String,
        val body: String,
        val color: String,

        val titleUrl: String = "",
        val thumbnailUrl: String = "",
        val imageUrl: String = ""
) {
    var additionalFields : Map<String, String>? = null
}
