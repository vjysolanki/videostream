package com.vj.tain.videostream.services.api;

import com.vj.tain.videostream.bom.Video;

import java.util.List;
import java.util.UUID;

public interface VideoService {
    Video publish(Video video);

    List<Video> listAll();

    Video updateMetadata(String vId, Video video);

}
