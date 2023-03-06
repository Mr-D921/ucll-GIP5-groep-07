package com.example.gip5groep7.Controllers;

import com.example.gip5groep7.Models.Video;
import com.example.gip5groep7.Models.VideoDTO;
import com.example.gip5groep7.RestControllers.VideoRestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HomeController {
    VideoRestController videoRestController;
    @GetMapping("/")
    public String home(Model model){
        //Iterable<Video> allVideos = videoRestController.getAllVideos().getBody();
        //model.addAttribute(allVideos);
        return "home/index";
    }
    @RequestMapping("/{videoCode}")
    public String redirectToVideo(@PathVariable String videoCode, Model model){
        model.addAttribute(videoCode);
        return "video/index";
    }

    @RequestMapping("/video/upload")
    public String uploadVideo(){
        return "video/create";
    }

    /*@PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String createVideo(@RequestPart String name, @RequestPart MultipartFile data) {
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.name = name;
        videoRestController.createVideo(videoDTO, data);
        return "redirect:/video/upload";
    }*/
}
