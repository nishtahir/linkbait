package com.nishtahir.linkbait.core.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.nishtahir.linkbait.core.Plugin
import groovy.json.JsonBuilder
import groovy.json.JsonOutput

/**
 * Utility class to work with JSON.
 */
class JSONUtils {

    /**
     * @param data
     * @return compact JSON presentation of data
     */
    static String dataToJSON(def data) {
        return new JsonBuilder(data).toString()
    }

    /**
     * @param data
     * @return nicely formatted JSON representation of data
     */
    static String dataToPrettyJSON(def data) {
        return JsonOutput.prettyPrint(dataToJSON(data))
    }

    /**
     *
     * @param data
     * @return
     */
    static Plugin jsonToPlugin(String data) {
        return new ObjectMapper().readValue(data, Plugin.class)
    }
}
