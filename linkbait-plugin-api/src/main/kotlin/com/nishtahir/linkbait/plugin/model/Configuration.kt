package com.nishtahir.linkbait.plugin.model

import java.io.File

/**
 * Configuration for linkbait.
 */
interface Configuration {

    /**
     * @return File Static file directory. Used by the server to expose files.
     */
    fun getStaticFileDirectory(): File

    /**
     * @return Where plugins are loaded from.
     */
    fun getPluginDirectory(): File

    /**
     * @return Temporary folder for operations that don't need
     * to be permanently persisted.
     */
    fun getTemporaryFileDirectory(): File

    /**
     * @return Folder where resolved plugin dependencies are kept. It
     * is essentially a local maven repository for Linkbait.
     */
    fun getPluginRepository(): File

    /**
     * @return Folder For persistence purposes.
     */
    fun getDataDirectory():File
}