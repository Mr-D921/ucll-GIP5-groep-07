package com.example.gip5groep7.Services;

import com.example.gip5groep7.Models.FirebaseCredential;
import com.example.gip5groep7.Models.Video;
import com.example.gip5groep7.Models.VideoDTO;
import com.example.gip5groep7.Repositories.VideoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.StorageOptions;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import org.apache.commons.io.IOUtils;
import com.google.cloud.storage.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepo;

    //test video upload to firebase
    //private final Logger log = LoggerFactory.getLogger(FirebaseStorageStrategy.class);
    private final Environment environment;

    private StorageOptions storageOptions;
    private String bucketName;
    private String projectId;
    public VideoService(Environment environment) {
        this.environment = environment;
    }
    @PostConstruct
    private void initializeFirebase() throws Exception {
        bucketName = environment.getRequiredProperty("FIREBASE_BUCKET_NAME");
        projectId = environment.getRequiredProperty("FIREBASE_PROJECT_ID");

        InputStream firebaseCredential = createFirebaseCredential();
        this.storageOptions = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(GoogleCredentials.fromStream(firebaseCredential)).build();

    }
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        File file = convertMultiPartToFile(multipartFile);
        Path filePath = file.toPath();
        String objectName = generateFileName(multipartFile);

        Storage storage = storageOptions.getService();

        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("video/mp4").build();

        ((com.google.cloud.storage.Storage) storage).create(blobInfo, Files.readAllBytes(filePath));


        System.out.println(("File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName));
        return objectName;
    }
    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }

    private InputStream createFirebaseCredential() throws Exception {
        //private key
        String privateKey = environment.getRequiredProperty("FIREBASE_PRIVATE_KEY").replace("\\n", "\n");

        FirebaseCredential firebaseCredential = new FirebaseCredential();
        firebaseCredential.setType(environment.getRequiredProperty("FIREBASE_TYPE"));
        firebaseCredential.setProject_id(projectId);
        firebaseCredential.setPrivate_key_id("FIREBASE_PRIVATE_KEY_ID");
        firebaseCredential.setPrivate_key(privateKey);
        firebaseCredential.setClient_email(environment.getRequiredProperty("FIREBASE_CLIENT_EMAIL"));
        firebaseCredential.setClient_id(environment.getRequiredProperty("FIREBASE_CLIENT_ID"));
        firebaseCredential.setAuth_uri(environment.getRequiredProperty("FIREBASE_AUTH_URI"));
        firebaseCredential.setToken_uri(environment.getRequiredProperty("FIREBASE_TOKEN_URI"));
        firebaseCredential.setAuth_provider_x509_cert_url(environment.getRequiredProperty("FIREBASE_AUTH_PROVIDER_X509_CERT_URL"));
        firebaseCredential.setClient_x509_cert_url(environment.getRequiredProperty("FIREBASE_CLIENT_X509_CERT_URL"));

        //serialize with Jackson
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(firebaseCredential);

        //convert jsonString string to InputStream using Apache Commons
        return IOUtils.toInputStream(jsonString);
    }
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }

    public ResponseEntity<Object> downloadFile(String fileName) throws Exception {
        Storage storage = storageOptions.getService();

        Blob blob = storage.get(BlobId.of(bucketName, fileName));
        ReadChannel reader = blob.reader();
        InputStream inputStream = Channels.newInputStream(reader);

        byte[] content = null;

        content = IOUtils.toByteArray(inputStream);

        final ByteArrayResource byteArrayResource = new ByteArrayResource(content);
        //byteArrayResource.getFilename();
        //TODO change filename
        return ResponseEntity
                .ok()
                .contentLength(content.length)
                .header("Content-type", "video/mp4")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "bananatest.mp4" + "\"")
                .body(byteArrayResource);

    }

    public String deleteVideoFromFirebaseAndDatabase(String fileName){
        //delete video file from the project based on file name
        Storage storage = storageOptions.getService();
        storage.delete(BlobId.of(bucketName, fileName));
        System.out.println("File '" + fileName + "' successfully deleted from bucket.");

        //delete video from sql database
        videoRepo.delete(videoRepo.findByFileURL(fileName).get());
        System.out.println("File '" + fileName + "' successfully deleted from sql database.");
        return fileName + " is successfully deleted from server.";
    }

    public Video createVideo(VideoDTO videoDTO) {
        Video newVideo = new Video(
                videoDTO.name,
                videoDTO.playtime,
                videoDTO.tags,
                videoDTO.fileURL
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
            //video.setViews(videoDTO.views);
            //video.setPlaytime(videoDTO.playtime);
            //video.setUploadDate(videoDTO.uploadDate);
            video.setTags(videoDTO.tags);
            //video.setFileURL(videoDTO.fileURL);

            return video;
        }
        else {
            return null; //TODO: throw Exception instead?
        }
    }

    public VideoDTO getVideoDTOByName(String videoName){
        Video video = videoRepo.findByFileURL(videoName).get();
        return convertVideoTOVideoDTO(video);
    }
    private VideoDTO convertVideoTOVideoDTO(Video video){
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.views = video.getViews();
        videoDTO.name = video.getName();
        videoDTO.tags = video.getTags();
        videoDTO.fileURL = video.getFileURL();
        return videoDTO;
    }
    public void updateVideoViewCount(String filename){
        Video video = videoRepo.findByFileURL(filename).get();
        video.incrementViewCount();
        videoRepo.save(video);
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
