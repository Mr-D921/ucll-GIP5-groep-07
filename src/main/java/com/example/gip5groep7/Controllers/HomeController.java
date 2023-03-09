package com.example.gip5groep7.Controllers;

import com.example.gip5groep7.Models.Video;
import com.example.gip5groep7.RestControllers.VideoRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {
    final
    VideoRestController videoRestController;

    public HomeController(VideoRestController videoRestController) {
        this.videoRestController = videoRestController;
    }

    @GetMapping("/")
    public String home(Model model){
        ResponseEntity<Iterable<Video>> allVideosResponseEntity = videoRestController.getAllVideos();
        if (allVideosResponseEntity != null) {
            Iterable<Video> allVideos = allVideosResponseEntity.getBody();
            model.addAttribute("videoList", allVideos);
        }

        return "home/index";
    }
    @RequestMapping("/{videoCode}")
    public String redirectToVideo(@PathVariable String videoCode, Model model){
        model.addAttribute("videoCodeKey",videoCode);
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
