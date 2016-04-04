package com.nishtahir.linkbait.controller;

import com.nishtahir.linkbait.LinkbaitJadeTemplateEngine
import spark.ModelAndView

import static spark.Spark.get

/**
 * Created by nish on 4/4/16.
 */
public class LandingController implements IController {
    @Override
    public void init() {
        get("/", { request, response ->
                Map<String, Object> map = [:]
        return new ModelAndView(map, "index")
        }, new LinkbaitJadeTemplateEngine())

    }
}
