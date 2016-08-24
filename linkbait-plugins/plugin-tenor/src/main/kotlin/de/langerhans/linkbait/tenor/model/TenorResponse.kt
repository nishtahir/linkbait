package de.langerhans.linkbait.tenor.model

/**
 * Created by maxke on 24.08.2016.
 * The successful response
 */
class TenorResponse(): TenorBaseResponse() {
    var next: String = ""
    var results: List<TenorResult>? = null
    var weburl: String = ""
}