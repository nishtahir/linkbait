package com.nishtahir.linkbait.core.model

import groovy.transform.Canonical
import groovy.transform.ToString

/**
 * Typical maven dependency.
 */
@Canonical
@ToString
class Dependency {

    String group

    String artifact

    String version

    @Override
    String toString() {
        return "$group:$artifact:$version"
    }
}
