package com.nishtahir.linkbait.plugin

/**
 * Attachment in a message.
 * I could not figure out how to make this
 * visible as an inner class.
 *
 * If you figure it out please fix and let me know
 *
 * Nish
 *
 */
class Attachment(
        /**
         * Title of Attachment.
         */
        val title: String,

        /**
         * Main content of the Attachment.
         */
        val body: String,

        /**
         * Used if the service supports colors.
         */
        val color: String,

        /**
         * Makes the title of the attachment a clickable link.
         */
        val titleUrl: String = "",

        /**
         * Adds a thumbnail to the attachment.
         */
        val thumbnailUrl: String = "",

        /**
         * Adds an image to the attachment.
         */
        val imageUrl: String = ""
) {

    /**
     * Additional content in the attachment.
     */
    var additionalFields : Map<String, String>? = null
}
