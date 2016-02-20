package com.nishtahir.islackbot.util

import groovy.json.JsonBuilder
import groovy.json.JsonOutput

/**
 * Created by nish on 2/20/16.
 */
class JSONUtils {

    /**
     *
     * @param data
     * @return
     */
    static String dataToJSON(def data) {
        return new JsonBuilder(data).toString()
    }

    /**
     *
     * @param data
     * @return
     */
    static String dataToPrettyJSON(def data) {
        return JsonOutput.prettyPrint(dataToJSON(data))
    }
}
