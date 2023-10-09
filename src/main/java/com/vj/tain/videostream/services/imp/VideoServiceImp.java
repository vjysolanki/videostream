package com.vj.tain.videostream.services.imp;

import com.vj.tain.videostream.bom.Metadata;
import com.vj.tain.videostream.bom.Video;
import com.vj.tain.videostream.dto.EngagementDTO;
import com.vj.tain.videostream.dto.VideoDetailsDTO;
import com.vj.tain.videostream.dto.VideoMetadataDTO;
import com.vj.tain.videostream.repository.VideoRepository;
import com.vj.tain.videostream.services.api.MetadataService;
import com.vj.tain.videostream.services.api.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class VideoServiceImp implements VideoService {
    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private MetadataService metadataService;

    public Video publish(Video video) {
        video.setImpressions(0); // set initial impressions to 0
        video.setViews(0); // set initial views to 0
        return save(video);
    }

    private Video getById(String vId) {
        return videoRepository.findById(vId)
                .orElseThrow(() -> new EntityNotFoundException("[ERROR] - Video you are trying to access doesn't exist!!"));
    }

    //    @Override
//    public Video load(String vId) {
//        Video existingVideo = getById(vId);
//        // Increment the impressions count
//        existingVideo.setImpressions(existingVideo.getImpressions() + 1);
//        return save(existingVideo);
//    }
    @Override
    public VideoDetailsDTO load(String videoId) {
        Video existingVideo = getById(videoId);

        VideoDetailsDTO videoDetails = new VideoDetailsDTO();
        videoDetails.setId(existingVideo.getId());
        videoDetails.setContent(existingVideo.getContent());
        videoDetails.setDelisted(existingVideo.isDelisted());
        Optional<Metadata> metadata = metadataService.getOptionalByVideoId(videoId);
        if (metadata.isPresent()) {
            videoDetails.setMetadata(metadata.get());
        }
        // Increment the impressions count
        existingVideo.setImpressions(existingVideo.getImpressions() + 1);
        save(existingVideo);
        return videoDetails;
    }

    @Transactional
    @Override
    public Video delist(String vId) {
        Video videoToDelist = getById(vId);

        videoToDelist.setDelisted(true);
//        XXX: Hard Delete metadata ?
        //soft delete metadata
        metadataService.delist(vId);
        return save(videoToDelist);
    }

    @Override
    public String play(String vId) {
        Video playableVideo = getById(vId);
        //some logic to find the video location that used by the player to load video from.
        // System may build a URL here and return that
        playableVideo.setViews(playableVideo.getViews() + 1);
        save(playableVideo);
        return String.format("https://mocked_video_url.com/%s", vId);
    }

    @Transactional
    @Override
    public Video save(Video video) {
        return videoRepository.save(video);
    }

    @Override
    public List<VideoMetadataDTO> listAllVideosWithPartialMetadata() {
        return videoRepository.findAllProjectedBy();
    }

    //    @Override
//    public List<VideoMetadataProjection> findVideosByDirector(String director) {
//        return videoRepository.findByDirector(director);
//    }
//
    @Override
    public List<VideoMetadataDTO> searchVideos(String director, String genre, String crew) {
        return videoRepository.search(director,genre,crew);
    }

    @Override
    public List<Video> listAll() {
        return videoRepository.findAll();
    }

    @Override
    public EngagementDTO getEngagementStats(String videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Video not found with ID: " + videoId));

        return new EngagementDTO(video.getImpressions(), video.getViews());
    }

}
