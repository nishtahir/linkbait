package com.nishtahir.linkbait.config

import groovy.transform.Canonical;

/**
 *
 */
@Canonical
class Connection {

    /**
     * Connection to SQL db
     *
     * relevant yml
     *
     *  url: {jdbc connection url}
     */
    String url
}
