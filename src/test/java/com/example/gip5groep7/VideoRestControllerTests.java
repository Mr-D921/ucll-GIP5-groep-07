package com.example.gip5groep7;

import com.example.gip5groep7.Models.Video;
import com.example.gip5groep7.Models.VideoDTO;
import com.example.gip5groep7.RestControllers.VideoRestController;
import com.example.gip5groep7.Services.VideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideoRestControllerTest {

    private VideoRestController controller;

    @Mock
    private VideoService service;

    @BeforeEach
    void setUp() {
        controller = new VideoRestController();
        controller.videoService = service;
    }

    /*@Test
    void uploadVideoToFirebase_returnsUrl() throws IOException {
        MultipartFile file = // create a mock MultipartFile object
                String url = "https://example.com/video.mp4";
        when(service.uploadFile(any(MultipartFile.class))).thenReturn(url);

        String result = controller.uploadVideoToFirebase(file);

        assertEquals(url, result);
    }*/

    @Test
    public void testUploadVideoToFirebase() throws IOException {
        // create mock VideoService
        VideoService videoServiceMock = Mockito.mock(VideoService.class);

        // create instance of VideoRestController
        VideoRestController videoRestController = new VideoRestController();
        videoRestController.videoService = videoServiceMock;

        // create mock MultipartFile
        byte[] data = "test data".getBytes();
        MockMultipartFile file = new MockMultipartFile("data", "test.mp4", "video/mp4", data);

        // set up mock VideoDTO
        String expectedFileUrl = "https://example.com/test.mp4";
        VideoDTO expectedVideoDTO = new VideoDTO();
        expectedVideoDTO.fileURL = expectedFileUrl;

        // set up mock VideoService method calls
        Mockito.when(videoServiceMock.uploadFile(file)).thenReturn(expectedFileUrl);
        Mockito.when(videoServiceMock.createVideo(expectedVideoDTO)).thenReturn(new Video());

        // call method being tested
        String actualFileUrl = videoRestController.uploadVideoToFirebase(file);

        // assert results
        assertEquals(expectedFileUrl, actualFileUrl);
        Mockito.verify(videoServiceMock).uploadFile(file);
        Mockito.verify(videoServiceMock).createVideo(expectedVideoDTO);
    }


    /*@Test
    void getVideo_returnsVideo() throws Exception {
        String filename = "video.mp4";
        ResponseEntity<Object> responseEntity = // create a mock response entity
                when(service.downloadFile(filename)).thenReturn(responseEntity);

        ResponseEntity<Object> result = controller.getVideo(filename);

        assertEquals(responseEntity, result);
    }*/

    /*@Test
    void createVideo_returnsCreatedVideo() {
        String name = "Video Title";
        MultipartFile file = // create a mock MultipartFile object
                VideoDTO videoDTO = new VideoDTO();
        videoDTO.name = name;
        when(service.createVideo(videoDTO)).thenReturn(new Video());

        ResponseEntity<VideoDTO> responseEntity = controller.createVideo(name, file);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(name, responseEntity.getBody().name);
    }*/

    @Test
    void updateVideo_returnsUpdatedVideo() {
        int id = 1;
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.name = "Updated Title";
        Video updatedVideo = new Video();
        when(service.updateVideo(id, videoDTO)).thenReturn(updatedVideo);

        ResponseEntity<VideoDTO> responseEntity = controller.updateVideo(id, videoDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedVideo.getName(), responseEntity.getBody().name);
    }

    @Test
    void deleteVideo_returnsTrue() {
        int id = 1;
        when(service.deleteVideo(id)).thenReturn(true);

        ResponseEntity<Boolean> responseEntity = controller.deleteVideo(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(true, responseEntity.getBody());
    }
}
