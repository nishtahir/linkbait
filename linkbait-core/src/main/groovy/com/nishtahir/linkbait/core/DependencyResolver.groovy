package com.nishtahir.linkbait.core

import com.nishtahir.linkbait.core.util.Booter
import groovy.util.logging.Log
import groovy.util.logging.Slf4j
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.RepositorySystemSession
import org.eclipse.aether.artifact.Artifact
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.collection.CollectRequest
import org.eclipse.aether.graph.Dependency
import org.eclipse.aether.graph.DependencyFilter
import org.eclipse.aether.resolution.ArtifactRequest
import org.eclipse.aether.resolution.ArtifactResult
import org.eclipse.aether.resolution.DependencyRequest
import org.eclipse.aether.util.artifact.JavaScopes
import org.eclipse.aether.util.filter.DependencyFilterUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * This has a big job. Downloads plugins from the plugin repository.
 */
@Slf4j
class DependencyResolver {

    /**
     * Maven repositories to search for dependencies.
     */
    def repositories = [
            "http://repository.jetbrains.com/all",
            "https://jitpack.io",
            "http://central.maven.org/maven2/"
    ]

    File repository

    DependencyResolver(File repository) {
        this.repository = repository
    }

    /**
     *
     * @param groupId
     * @param artifactId
     * @param version
     * @return
     */
    public List<File> resolveArtifactWithDependencies(com.nishtahir.linkbait.core.model.Dependency dependency) {

        String artifactIdentifier = dependency.toString()
        log.info("Resolving artifact with dependencies: $artifactIdentifier")

        RepositorySystem repositorySystem = Booter.newRepositorySystem();
        RepositorySystemSession session = Booter.newRepositorySystemSession(repositorySystem, repository);

        Artifact artifact = new DefaultArtifact(artifactIdentifier);
        DependencyFilter classpathFilter = DependencyFilterUtils.classpathFilter(JavaScopes.COMPILE);

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(new Dependency(artifact, JavaScopes.COMPILE));
        collectRequest.setRepositories(Booter.newRepositories(repositories));

        DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, classpathFilter);

        return repositorySystem.resolveDependencies(session, dependencyRequest)
                .getArtifactResults().collect {
            log.info("Located artificat at: ${it.artifact.getFile().toString()}")
            it.artifact.file
        }
    }

}
