package com.nishtahir.linkbait.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.nishtahir.linkbait.model.Plugin
import groovy.json.JsonBuilder
import groovy.json.JsonOutput

/**
 * Utility class to work with JSON.
 */
class JsonUtils {

    /**
     * @param data
     * @return compact JSON presentation of data
     */
    static String dataToJson(def data) {
        return new JsonBuilder(data).toString()
    }

    /**
     * @param data
     * @return nicely formatted JSON representation of data
     */
    static String dataToPrettyJson(def data) {
        return JsonOutput.prettyPrint(dataToJson(data))
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
