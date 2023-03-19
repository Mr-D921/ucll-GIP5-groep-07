package com.example.gip5groep7.Controllers;

import com.example.gip5groep7.Models.Video;
import com.example.gip5groep7.RestControllers.VideoRestController;
import com.example.gip5groep7.Services.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    /*@RequestMapping("/{videoCode}")
    public String redirectToVideo(@PathVariable String videoCode, Model model){
        model.addAttribute("videoCodeKey",videoCode);
        return "video/index";
    }*/
    //video upload page
    @RequestMapping("/video/upload")
    public String uploadVideo(){
        return "video/create";
    }
    //routing of upload video
    //TODO this should redirect the user to an "upload successful" page
    @RequestMapping("/video/upload/post")
    public String uploadVideoPostRequest(@RequestParam("data") MultipartFile file, Model model) throws IOException {

        videoRestController.uploadVideoToFirebase(file);
        model.addAttribute("isCreated", "Video");
        return "video/create";
    }
    @RequestMapping("/video/{videoName}")
    public String testtesttest(Model model, @PathVariable String videoName) throws Exception {
        /*ResponseEntity<Object> test = videoRestController.getVideo(videoName);
        boolean isVideoFound;
        if (test != null) {
            isVideoFound = true;
        }else {
            isVideoFound = false;}*/

        model.addAttribute("videoName", videoName);

        //model.addAttribute("videoFile", test);
        //model.addAttribute("videoName", test.getHeaders().get("filename"));
        //model.addAttribute("videoFile", test);
        return "video/index";
    }

    /*@PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String createVideo(@RequestPart String name, @RequestPart MultipartFile data) {
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.name = name;
        videoRestController.createVideo(videoDTO, data);
        return "redirect:/video/upload";
    }*/
}
