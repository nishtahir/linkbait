package com.nishtahir.linkbait.config

import groovy.transform.Canonical;

/**
 *
 */
@Canonical
class Houndify {

    /**
     * API url
     *
     * relevant yml
     *
     *  id: {client id}
     */
    String url

    /**
     * Client ID
     *
     * relevant yml
     *
     *  id: {client id}
     */
    String id

    /**
     * Client key
     *
     * relevant yml
     *
     *  id: {client key}
     */
    String key
}
