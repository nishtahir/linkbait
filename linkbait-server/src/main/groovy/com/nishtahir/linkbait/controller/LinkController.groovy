package com.nishtahir.linkbait.controller

import com.nishtahir.linkbait.LinkbaitJadeTemplateEngine
import com.nishtahir.linkbait.service.LinkService
import com.nishtahir.linkbait.util.JsonUtils
import groovy.transform.Canonical
import spark.ModelAndView

import static spark.Spark.get

@Canonical
class LinkController implements IController {

    LinkService service

    @Override
    void init() {

        /**
         *  Returns JSON with List of links posted today.
         */
        get("/*/today/json", { request, response ->
            String group = request.splat()[0]
            response.type("application/json")
            return JsonUtils.dataToPrettyJson(service.getLinksPostedToday(group))
        });

        get("/*/today", { request, response ->
            Map<String, Object> map = [:]
            String group = request.splat()[0]
            map.put("pageName", "Linkbait")
            map.put("when", "today")
            map.put("Links", service.getLinksPostedToday(group))
            return new ModelAndView(map, "links")
        }, new LinkbaitJadeTemplateEngine())

        /**
         *  Returns JSON with List of links posted this week.
         */
        get("/*/week/json", { request, response ->
            response.type("application/json")
            String group = request.splat()[0]
            return JsonUtils.dataToPrettyJson(service.getLinksPostedThisWeek(group))
        });

        get("/*/week", { request, response ->
            Map<String, Object> map = [:]
            String group = request.splat()[0]
            map.put("pageName", "Linkbait")
            map.put("when", "this week")
            map.put("Links", service.getLinksPostedThisWeek(group))
            return new ModelAndView(map, "links")
        }, new LinkbaitJadeTemplateEngine())

        /**
         *  Returns JSON with List of links posted this month.
         */
        get("/*/month/json", { request, response ->
            response.type("application/json")
            String group = request.splat()[0]
            return JsonUtils.dataToPrettyJson(service.getLinksPostedThisMonth(group))
        });

        get("/*/month", { request, response ->
            Map<String, Object> map = [:]
            String group = request.splat()[0]
            map.put("pageName", "Linkbait")
            map.put("when", "this month")
            map.put("Links", service.getLinksPostedThisMonth(group))
            return new ModelAndView(map, "links")
        }, new LinkbaitJadeTemplateEngine())

        /**
         *  Returns the top links from the most active categories
         */
        get("/*/digest/json", { request, response ->
            response.type("application/json")
            String group = request.splat()[0]
            return JsonUtils.dataToPrettyJson(service.getWeeklyDigest(group))
        });

        get("/*/digest", { request, response ->
            Map<String, Object> map = [:]
            String group = request.splat()[0]
            map.put("pageName", "Linkbait")
            map.put("when", "last week")
            map.put("Links", service.getWeeklyDigest(group))
            return new ModelAndView(map, "links")
        }, new LinkbaitJadeTemplateEngine())

    }
}
