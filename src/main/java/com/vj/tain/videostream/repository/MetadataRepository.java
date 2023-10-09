package com.vj.tain.videostream.repository;

import com.vj.tain.videostream.bom.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetadataRepository extends JpaRepository<Metadata, String> {
    Optional<Metadata> findByVideoId(String videoId);
}
