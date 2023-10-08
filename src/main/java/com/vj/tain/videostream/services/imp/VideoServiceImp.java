package com.vj.tain.videostream.services.imp;

import com.vj.tain.videostream.bom.Video;
import com.vj.tain.videostream.repository.VideoRepository;
import com.vj.tain.videostream.services.api.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class VideoServiceImp implements VideoService {
    @Autowired
    private VideoRepository videoRepository;

    public Video publish(Video video) {
        video.setImpressions(0); // set initial impressions to 0
        video.setViews(0); // set initial views to 0
        return videoRepository.save(video);
    }

    @Override
    public List<Video> listAll() {
        return videoRepository.findAll();
    }

    @Override
    public Video updateMetadata(String vId, Video metadata) {

        Video existingVideo = videoRepository.findById(vId).orElseThrow(() -> new IllegalArgumentException("[ERROR] - Video you are trying to update doesn't exist!!"));

        Video updatedVideo = existingVideo.toBuilder()
                .title(Optional.ofNullable(metadata.getTitle()).orElse(existingVideo.getTitle()))
                .synopsis(Optional.ofNullable(metadata.getSynopsis()).orElse(existingVideo.getSynopsis()))
                .director(Optional.ofNullable(metadata.getDirector()).orElse(existingVideo.getDirector()))
                .crew(Optional.ofNullable(metadata.getCrew()).filter(crewList -> !crewList.isEmpty()).orElse(existingVideo.getCrew()))
                .yearOfRelease(updateIfNull(metadata.getYearOfRelease(), existingVideo.getYearOfRelease()))
                .genre(Optional.ofNullable(metadata.getGenre()).orElse(existingVideo.getGenre()))
                .runningTime(updateIfNull(metadata.getRunningTime(), existingVideo.getRunningTime()))
                .format(Optional.ofNullable(metadata.getFormat()).orElse(existingVideo.getFormat()))
                .build();

        return videoRepository.save(updatedVideo);
    }

    @Override
    public Video delist(String vId) {
        Video videoToDelist = videoRepository.findById(vId).orElseThrow(() -> new IllegalArgumentException("[ERROR] - Video you are trying to delist doesn't exist!!"));
        videoToDelist.setDelisted(true);
        return videoRepository.save(videoToDelist);
    }

    @Override
    public Video getById(String vId) {
        return videoRepository.findById(vId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] - Video you are trying to access doesn't exist!!"));
    }

    @Override
    public String playVideo(String vId) {
        Video playableVideo = videoRepository.findById(vId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] - Video you are trying to play doesn't exist!!"));

        //some logic to find the video location that used by the player to load video from.
        // System may build a URL here and return that
        return String.format("https://mocked_video_url.com/%s/%s", vId, playableVideo.getFormat());
    }

    private <T> T updateIfNull(T newValue, T currentValue) {
        return newValue == null ? currentValue : newValue;
    }


}
