package com.nishtahir.linkbait.model

import groovy.transform.Canonical
import groovy.transform.ToString

/**
 * Model for Plugin
 */
@Canonical
@ToString(includePackage = false)
class Plugin {

    /**
     * Version information about plugin
     */
    String version

    /**
     * Title of the plugin
     */
    String title

    /**
     * Description of the plugin
     */
    String description

    /**
     * Author of the plugin
     */
    String author

    /**
     * Link to repository of the plugin
     */
    String url

    /**
     * Class name for handler to load
     */
    String handler

}