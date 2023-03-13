package com.example.gip5groep7.Models;

import java.time.LocalDateTime;
import java.util.List;

public class VideoDTO {

    public String name;

    public int views;
    public int playtime;

    public LocalDateTime uploadDate;
    //TODO: integrate with User class
    //public User uploader;

    public List<String> tags;

    public String fileURL;

    public VideoDTO() {}

}