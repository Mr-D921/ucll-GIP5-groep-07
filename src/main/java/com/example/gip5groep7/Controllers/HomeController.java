package com.example.gip5groep7.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model){

        return "home/index";
    }
    @RequestMapping("/{videoCode}")
    public String redirectToVideo(@PathVariable String videoCode, Model model){
        model.addAttribute(videoCode);
        return "video/index";
    }
}
