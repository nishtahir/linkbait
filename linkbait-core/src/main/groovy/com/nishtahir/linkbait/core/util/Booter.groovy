package com.nishtahir.linkbait.core.util

import org.apache.maven.repository.internal.MavenRepositorySystemUtils
import org.eclipse.aether.DefaultRepositorySystemSession
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.RepositorySystemSession
import org.eclipse.aether.repository.LocalRepository
import org.eclipse.aether.repository.RemoteRepository

/**
 * A helper to boot the repository system and a repository system session.
 */
class Booter {

    /**
     * Default artifact repository
     */
    private static final String DEFAULT_REPO = "data/repo"

    /**
     *
     * @return
     */
    public static RepositorySystem newRepositorySystem() {
        return ManualRepositorySystemFactory.newRepositorySystem();
    }

    /**
     *
     * @param system
     * @param repository
     * @return
     */
    static DefaultRepositorySystemSession newRepositorySystemSession(RepositorySystem system,
                                                                     File repository = new File(DEFAULT_REPO)) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        LocalRepository localRepo = new LocalRepository(repository);
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));
        return session
    }

    /**
     *
     * @param system
     * @param session
     * @return
     */
    public static List<RemoteRepository> newRepositories(List<String> urls) {
        return urls.collect() { url ->
            newCentralRepository(url)
        }
    }

    /**
     *
     * @param url
     * @return
     */
    private static RemoteRepository newCentralRepository(String url = "http://central.maven.org/maven2/") {
        return new RemoteRepository.Builder("central", "default", url).build();
    }

}
