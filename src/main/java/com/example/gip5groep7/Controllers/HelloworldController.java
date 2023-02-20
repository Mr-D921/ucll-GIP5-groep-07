package com.example.gip5groep7.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloworldController {
    @GetMapping("/helloworld")
    public String helloworld(@RequestParam(name = "name", required = false, defaultValue = "world") String name, Model model){
        model.addAttribute("name", name);
        return "helloworld";
    }
}
