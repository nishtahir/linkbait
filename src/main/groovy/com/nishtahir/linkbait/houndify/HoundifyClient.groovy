package com.nishtahir.linkbait.houndify

import com.nishtahir.linkbait.App
import groovy.json.JsonBuilder

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by maxke on 25.03.2016.
 * Provides methods to talk to the houndify API
 */
public class HoundifyClient {

    private static final String API_USER = "linkbait"
    private static final String HEADER_REQ_AUTH = "Hound-Request-Authentication"
    private static final String HEADER_CLIENT_AUTH = "Hound-Client-Authentication"
    private static final String HEADER_REQ_INFO = "Hound-Request-Info"

    /**
     * Request data from the API for a given input string
     * @param input The textual query
     * @return The JSON String reply from the API
     */
    static String doTextRequest(String input) {
        URL url = new URL(App.configuration.houndify.url + "?query=" + URLEncoder.encode(input, "utf-8"))
        String clientId = App.configuration.houndify.id
        String clientKey = App.configuration.houndify.key

        return url.getText(requestProperties: getHeaders(clientKey, clientId))
    }

    /**
     * Generates the required headers for the API request
     * @param clientKey The key. From the config
     * @param clientId The ID. From the config
     * @return A Map with the required headers
     */
    static Map getHeaders(String clientKey, String clientId) {
        long ts = System.currentTimeMillis() / 1000
        String reqId = UUID.randomUUID().toString()
        String authId = API_USER + ";" + reqId

        byte[] keyBytes = Base64.getUrlDecoder().decode(clientKey)
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        sha256Hmac.init(new SecretKeySpec(keyBytes, "HmacSHA256"));
        byte[] hash = sha256Hmac.doFinal(String.format("%s%d", authId, ts).getBytes());

        String clientAuth = clientId + ";" + ts.toString() + ";" + Base64.getUrlEncoder().encodeToString(hash)
        String reqInfo = getReqInfo(clientId)

        return [(HEADER_REQ_AUTH): authId, (HEADER_CLIENT_AUTH): clientAuth, (HEADER_REQ_INFO): reqInfo]
    }

    /**
     * Create a RequestInfo object
     * @param clientId Client ID from config
     * @return JSON String to be glued into the header
     */
    static String getReqInfo(String clientId) {
        def data = [
                UserID: API_USER,
                ClientID: clientId,
        ]

        return new JsonBuilder(data).toString()
    }
}
