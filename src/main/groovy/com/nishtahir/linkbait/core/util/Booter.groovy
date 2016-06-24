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
     * @return
     */
    public static DefaultRepositorySystemSession newRepositorySystemSession(RepositorySystem system) {
        return newRepositorySystemSession(system, new File(DEFAULT_REPO));
    }

    /**
     *
     * @param system
     * @param repository
     * @return
     */
    public static DefaultRepositorySystemSession newRepositorySystemSession(RepositorySystem system, File repository) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        LocalRepository localRepo = new LocalRepository(repository);
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));
        return session;
    }

    /**
     *
     * @param system
     * @param session
     * @return
     */
    public static List<RemoteRepository> newRepositories(RepositorySystem system, RepositorySystemSession session) {
        return new ArrayList<RemoteRepository>(Arrays.asList(newCentralRepository()));
    }

    /**
     *
     * @return
     */
    private static RemoteRepository newCentralRepository() {
        return new RemoteRepository.Builder("central", "default", "http://central.maven.org/maven2/").build();
    }

    /**
     * 
     * @param url
     * @return
     */
    private static RemoteRepository newCentralRepository(String url){
        return new RemoteRepository.Builder("central", "default", url).build();
    }

}
