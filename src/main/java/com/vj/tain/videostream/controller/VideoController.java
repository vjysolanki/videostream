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
        Video savedVideo = videoService.publish(video);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVideo);
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<Video> delistVideo(@PathVariable String videoId, @RequestParam(defaultValue = "false") boolean soft) {
        idValidation(videoId);
        // XXX: handle hard delete if required
        Video videoToDelist = videoService.delist(videoId);
        return ResponseEntity.ok(videoToDelist);
    }

    @GetMapping("/{videoId}/load")
    public ResponseEntity<VideoDetailsDTO> loadVideoById(@PathVariable String videoId) {
        idValidation(videoId);
        VideoDetailsDTO videoToReturn = videoService.load(videoId);
        return ResponseEntity.ok(videoToReturn);
    }

    @GetMapping("/{videoId}/play")
    public ResponseEntity<String> playVideo(@PathVariable String videoId) {
        idValidation(videoId);
        String videoUrl = videoService.play(videoId);
        return ResponseEntity.ok(videoUrl);
    }

    @GetMapping
    public ResponseEntity<List<VideoMetadataDTO>> listAllVideos() {
        List<VideoMetadataDTO> videos = videoService.listAllVideosWithPartialMetadata();
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/only")
    public ResponseEntity<List<Video>> listAllVideosWithFullDetails() {
        List<Video> videoList = videoService.listAll();
        return ResponseEntity.ok(videoList);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchVideos(
            @RequestParam(value = "director", required = false) String directorName,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "crew", required = false) String crewName
    ) {

        List<VideoMetadataDTO> videos = videoService.searchVideos(directorName, genre, crewName);

        return ResponseEntity.ok(videos);
    }

    @GetMapping("/{videoId}/engagement")
    public ResponseEntity<EngagementDTO> getVideoEngagement(@PathVariable String videoId) {
        idValidation(videoId);
        EngagementDTO engagement = videoService.getEngagementStats(videoId);
        return ResponseEntity.ok(engagement);
    }

    private static void idValidation(String videoId) {
        if (null == videoId || videoId.isBlank()) {
            throw new IllegalArgumentException("[ERROR] - Video ID is missing!!");
        }
    }

//    @GetMapping("/search/director/{directorName}")
//    public ResponseEntity<List<VideoMetadataProjection>> findVideosByDirector(@PathVariable String directorName) {
//        List<VideoMetadataProjection> videos = videoService.findVideosByDirector(directorName);
//        return ResponseEntity.ok(videos);
//    }
//
}
