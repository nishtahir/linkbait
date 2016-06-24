package com.nishtahir.linkbait.core.model

import com.jcabi.aether.Classpath
import com.nishtahir.linkbait.core.DependencyResolver
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by nish on 6/23/16.
 */
class DependencyResolverTest extends Specification {

    @Shared
    DependencyResolver pluginResolver

    def setupSpec() {
        pluginResolver = new DependencyResolver()
        pluginResolver.configuration = new Configuration() {
            @Override
            File getPluginDirectory() {
                return new File("IvyTest")
            }

            @Override
            File getStaticFileDirectory() {
                return null
            }

            @Override
            File getTemporaryFileDirectory() {
                return null
            }

            @Override
            String getPluginRepository() {
                return ""
            }
        }
    }

    def "Plugin resolver should download artifact"() {
        expect:
        pluginResolver.resolveArtifactWithDependencies('org.eclipse.jetty', 'jetty-http', '9.4.0.M0') != null
    }

    def "test"(){
        Collection<File> jars = new Classpath(
                this.getProject(),
                new File(this.session.getLocalRepository().getBasedir()),
                "test" // the scope you're interested in
        );
    }
}
