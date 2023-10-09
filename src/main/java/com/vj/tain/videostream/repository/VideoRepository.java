package com.vj.tain.videostream.repository;

import com.vj.tain.videostream.bom.Video;
import com.vj.tain.videostream.dto.VideoMetadataDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, String> {
//    @Query("SELECT new com.vj.tain.videostream.dto.VideoMetadataDTO(m.title, m.director, m.crew, m.genre, m.runningTime) " +
//            "FROM Video v, Metadata m " +
//            "WHERE v.id = m.videoId")
//    List<VideoMetadataDTO> findAllProjectedBy();

    @Query("SELECT new com.vj.tain.videostream.dto.VideoMetadataDTO(m.title, m.director, m.crew, m.genre, m.runningTime) " +
            "FROM Video v JOIN Metadata m ON v.id = m.videoId")
    List<VideoMetadataDTO> findAllProjectedBy();



//    @Query("SELECT new com.vj.tain.videostream.dto.VideoMetadataDTO(m.title, m.director, m.crew, m.genre, m.runningTime) " +
//            "FROM Video v JOIN v.metadata m WHERE m.director = :director")
//    List<VideoMetadataProjection> findByDirector(@Param("director") String director);
//
//    @Query("SELECT new com.vj.tain.videostream.dto.VideoMetadataDTO(m.title, m.director, m.crew, m.genre, m.runningTime) " +
//            "FROM Video v JOIN v.metadata m WHERE m.genre = :genre")
//    List<VideoMetadataProjection> findByGenre(@Param("genre") String genre);
//
//    @Query("SELECT new com.vj.tain.videostream.dto.VideoMetadataDTO(m.title, m.director, m.crew, m.genre, m.runningTime) " +
//            "FROM Video v JOIN v.metadata m WHERE :crewName MEMBER OF m.crew")
//    List<VideoMetadataProjection> findByCrew(@Param("crewName") String crewName);

    @Query("SELECT new com.vj.tain.videostream.dto.VideoMetadataDTO(m.title, m.director, m.crew, m.genre, m.runningTime) " +
            "FROM Video v, Metadata m " +
            "WHERE v.id = m.videoId " +
            "AND (:director IS NULL OR m.director = :director) " +
            "AND (:genre IS NULL OR m.genre = :genre) " +
            "AND (:crew IS NULL OR m.crew LIKE CONCAT('%', :crew, '%'))")
    List<VideoMetadataDTO> search(@Param("director") String director, @Param("genre") String genre, @Param("crew") String crew);

}
