package com.cydercode.homenet.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UiController {

    @RequestMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("name", "HomeNet");
        return "index";
    }

    @RequestMapping("/control-panel")
    public String controlPanel(Model model) {
        return "control-panel";
    }

    @RequestMapping("/settings")
    public String settings(Model model) {
        return "settings";
    }
}
