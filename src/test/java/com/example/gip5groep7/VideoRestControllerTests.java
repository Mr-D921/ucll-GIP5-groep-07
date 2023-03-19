package com.example.gip5groep7;

import com.example.gip5groep7.Models.Video;
import com.example.gip5groep7.Models.VideoDTO;
import com.example.gip5groep7.RestControllers.VideoRestController;
import com.example.gip5groep7.Services.VideoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VideoRestControllerTests {

    @Mock
    private VideoService videoService;

    @InjectMocks
    private VideoRestController videoRestController;

    @Test
    public void testCreateVideo() {
        // Given
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.name = "test video";
        videoDTO.fileURL = "test video url";
        when(videoService.createVideo(any(VideoDTO.class))).thenReturn(new Video());

        // When
        ResponseEntity<VideoDTO> response = videoRestController.createVideo("test video", null);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetVideoById() {
        // Given
        Video video = new Video();
        video.setFileURL("test video url");
        when(videoService.findVideoById(anyInt())).thenReturn(video);

        // When
        ResponseEntity<String> response = videoRestController.getVideoById(1);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetAllVideos() {
        // Given
        List<Video> videos = new ArrayList<>();
        videos.add(new Video());
        videos.add(new Video());
        when(videoService.listAllVideos()).thenReturn(videos);

        // When
        ResponseEntity<Iterable<Video>> response = videoRestController.getAllVideos();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().iterator().hasNext());
    }

}
