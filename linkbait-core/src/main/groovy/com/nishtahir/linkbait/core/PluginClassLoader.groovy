package com.nishtahir.linkbait.core

import org.jetbrains.annotations.NotNull

/**
 *  Class pluginClassLoader. Needed to expose {@link URLClassLoader#addURL(java.net.URL)}
 */
class PluginClassLoader extends URLClassLoader {
    
    PluginClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addURL(@NotNull URL url) {
        super.addURL(url)
    }
}
