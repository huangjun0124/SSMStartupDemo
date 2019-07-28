package com.wishuok.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/swagger")
public class SwaggerController {

    @RequestMapping("")
    public String home(){
        return "redirect:/swagger-ui.html";
    }

}
