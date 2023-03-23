package com.example.gip5groep7;

import com.example.gip5groep7.Models.Video;
import com.example.gip5groep7.Models.VideoDTO;
import com.example.gip5groep7.RestControllers.VideoRestController;
import com.example.gip5groep7.Services.VideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class VideoRestControllerTests {

    private MockMvc mockMvc;

    @Mock
    private VideoService videoService;

    @InjectMocks
    private VideoRestController videoRestController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(videoRestController).build();
    }

    @Test
    public void testUploadVideoToFirebase_success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("data", "test.mp4", MediaType.MULTIPART_FORM_DATA_VALUE,
                "test video content".getBytes());
        String name = "Test Video";
        String tags = "tag1,tag2";

        when(videoService.uploadFile(any(MultipartFile.class))).thenReturn("https://firebase-storage-url/test.mp4");

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/video/upload")
                        .file(file)
                        .param("name", name)
                        .param("tags", tags))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("https://firebase-storage-url/test.mp4"));
    }

    /*@Test
    public void testUploadVideoToFirebase_failure() throws Exception {
        MockMultipartFile file = new MockMultipartFile("data", "test.mp4", MediaType.MULTIPART_FORM_DATA_VALUE,
                "test video content".getBytes());
        String name = "Test Video";
        String tags = "tag1,tag2";

        when(videoService.uploadFile(any(MultipartFile.class))).thenThrow(new IOException("File upload failed."));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/video/upload")
                        .file(file)
                        .param("name", name)
                        .param("tags", tags))
                .andExpect(status().isBadRequest())
                .andExpect((ResultMatcher) content().string("File upload failed."));

    }*/

    @Test
    public void testGetVideo() throws Exception {
        String fileName = "test.mp4";
        byte[] fileContent = "test video content".getBytes();

        // Mock the video service to return a response entity
        ResponseEntity<Object> responseEntity = ResponseEntity.ok()
                .contentLength(fileContent.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .body(fileContent);
        when(videoService.downloadFile(anyString())).thenReturn(responseEntity);

        // Send a GET request to the controller and assert the response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/video/" + fileName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().bytes(fileContent));
    }

    /*@Test
    public void givenInvalidFileName_whenGetVideo_thenReturnsNotFound() throws Exception {
        // Arrange
        String fileName = "nonexistent.mp4";
        when(videoService.downloadFile(anyString())).thenReturn(null);

        // Act and Assert
        mockMvc.perform(get("/api/video/{filename}", fileName))
                .andExpect(status().isNotFound());
    }*/

    @Test
    public void testCreateVideoSuccess() throws Exception {
        // Arrange
        String name = "Test Video";
        byte[] content = {0, 1, 2, 3, 4};
        MockMultipartFile file = new MockMultipartFile("data", "test.mp4", "video/mp4", content);

        VideoDTO videoDTO = new VideoDTO();
        videoDTO.name = name;
        videoDTO.fileURL = "test.mp4";
        videoDTO.tags = List.of("test");

        Video video = new Video();
        video.setId(1);
        video.setName(name);
        video.setFileURL("test.mp4");
        video.setTags(List.of("test"));

        //when(videoService.createVideo(videoDTO)).thenReturn(video);
        when(videoService.createVideo(any(VideoDTO.class))).thenReturn(video);

        ResponseEntity<VideoDTO> expectedResponseEntity = new ResponseEntity<>(videoDTO, HttpStatus.CREATED);

        // Act
        ResponseEntity<VideoDTO> actualResponseEntity = videoRestController.createVideo(name, file);

        // Assert
        assertEquals(expectedResponseEntity, actualResponseEntity);
    }

    /*@Test
    public void testCreateVideoFailure() throws Exception {
        // Arrange
        String name = "Test Video";
        byte[] content = {0, 1, 2, 3, 4};
        MockMultipartFile file = new MockMultipartFile("data", "test.mp4", "video/mp4", content);

        when(videoService.createVideo(new VideoDTO())).thenReturn(null);

        ResponseEntity<VideoDTO> expectedResponseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        // Act
        ResponseEntity<VideoDTO> actualResponseEntity = videoRestController.createVideo(name, file);

        // Assert
        assertEquals(expectedResponseEntity, actualResponseEntity);
    }*/

    @Test
    public void testGetAllVideos() {
        // Create some sample videos
        Video video1 = new Video("Video 1", 0, 60, null, Arrays.asList("tag1", "tag2"), "url1");
        Video video2 = new Video("Video 2", 0, 120, null, Arrays.asList("tag3", "tag4"), "url2");
        List<Video> videos = new ArrayList<>(Arrays.asList(video1, video2));

        // Mock the video service to return the sample videos
        when(videoService.listAllVideos()).thenReturn(videos);

        // Call the getAllVideos endpoint
        ResponseEntity<Iterable<Video>> responseEntity = videoRestController.getAllVideos();

        // Verify that the HTTP status code is OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify that the response body contains the sample videos
        assertEquals(videos, responseEntity.getBody());
    }

    @Test
    public void testGetAllVideosWhenNoVideosExist() {
        // Mock the video service to return an empty list of videos
        when(videoService.listAllVideos()).thenReturn(new ArrayList<>());

        // Call the getAllVideos endpoint
        ResponseEntity<Iterable<Video>> responseEntity = videoRestController.getAllVideos();

        // Verify that the HTTP status code is OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify that the response body is an empty list
        assertEquals(new ArrayList<>(), responseEntity.getBody());
    }

    @Test
    void deleteVideo_Success() throws Exception {
        // Arrange
        int videoId = 1;

        // Act
        mockMvc.perform(delete("/videos/{id}", videoId))
                .andExpect(status().isOk());

        // Assert
        verify(videoService, times(1)).deleteVideo(videoId);
    }


}
