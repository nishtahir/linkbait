package com.nishtahir.linkbait.core

import com.nishtahir.linkbait.core.util.Booter
import groovy.transform.Canonical
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
class DependencyResolver {

    def repositories = [
            "http://repository.jetbrains.com/all",
            "https://jitpack.io",
            "http://central.maven.org/maven2/"
    ]

    File repository

    DependencyResolver(File repository){
        this.repository = repository
    }


    private Logger LOG = LoggerFactory.getLogger(this.class.getSimpleName())

    /**
     *
     * @param groupId
     * @param artifactId
     * @param version
     * @return
     */
    public File resolveArtifact(com.nishtahir.linkbait.core.model.Dependency dependency) {
        String artifactIdentifier = dependency.toString()
        LOG.debug("Resolving artifact: $artifactIdentifier")

        RepositorySystem repositorySystem = Booter.newRepositorySystem();
        RepositorySystemSession session = Booter.newRepositorySystemSession(repositorySystem);

        Artifact artifact = new DefaultArtifact(artifactIdentifier);

        ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.setArtifact(artifact);
        artifactRequest.setRepositories(Booter.newRepositories(repositories));

        ArtifactResult artifactResult = repositorySystem.resolveArtifact(session, artifactRequest);

        artifact = artifactResult.getArtifact();
        LOG.debug("Artifact resolved at: ${artifact.getFile().toString()}")
        return artifact.getFile()
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

        LOG.debug("Resolving artifact with dependencies: $artifactIdentifier")

        RepositorySystem repositorySystem = Booter.newRepositorySystem();
        RepositorySystemSession session = Booter.newRepositorySystemSession(repositorySystem, repository);

        Artifact artifact = new DefaultArtifact(artifactIdentifier);
        DependencyFilter classpathFilter = DependencyFilterUtils.classpathFilter(JavaScopes.COMPILE);

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(new Dependency(artifact, JavaScopes.COMPILE));
        collectRequest.setRepositories(Booter.newRepositories(repositories));

        DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, classpathFilter);

        List<ArtifactResult> artifactResults =
                repositorySystem.resolveDependencies(session, dependencyRequest).getArtifactResults();

        return artifactResults.collect {
            LOG.debug("Artifact resolved at: ${it.artifact.getFile().toString()}")
            it.artifact.file
        }
    }

}
