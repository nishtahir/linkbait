package com.nishtahir.linkbait;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.template.JadeTemplate;
import spark.ModelAndView;
import spark.template.jade.JadeTemplateEngine;

import java.io.IOException;
import java.util.Map;

/**
 * Need this class to load {@link LinkbaitClasspathTemplateLoader}
 */
public class LinkbaitJadeTemplateEngine extends JadeTemplateEngine {

    private JadeConfiguration configuration;

    /**
     * Construct a jade template engine defaulting to the 'templates' directory
     * under the resource path.
     */
    public LinkbaitJadeTemplateEngine() {
        this("templates");
    }

    public LinkbaitJadeTemplateEngine(String templateRoot) {
        configuration = new JadeConfiguration();
        if (App.isDebug()) {
            configuration.setCaching(false);
        }
        configuration.setTemplateLoader(new LinkbaitClasspathTemplateLoader(templateRoot));
    }

    public LinkbaitJadeTemplateEngine(JadeConfiguration configuration) {
        this.configuration = configuration;
    }

    public JadeConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public String render(ModelAndView modelAndView) {
        try {
            JadeTemplate template = configuration.getTemplate(modelAndView.getViewName());
            return configuration.renderTemplate(template,
                    (Map<String, Object>) modelAndView.getModel());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
