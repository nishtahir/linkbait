package com.nishtahir.linkbait;

import de.neuland.jade4j.template.ClasspathTemplateLoader;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Fixes the filename extension bug in Jade
 */
public class LinkbaitClasspathTemplateLoader extends ClasspathTemplateLoader {

    private static final String suffix = ".jade";


    private String templateRoot;

    /**
     * Construct a classpath loader using the given template root.
     *
     * @param templateRoot the template root directory
     */
    public LinkbaitClasspathTemplateLoader(String templateRoot) {
        if (!templateRoot.endsWith(File.separator)) {
            templateRoot += File.separator;
        }
        this.templateRoot = templateRoot;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reader getReader(String name) throws IOException {
        name = templateRoot + name;
        String extension = FilenameUtils.getExtension(name);
        if ("".equals(extension)) name = name + suffix;


        if (App.isDebug()){
            ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
            URL resURL = ctxLoader.getResource(name);
            URLConnection resConn = null;
            if (resURL != null) {
                resConn = resURL.openConnection();
                resConn.setUseCaches(false);
            }
            assert resConn != null;
            InputStream resIn = resConn.getInputStream();
            return  new InputStreamReader(resIn);
        }

        return new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(name), getEncoding());
    }
}
