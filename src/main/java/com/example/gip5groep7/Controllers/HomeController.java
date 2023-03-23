package com.example.gip5groep7.Controllers;

import com.example.gip5groep7.Models.Video;
import com.example.gip5groep7.Models.VideoDTO;
import com.example.gip5groep7.Repositories.VideoRepository;
import com.example.gip5groep7.RestControllers.VideoRestController;
import com.example.gip5groep7.Services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    VideoService videoService;
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

    //video upload page
    @RequestMapping("/video/upload")
    public String uploadVideo(){
        return "video/create";
    }
    //routing of upload video
    //TODO this should redirect the user to an "upload successful" page
    @RequestMapping("/video/upload/post")
    public String uploadVideoPostRequest(@RequestParam("data") MultipartFile file, Model model, @RequestParam("videoName") String name, @RequestParam("tags") String tagsStr) throws IOException {

        videoRestController.uploadVideoToFirebase(file, name, tagsStr);
        model.addAttribute("isCreated", "Video");
        return "video/success";
    }
    @RequestMapping("/video/{videoName}")
    public String getVideoByName(Model model, @PathVariable String videoName) throws Exception {
        VideoDTO videoDTO = videoService.getVideoDTOByName(videoName);
        videoService.updateVideoViewCount(videoName);
       // model.addAttribute("videoName", videoName);
        model.addAttribute("Video", videoDTO);
        //model.addAttribute("videoFile", test);
        //model.addAttribute("videoName", test.getHeaders().get("filename"));
        //model.addAttribute("videoFile", test);
        return "video/index";
    }
    @GetMapping("/video/delete/{filename}")
    public String deleteVideo(@PathVariable String filename){
        videoService.deleteVideoFromFirebaseAndDatabase(filename);
        return "home/index";
    }


}
