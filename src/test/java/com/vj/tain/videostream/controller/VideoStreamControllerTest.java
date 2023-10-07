//package com.vj.tain.videostream.controller;
//
//import com.vj.tain.videostream.bom.Video;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class VideoStreamControllerTest {
//
//    @Autowired
//    private VideoStreamController videoStreamController;
//
//    @Test
//    public void testPublishVideo() {
//        Video video = new Video();
//        // set attributes for video
//        video.setTitle("Test Title");
//        // ... other attributes ...
//
//        Video savedVideo = videoStreamController.publishVideo(video).getBody();
//        assertNotNull(savedVideo);
//        assertEquals("Test Title", savedVideo.getTitle());
//        // ... other assertions ...
//    }
//
//    // Add more tests for other methods
//}
