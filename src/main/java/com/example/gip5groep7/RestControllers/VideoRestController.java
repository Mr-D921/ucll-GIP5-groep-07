package com.example.gip5groep7.RestControllers;

import com.example.gip5groep7.Models.Video;
import com.example.gip5groep7.Models.VideoDTO;
import com.example.gip5groep7.Services.VideoService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class VideoRestController {

    @Autowired
    public VideoService videoService;

    // upload video to firebase
    // upload video url to sql database
    // it returns the file name which can be used to download the video
    @PostMapping("/video/upload")
    public String uploadVideoToFirebase(@RequestParam("data") MultipartFile file, String name, String tags) throws IOException {
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.fileURL = videoService.uploadFile(file);
        videoDTO.views = 0;
        videoDTO.name = name;
        videoDTO.tags = Arrays.asList(tags.split("\\s*,\\s*"));
        videoService.createVideo(videoDTO);
        return videoDTO.fileURL;
    }

    // download video from firebase based on file name
    @GetMapping("/video/{filename}")
    public ResponseEntity<Object> getVideo(@PathVariable String filename) throws Exception {
        return videoService.downloadFile(filename);
    }

    /*@GetMapping(value = "test/video/{title}", produces = "video/mp4")
    public Mono<Resource> getVideosTest(@PathVariable String title, @RequestHeader("Range") String range){
        Firestore dbFirestore = FirestoreClient.getFirestore();

        System.out.println("range in bytes(): " + range);
        return videoService.getVideo(title);
    }*/

    @PostMapping
    public ResponseEntity<VideoDTO> createVideo(@RequestPart String name, @RequestPart MultipartFile data) {
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.name = name;
        //TODO: implement way to save video url (javascript?)
        /*
        try {
            videoDTO.data = data.getBytes();
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
         */
        Video video = videoService.createVideo(videoDTO);

        VideoDTO responseDTO = videoToDTO(video);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /*@GetMapping("{id}")
    public ResponseEntity<String> getVideoById(@PathVariable("id") int id) {
        //TODO: check whether just sending the url is fine
        return ResponseEntity.ok(videoService.findVideoById(id).getFileURL());
    }*/

    @GetMapping
    public ResponseEntity<Iterable<Video>> getAllVideos() {
        return new ResponseEntity<>(videoService.listAllVideos(), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<VideoDTO> updateVideo(@PathVariable("id") int id, @RequestPart VideoDTO videoDTO) {
        Video video = videoService.updateVideo(id, videoDTO);

        VideoDTO responseDTO = videoToDTO(video);

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> deleteVideo(@PathVariable int id) {
        return new ResponseEntity<>(videoService.deleteVideo(id), HttpStatus.OK);
    }

    private VideoDTO videoToDTO(Video video) {
        VideoDTO dto = new VideoDTO();

        dto.name = video.getName();
        dto.views = video.getViews();
        dto.playtime = video.getPlaytime();
        dto.uploadDate = video.getUploadDate();
        dto.fileURL = video.getFileURL();
        dto.tags = video.getTags();

        return dto;
    }
}