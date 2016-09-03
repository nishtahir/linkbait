package de.langerhans.linkbait.tenor.model

/**
 * Created by maxke on 24.08.2016.
 * Media collection
 */
data class TenorMediaCollection(
        val nanogif: TenorMedia,
        val nanomp4: TenorMedia,
        val nanowebm: TenorMedia,
        val tinygif: TenorMedia,
        val tinymp4: TenorMedia,
        val tinywebm: TenorMedia,
        val gif: TenorMedia,
        val mp4: TenorMedia,
        val webm: TenorMedia,
        val loopedmp4: TenorMedia
)