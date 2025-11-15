package com.zapastore.zapastore_h2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Value("${saludo.bienvenida}")
    private String saludo;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("saludo", saludo);
        return "home"; // Muestra WEB-INF/jsp/home.jsp
    }
}
