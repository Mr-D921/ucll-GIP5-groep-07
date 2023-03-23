package com.example.gip5groep7;

import com.example.gip5groep7.Models.Video;
import com.example.gip5groep7.RestControllers.VideoRestController;
import com.example.gip5groep7.Services.VideoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VideoRestController videoRestController;

    @Test
    public void testHomeEndpoint() throws Exception {
        List<Video> videos = new ArrayList<>();
        Video video1 = new Video();
        video1.setName("video1");
        video1.setFileURL("url1");
        Video video2 = new Video();
        video2.setName("video2");
        video2.setFileURL("url2");
        videos.add(video1);
        videos.add(video2);
        Mockito.when(videoRestController.getAllVideos()).thenReturn(ResponseEntity.ok(videos));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/index"))
                .andExpect(model().attribute("videoList", hasSize(2)))
                .andExpect(content().string(containsString("video1")))
                .andExpect(content().string(containsString("video2")));
    }

    @Test
    public void testUploadVideoEndpoint() throws Exception {
        mockMvc.perform(get("/video/upload"))
                .andExpect(status().isOk())
                .andExpect(view().name("video/create"));
    }

    @Test
    public void testUploadVideoPostRequestEndpoint() throws Exception {
        MockMultipartFile file = new MockMultipartFile("data", "filename.mp4", MediaType.MULTIPART_FORM_DATA_VALUE, "test video".getBytes());
        when(videoRestController.uploadVideoToFirebase(any())).thenReturn("url");

        mockMvc.perform(multipart("/video/upload/post")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(view().name("video/create"))
                .andExpect(model().attribute("isCreated", "Video"));
    }

    @Test
    public void testVideoNameEndpoint() throws Exception {
        String videoName = "testVideo";
        mockMvc.perform(get("/video/{videoName}", videoName))
                .andExpect(status().isOk())
                .andExpect(view().name("video/index"))
                .andExpect(model().attribute("videoName", videoName));
    }
}
