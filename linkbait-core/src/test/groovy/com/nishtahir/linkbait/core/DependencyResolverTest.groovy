package com.nishtahir.linkbait.core

import com.nishtahir.linkbait.core.DependencyResolver
import com.nishtahir.linkbait.core.model.Dependency
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by nish on 6/23/16.
 */
class DependencyResolverTest extends Specification {

    def setupSpec() {

    }

    /**
     * Requires network. Test locally do not commit uncommented to repo.
     */
    def "Plugin resolver should download artifact"() {
//        given:
//        File testDir = new File("test-repo")
//        testDir.mkdirs()
//        DependencyResolver resolver = new DependencyResolver(testDir)
//        Dependency dependency = new Dependency(group: 'org.eclipse.jetty', artifact: 'jetty-http', version: '9.4.0.M0')
//        expect:
//        resolver.resolveArtifactWithDependencies(dependency) != null
//        testDir.delete()
    }

}
