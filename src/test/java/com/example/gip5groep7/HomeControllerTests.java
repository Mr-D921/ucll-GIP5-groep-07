package com.example.gip5groep7;

import com.example.gip5groep7.Controllers.HomeController;
import com.example.gip5groep7.Models.Video;
import com.example.gip5groep7.RestControllers.VideoRestController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class HomeControllerTests {

    private static final String HOME_VIEW_NAME = "home/index";
    private static final String VIDEO_VIEW_NAME = "video/index";
    private static final String CREATE_VIDEO_VIEW_NAME = "video/create";

    @Mock
    private VideoRestController videoRestController;

    @Mock
    private Model model;

    @InjectMocks
    private HomeController homeController;

    @Test
    public void testHomeMethod() {
        // Setup
        List<Video> videoList = new ArrayList<>();
        Video video1 = new Video();
        video1.setId(1);
        video1.setName("video1");
        video1.setViews(100);
        videoList.add(video1);
        ResponseEntity<Iterable<Video>> responseEntity = new ResponseEntity<>(videoList, HttpStatus.OK);
        when(videoRestController.getAllVideos()).thenReturn(responseEntity);

        // Exercise
        String viewName = homeController.home(model);

        // Verify
        assertEquals(HOME_VIEW_NAME, viewName);
        verify(model).addAttribute(eq("videoList"), anyList());
    }

    @Test
    public void testRedirectToVideoMethod() {
        // Setup
        String videoCodeKey = "abc";
        Model model = mock(Model.class);

        // Exercise
        String viewName = homeController.redirectToVideo(videoCodeKey, model);

        // Verify
        assertEquals(VIDEO_VIEW_NAME, viewName);
        verify(model).addAttribute(eq("videoCodeKey"), eq(videoCodeKey));
    }

    @Test
    public void testUploadVideoMethod() {
        // Exercise
        String viewName = homeController.uploadVideo();

        // Verify
        assertEquals(CREATE_VIDEO_VIEW_NAME, viewName);
    }
}
