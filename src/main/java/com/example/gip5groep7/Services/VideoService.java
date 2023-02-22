package com.example.gip5groep7.Services;

import com.example.gip5groep7.Models.Video;
import com.example.gip5groep7.Models.VideoDTO;
import com.example.gip5groep7.Repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepo;

    public Video createVideo(VideoDTO videoDTO) {
        Video newVideo = new Video(
                videoDTO.name,
                videoDTO.playtime,
                videoDTO.tags,
                videoDTO.data
        );

        return videoRepo.save(newVideo);
    }

    public Iterable<Video> listAllVideos() {
        return videoRepo.findAll();
    }

    public Video findVideoById(int id) {
        Optional<Video> value = videoRepo.findById(id);
        Video video = null;

        if(value.isPresent()) {
            video = value.get();
        }

        return video;
    }

    public Video updateVideo(int id, VideoDTO videoDTO) {
        Optional<Video> value = videoRepo.findById(id);
        if(value.isPresent()) {
            Video video = value.get();

            video.setName(videoDTO.name);
            video.setViews(videoDTO.views);
            video.setPlaytime(videoDTO.playtime);
            video.setUploadDate(videoDTO.uploadDate);
            video.setTags(videoDTO.tags);
            video.setData(videoDTO.data);

            return video;
        }
        else {
            return null; //TODO: throw Exception instead?
        }
    }

    public boolean deleteVideo(int id) {
        Optional<Video> video = videoRepo.findById(id);
        boolean success = false;

        if(video.isPresent()) {
            videoRepo.deleteById(id);
            success = true;
        }

        return success;
    }
}
