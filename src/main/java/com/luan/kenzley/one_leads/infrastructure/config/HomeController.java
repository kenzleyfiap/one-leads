package com.luan.kenzley.one_leads.infrastructure.config;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "redirect:/swagger-ui/index.html";
    }
}
