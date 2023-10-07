package com.vj.tain.videostream.repository;

import com.vj.tain.videostream.bom.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VideoRepository extends JpaRepository<Video, String> {
}
