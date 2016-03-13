package com.nishtahir.linkbait.controller

import com.nishtahir.linkbait.LinkbaitJadeTemplateEngine
import com.nishtahir.linkbait.service.LinkService
import com.nishtahir.linkbait.util.JSONUtils
import groovy.transform.Canonical
import spark.ModelAndView
import spark.template.jade.JadeTemplateEngine

import static spark.Spark.get

@Canonical
class LinkController implements IController {

    LinkService service

    @Override
    void init() {

        /**
         *  Returns JSON with List of links posted today.
         */
        get("/today/json", { request, response ->
            response.type("application/json")
            return JSONUtils.dataToPrettyJSON(service.linksPostedToday)
        });

        get("/today/", { request, response ->
            Map<String, Object> map = [:]
            map.put("pageName", "Linkbait")
            map.put("Links", service.linksPostedToday)
            return new ModelAndView(map, "links")
        }, new LinkbaitJadeTemplateEngine())

        /**
         *  Returns JSON with List of links posted this week.
         */
        get("/week", { request, response ->
            response.type("application/json")
            return JSONUtils.dataToPrettyJSON(service.linksPostedThisWeek)
        });

        /**
         *  Returns JSON with List of links posted this month.
         */
        get("/month", { request, response ->
            response.type("application/json")
            return JSONUtils.dataToPrettyJSON(service.linksPostedThisMonth)
        });

        /**
         *  Returns the top links from the most active categories
         */
        get("/digest", { request, response ->
            response.type("application/json")
            return JSONUtils.dataToPrettyJSON(service.weeklyDigest)
        });
    }
}
