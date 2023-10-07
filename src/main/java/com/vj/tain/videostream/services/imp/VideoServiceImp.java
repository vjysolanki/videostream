package com.vj.tain.videostream.services.imp;

import com.vj.tain.videostream.bom.Video;
import com.vj.tain.videostream.repository.VideoRepository;
import com.vj.tain.videostream.services.api.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

        Optional<Video> videoOptional = videoRepository.findById(vId);

        if (!videoOptional.isPresent()) {
            throw new IllegalArgumentException("[ERROR] - Video you are trying to update doesn't exist!!");
        }

        Video existingVideo = videoOptional.get();
        Video updatedVideo = existingVideo.toBuilder()
                .title(Optional.ofNullable(metadata.getTitle()).orElse(existingVideo.getTitle()))
                .synopsis(Optional.ofNullable(metadata.getSynopsis()).orElse(existingVideo.getSynopsis()))
                .director(Optional.ofNullable(metadata.getDirector()).orElse(existingVideo.getDirector()))
                .crew(Optional.ofNullable(metadata.getCrew()).filter(crewList -> !crewList.isEmpty()).orElse(existingVideo.getCrew()))
                .yearOfRelease(updateIfNull(metadata.getYearOfRelease(), existingVideo.getYearOfRelease()))
                .genre(Optional.ofNullable(metadata.getGenre()).orElse(existingVideo.getGenre()))
                .runningTime(updateIfNull(metadata.getRunningTime(), existingVideo.getRunningTime()))
                .build();

        return videoRepository.save(updatedVideo);
    }

    private <T> T updateIfNull(T newValue, T currentValue) {
        return newValue == null ? currentValue : newValue;
    }


}
