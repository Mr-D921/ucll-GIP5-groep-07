package com.example.gip5groep7.Repositories;

import com.example.gip5groep7.Models.Video;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends CrudRepository<Video, Integer> {

    Optional<Video> findByFileURL(String fileUrl);
}
