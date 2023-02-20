package com.example.gip5groep7.Models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Video {

    //region Variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;

    @Column
    private int views;
    @Column
    private int playtime;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;
    //TODO: integrate with User class
    //@Column
    //private User uploader;

    @ElementCollection
    private List<String> tags;

    //Save contents of the video file as an array bytes, which can later be reinterpreted
    //Tag lob will cause the data to be saved as a BLOB (binary large object) in the database
    @Lob
    private byte[] data;

    //endregion

    public Video() {}

    public Video(String name, int playtime, List<String> tags, byte[] data) {
        this.name = name;
        this.views = 0;
        this.playtime = playtime; //TODO: figure out how to extract playtime from video data
        this.uploadDate = LocalDateTime.now();
        this.tags = tags;
        this.data = data;
    }

    public Video(String name, int views, int playtime, LocalDateTime uploadDate, List<String> tags, byte[] data) {
        this.name = name;
        this.views = views;
        this.playtime = playtime;
        this.uploadDate = uploadDate;
        this.tags = tags;
        this.data = data;
    }

    //region Getters & Setters

    //region Id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //endregion

    //region Name

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //endregion

    //region Views

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    //endregion

    //region Playtime

    public int getPlaytime() {
        return playtime;
    }

    public void setPlaytime(int playTime) {
        this.playtime = playTime;
    }

    //endregion

    //region UploadDate

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    //endregion

    //region Tags

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    //endregion

    //region Data

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    //endregion

    //endregion
}
