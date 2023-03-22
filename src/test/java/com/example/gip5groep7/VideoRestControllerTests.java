package com.example.gip5groep7.RestControllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.example.gip5groep7.Models.Video;
import com.example.gip5groep7.Models.VideoDTO;
import com.example.gip5groep7.Services.VideoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(VideoRestController.class)
public class VideoRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private VideoService videoService;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void uploadVideoToFirebaseTest() throws Exception {
        // create mock file
        String content = "example video content";
        InputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file = new MockMultipartFile("data", "test.mp4", "video/mp4", stream);

        // create mock VideoDTO object
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setFileURL("https://example.com/test.mp4");

        // mock videoService behavior
        when(videoService.uploadFile(any(MultipartFile.class))).thenReturn("https://example.com/test.mp4");
        when(videoService.createVideo(any(VideoDTO.class))).thenReturn(new Video());

        // perform request
        MvcResult result = mockMvc.perform(multipart("/api/video/upload")
                        .file(file))
                .andExpect(status().isOk())
                .andReturn();

        // verify response
        String response = result.getResponse().getContentAsString();
        assertEquals("https://example.com/test.mp4", response);

        // verify service calls
        verify(videoService, times(1)).uploadFile(any(MultipartFile.class));
        verify(videoService, times(1)).createVideo(any(VideoDTO.class));
    }

    @Test
    void getVideoTest() throws Exception {
        // Setup
        String filename = "example.mp4";
        String expectedContentType = "video/mp4";
        byte[] expectedData = {0x12, 0x34, 0x56, 0x78}; // Example data
        HttpHeaders expectedHeaders = new HttpHeaders();
        expectedHeaders.setContentType(MediaType.parseMediaType(expectedContentType));
        expectedHeaders.setContentLength(expectedData.length);
        expectedHeaders.set("Content-Range", "bytes 0-" + (expectedData.length - 1) + "/" + expectedData.length);

        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(new ByteArrayResource(expectedData), expectedHeaders, HttpStatus.OK);

        when(videoService.downloadFile(filename)).thenReturn(expectedResponse);

        // Execution
        VideoRestController videoRestController;
        ResponseEntity<Object> actualResponse = videoRestController.getVideo(filename);

        // Assertion
        assertEquals(expectedContentType, actualResponse.getHeaders().getContentType().toString());
        assertEquals(expectedData.length, actualResponse.getBody().toString().getBytes().length);
        assertEquals(expectedResponse, actualResponse);
    }
}