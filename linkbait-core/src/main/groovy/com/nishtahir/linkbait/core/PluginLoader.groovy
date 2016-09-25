package com.nishtahir.linkbait.core

import com.nishtahir.linkbait.core.util.DependencyUtils
import com.nishtahir.linkbait.plugin.Plugin
import com.nishtahir.linkbait.plugin.model.Configuration
import groovy.io.FileType
import groovy.util.logging.Slf4j
import org.jetbrains.annotations.NotNull

import java.util.jar.JarInputStream
import java.util.jar.Manifest
import java.util.logging.Logger

/**
 * Class pluginClassLoader to loadPluginsFromPath language tools dynamically
 * at runtime, this helps decouple to the project into
 * more manageable components.
 *
 * Information is loaded from the manifest
 */
@Slf4j
class PluginLoader {

    /**
     * Base of the application package. Used to ensure that
     * we don't try to download duplicate packages.
     */
    public static final ROOT_PACKAGE_NAME = 'com.nishtahir'

    /**
     * Application config
     */
    Configuration configuration

    PluginLoader(@NotNull Configuration configuration) {
        this.configuration = configuration
    }

    /**
     * List of plugins loaded from plugins.
     */
    Map<String, Plugin> plugins = [:]

    /**
     * Classloader where all the magic happens.
     */
    PluginClassLoader pluginClassLoader = new PluginClassLoader(new URL[0], getClass().classLoader)

    /**
     * @param path path to plugin folder
     * @throws FileNotFoundException
     */
    public void loadPluginsFromJar(File path) throws FileNotFoundException {
        path.eachFile(FileType.FILES) {
            if (it.name.endsWith(".jar")) {
                try {
                    loadPlugin(it)
                } catch (Exception e) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Loaded class should be a singleton.
     * @param files
     * @throws Exception
     */
    public void loadPlugin(File plugin) throws Exception {
        Manifest mf = getManifest(plugin)
        def attributes = mf.getMainAttributes()

        String pluginClassName = attributes.getValue('Plugin-Class')
        String pluginId = attributes.getValue('Plugin-Id')
        String pluginVersion = attributes.getValue('Plugin-Version')
        String pluginProvider = attributes.getValue('Plugin-Provider')
        String pluginDescription = attributes.getValue('Plugin-Description')
        String dependencies = attributes.getValue('Dependencies')

        log.info("Loading: $plugin.path")
        log.info("Plugin-Id: $pluginId")
        log.info("Plugin-Class: $pluginClassName")
        log.info("Plugin-Provider: $pluginProvider")
        log.info("Plugin-Version: $pluginVersion")
        log.info("Plugin-Descriprion: $pluginDescription")

        DependencyResolver resolver = new DependencyResolver(configuration.pluginRepository)
        List<File> files = DependencyUtils.parseDependencies(dependencies).findAll {
            //All of this should already be in the classpath
            //After all, they are compile time dependencies for the framework
            !it.group.startsWith(ROOT_PACKAGE_NAME) && it.group != "linkbait"
        }.collectMany {
            resolver.resolveArtifactWithDependencies(it)
        }

        addJarsToClasspath(files)
        addJarToClasspath(plugin)
        Class<Plugin> clazz = (Class<Plugin>) Class.forName(pluginClassName, true, pluginClassLoader)
        plugins.put(pluginId, clazz.newInstance())
    }

    /**
     *
     * @param jar
     * @return
     */
    private static Manifest getManifest(File jar) {
        InputStream input = new FileInputStream(jar);
        JarInputStream jarStream = new JarInputStream(input);
        Manifest manifest = jarStream.getManifest();
        return manifest
    }

    /**
     * Load list of jars into classpath.
     * @param jars
     */
    void addJarsToClasspath(List<File> jars) {
        jars.each { jar ->
            addJarToClasspath(jar)
        }
    }

    /**
     * Loads a single jar onto the classpath.
     * @param jar file to load.
     */
    void addJarToClasspath(File jar) {
        pluginClassLoader.addURL(jar.toURI().toURL())
    }

}
