package com.example.customcakes_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    @RequestMapping(value = {
            "/",
            "/detail/**",
            "/explore",
            "/howtouse",
            "/makeorder",
            "/manage",
            "/myorder",
            "/myorder_store",
            "/auth/**",
            "/popup/**"
    })
    public String forward() {
        return "forward:/index.html";
    }
}
