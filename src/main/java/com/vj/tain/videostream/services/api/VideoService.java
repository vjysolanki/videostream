package com.vj.tain.videostream.services.api;

import com.vj.tain.videostream.bom.Video;
import com.vj.tain.videostream.dto.EngagementDTO;
import com.vj.tain.videostream.dto.RawVideoDTO;
import com.vj.tain.videostream.dto.VideoDetailsDTO;
import com.vj.tain.videostream.dto.VideoMetadataDTO;

import java.util.List;

public interface VideoService {
    public Video publishRaw(RawVideoDTO rawVideoDTO);

    public VideoDetailsDTO load(String vId);

    public Video delist(String vId);

    public String play(String vId);

    public Video save(Video video);

    List<VideoMetadataDTO> listAllVideosWithPartialMetadata();

//    List<VideoMetadataProjection> findVideosByDirector(String director);

    public List<Video> listAll();

    List<VideoMetadataDTO> searchVideos(String director, String genre, String crew);

    EngagementDTO getEngagementStats(String videoId);

}
