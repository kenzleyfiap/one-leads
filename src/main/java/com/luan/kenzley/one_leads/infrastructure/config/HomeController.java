package com.luan.kenzley.one_leads.infrastructure.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "redirect:/swagger-ui/index.html";
    }
}
