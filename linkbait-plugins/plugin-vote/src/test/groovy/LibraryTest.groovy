/*
 * This Spock specification was auto generated by running the Gradle 'init' task
 * by 'nish' at '4/19/16 10:35 PM' with Gradle 2.11
 *
 * @author nish, @date 4/19/16 10:35 PM
 */

import spock.lang.Specification

class LibraryTest extends Specification{
    def "someLibraryMethod returns true"() {
        setup:
        Library lib = new Library()
        when:
        def result = lib.someLibraryMethod()
        then:
        result == true
    }
}