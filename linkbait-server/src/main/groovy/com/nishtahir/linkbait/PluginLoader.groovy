package com.nishtahir.linkbait

import com.nishtahir.linkbait.core.request.RequestHandler
import com.nishtahir.linkbait.model.Plugin
import com.nishtahir.linkbait.util.JsonUtils
import groovy.io.FileType
import groovy.transform.Canonical

/**
 * Class loader to loadPluginsFromPath language tools dynamically
 * at runtime, this helps decouple to the project into
 * more manageable components.
 */
@Singleton
@Canonical
class PluginLoader {

    /**
     * Name of plugin file to load.
     */
    public static final PLUGIN_FILE = 'plugin.json'

    /**
     * List of handlers loaded from plugins.
     */
    List<RequestHandler> handlers = [];

    /**
     * @param path path to plugin folder
     * @throws FileNotFoundException
     */
    public void loadPluginsFromPath(String path) throws FileNotFoundException {
        new File(path).eachFile(FileType.FILES) {
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
    public void loadPlugin(File file) throws Exception {
        URLClassLoader child = new URLClassLoader([file.toURI().toURL()] as URL[], this.getClass().getClassLoader())
        String json = child.getResourceAsStream(PLUGIN_FILE).text
        Plugin plugin = JsonUtils.jsonToPlugin(json)

        Class<RequestHandler> clazz = (Class<RequestHandler>) Class.forName(plugin.handler, true, child)
        RequestHandler instance = clazz.newInstance()
        handlers.add(instance);
        println "Loaded: ${plugin}"
    }

}

