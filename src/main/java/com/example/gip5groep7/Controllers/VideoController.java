package com.example.gip5groep7.Controllers;

import com.example.gip5groep7.Models.Video;
import com.example.gip5groep7.Models.VideoDTO;
import com.example.gip5groep7.Services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping
    public ResponseEntity<VideoDTO> createVideo(@RequestPart VideoDTO videoDTO, @RequestPart MultipartFile data) {
        try {
            //Check whether a file was uploaded, and whether it was a mp4 file. Return bad request if either is false
            if(data == null || data.isEmpty() || (data.getContentType() != null && !data.getContentType().equals("video/mp4"))) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            videoDTO.data = data.getBytes();
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Video video = videoService.createVideo(videoDTO);

        VideoDTO responseDTO = videoToDTO(video);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Resource> getVideoById(@PathVariable("id") int id) {
        return ResponseEntity.ok(new ByteArrayResource(videoService.findVideoById(id).getData()));
    }

    @GetMapping
    public ResponseEntity<Iterable<Video>> getAllVideos() {
        return new ResponseEntity<>(videoService.listAllVideos(), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<VideoDTO> updateVideo(@PathVariable("id") int id, @RequestBody VideoDTO videoDTO/*, @RequestPart MultipartFile data*/) {
        //TODO: must the video data be overwrite-able?
        /*
        try {
            videoDTO.data = data.getBytes();
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }*/
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
        dto.data = video.getData();
        dto.tags = video.getTags();

        return dto;
    }
}