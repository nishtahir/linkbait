package com.nishtahir.linkbait.core.util

import com.nishtahir.linkbait.core.model.Dependency
import spock.lang.Specification

/**
 * Created by nish on 7/21/16.
 */
class DependencyUtilsTest extends Specification {

    def "ParseDependency should return correct dependency"() {
        given:
        def dependency = DependencyUtils.parseDependency("com.nishtahir.linkbait:linkbait-plugin-api:1.0-SNAPSHOT")
        expect:
        dependency == new Dependency(
                group: "com.nishtahir.linkbait",
                artifact: "linkbait-plugin-api",
                version: "1.0-SNAPSHOT")
    }
}
