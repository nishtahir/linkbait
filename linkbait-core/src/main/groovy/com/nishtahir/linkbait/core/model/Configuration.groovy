package com.nishtahir.linkbait.core.model

/**
 * Config to be implemented by host container
 */
public interface Configuration {

    /**
     *
     * @return
     */
    File getPluginDirectory()

    /**
     *
     * @return
     */
    File getStaticFileDirectory()

    /**
     *
     * @return
     */
    File getTemporaryFileDirectory()

    /**
     * Plugin repository
     * @return
     */
    File getPluginRepository()
}
