package com.nishtahir.linkbait.core.util

import spock.lang.Specification

/**
 * Created by nish on 7/21/16.
 */
class DependencyUtilsTest extends Specification {

    def "ParseDependency should return correct group id"() {
        given:
        def map = DependencyUtils.parseDependency("com.nishtahir.linkbait:linkbait-plugin-api:1.0-SNAPSHOT")
            String val = map.get('GROUP')
        expect:
            val == "com.nishtahir.linkbait"
    }

    def "parseDependencies"() {
        given:
        def val = DependencyUtils.parseDependencies("""com.nishtahir.linkbait:linkbait-plugin-api:1.0-SNAPSHOT,uy.kohesive.injekt:injekt-core:1.16.+,org.xerial:sqlite-jdbc:3.8.11.2,org.xerial.thirdparty:nestedvm:1.0,com.j256.ormlite:ormlite-core:4.48,com.j256.ormlite:ormlite-jdbc:4.48,commons-io:commons-io:2.4,info.debatty:java-string-similarity:0.13,org.jetbrains.kotlin:kotlin-stdlib:1.0.2-1""")

        expect:
        val != null
    }
}
