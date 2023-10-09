package com.vj.tain.videostream.controller;

import com.vj.tain.videostream.bom.Video;
import com.vj.tain.videostream.dto.EngagementDTO;
import com.vj.tain.videostream.dto.VideoDetailsDTO;
import com.vj.tain.videostream.dto.VideoMetadataDTO;
import com.vj.tain.videostream.services.api.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/videos")
@Slf4j
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping
    public ResponseEntity<Video> publishVideo(@Valid @RequestBody Video video) {
        log.info(video.toString());
        Video savedVideo = videoService.publish(video);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVideo); // returning the saved video
    }

    @PutMapping("/{videoId}/delist")
    public ResponseEntity<Video> delistVideo(@PathVariable String videoId) {
        if (null == videoId || videoId.isBlank()) {
            throw new IllegalArgumentException("[ERROR] - Video ID is missing!!");
        }
        Video videoToDelist = videoService.delist(videoId);
        return ResponseEntity.ok(videoToDelist);
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<VideoDetailsDTO> loadVideoById(@PathVariable String videoId) {
        if (null == videoId || videoId.isBlank()) {
            throw new IllegalArgumentException("[ERROR] - Video ID is missing!!");
        }
        VideoDetailsDTO videoToReturn = videoService.load(videoId);
        return ResponseEntity.ok(videoToReturn);
    }

    @GetMapping("/{videoId}/play")
    public ResponseEntity<String> playVideo(@PathVariable String videoId) {
        if (null == videoId || videoId.isBlank()) {
            throw new IllegalArgumentException("[ERROR] - Video ID is missing!!");
        }
        String videoUrl = videoService.play(videoId);
        return ResponseEntity.ok(videoUrl);
    }


    @GetMapping
    public ResponseEntity<List<VideoMetadataDTO>> listAllVideos() {
        List<VideoMetadataDTO> videos = videoService.listAllVideosWithPartialMetadata();
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Video>> listAllVideosWithFullDetails() {
        List<Video> videoList = videoService.listAll();
        return ResponseEntity.ok(videoList);
    }


    //Search
    //use - /videos/search?director=SomeDirectorName&genre=Action
    @GetMapping("/search")
    public ResponseEntity<?> searchVideos(
            @RequestParam(value = "director", required = false) String directorName,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "crew", required = false) String crewName
    ) {

        List<VideoMetadataDTO> videos = null;
//        if (directorName == null && genre == null && crewName == null) {
//            videos = videoService.listAllVideosWithPartialMetadata();
//        } else
        {
            videos = videoService.searchVideos(directorName, genre, crewName);
        }

        return ResponseEntity.ok(videos);
    }

    @GetMapping("/{videoId}/engagement")
    public ResponseEntity<EngagementDTO> getVideoEngagement(@PathVariable String videoId) {
        EngagementDTO engagement = videoService.getEngagementStats(videoId);
        return ResponseEntity.ok(engagement);
    }


//    @GetMapping("/search/director/{directorName}")
//    public ResponseEntity<List<VideoMetadataProjection>> findVideosByDirector(@PathVariable String directorName) {
//        List<VideoMetadataProjection> videos = videoService.findVideosByDirector(directorName);
//        return ResponseEntity.ok(videos);
//    }
//
}
