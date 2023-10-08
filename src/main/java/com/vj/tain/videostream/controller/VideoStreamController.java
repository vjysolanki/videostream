package com.vj.tain.videostream.controller;

import com.vj.tain.videostream.bom.Video;
import com.vj.tain.videostream.services.api.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/videos")
@Slf4j
public class VideoStreamController {

    @Autowired
    private VideoService videoService;

    @PostMapping
    public ResponseEntity<Video> publishVideo(@RequestBody Video video) {
        log.info(video.toString());
        Video savedVideo = videoService.publish(video);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVideo); // returning the saved video
    }

    @PutMapping("/{videoId}/metadata")
    public ResponseEntity<Video> updateMetadata(@PathVariable String videoId, @RequestBody Video video) {
        if (null == videoId || videoId.isBlank()) {
            throw new IllegalArgumentException("[ERROR] - Video ID is empty!!");
        }
        Video updatedVideo = videoService.updateMetadata(videoId, video);
        return new ResponseEntity<>(updatedVideo, HttpStatus.OK);
    }

    @PutMapping("/{videoId}/delist")
    public ResponseEntity<Video> delistVideo(@PathVariable String videoId) {
        if (null == videoId || videoId.isBlank()) {
            throw new IllegalArgumentException("[ERROR] - Video ID is empty!!");
        }
        Video videoToDelist = videoService.delist(videoId);
        return ResponseEntity.ok(videoToDelist);
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<Video> getVideoById(@PathVariable String videoId) {
        if (null == videoId || videoId.isBlank()) {
            throw new IllegalArgumentException("[ERROR] - Video ID is empty!!");
        }
        Video videoToReturn = videoService.getById(videoId);
        return ResponseEntity.ok(videoToReturn);
    }

    @GetMapping("/{videoId}/play")
    public ResponseEntity<String> playVideo(@PathVariable String videoId) {
        if (null == videoId || videoId.isBlank()) {
            throw new IllegalArgumentException("[ERROR] - Video ID is empty!!");
        }
        String videoUrl = videoService.playVideo(videoId);
        return ResponseEntity.ok(videoUrl);
    }

    // API to retrieve all videos
    @GetMapping
    public ResponseEntity<List<Video>> listAllVideos() {
        List<Video> videoList = videoService.listAll();
        return ResponseEntity.ok(videoList);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final String illegalArgumentExceptionHandler(final IllegalArgumentException e) {

        return '"' + e.getMessage() + '"';
    }
}
