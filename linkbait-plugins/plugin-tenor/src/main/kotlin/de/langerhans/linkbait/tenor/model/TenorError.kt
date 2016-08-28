package de.langerhans.linkbait.tenor.model

/**
 * Created by maxke on 24.08.2016.
 * Some error occured
 */
class TenorError(): TenorBaseResponse() {
    var code: Int = 0
    var error: String = ""
}