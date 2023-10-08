package com.vj.tain.videostream.services.imp;

import com.vj.tain.videostream.bom.Video;
import com.vj.tain.videostream.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VideoServiceImpTest {

    @InjectMocks
    private VideoServiceImp videoService;

    @Mock
    private VideoRepository videoRepository;

    private Video videoWithMetadata;
    private Video onlyVideo;

    @BeforeEach
    public void setup() {

        onlyVideo = new Video();
        onlyVideo.setContent("Sample Content Mock without Metadata");
        onlyVideo.setDelisted(false);

        videoWithMetadata = new Video();
        videoWithMetadata.setContent("Sample Content Mock");
        videoWithMetadata.setTitle("Sample Title");
        videoWithMetadata.setSynopsis("Sample Synopsis");
        videoWithMetadata.setDirector("Sample Director");
        videoWithMetadata.setGenre("Sample Genre");
        videoWithMetadata.setYearOfRelease(2023);
        videoWithMetadata.setRunningTime(120);
        videoWithMetadata.setCrew(Arrays.asList("Actor 1", "Actor 2", "Actor 3"));
        videoWithMetadata.setFormat("mp4");
        videoWithMetadata.setDelisted(false);
    }

    @Test
    public void testPublish() {
        when(videoRepository.save(any(Video.class))).thenReturn(onlyVideo);

        Video result = videoService.publish(onlyVideo);

        assertNotNull(result);
        assertNotNull(onlyVideo.getContent());
        assertNull(onlyVideo.getCrew());
        assertNull(onlyVideo.getTitle());
//        assertEquals("Sample Title", result.getTitle());
//        assertEquals("Sample Synopsis", result.getSynopsis());
//        assertEquals("Sample Director", result.getDirector());
//        assertEquals("Sample Genre", result.getGenre());
//        assertEquals(2023, result.getYearOfRelease());
//        assertEquals(120, result.getRunningTime());
//        assertEquals("Sample Content Mock", result.getContent());
//        assertEquals(Arrays.asList("Actor 1", "Actor 2", "Actor 3"), result.getCrew());
        assertFalse(result.isDelisted());

        assertEquals(0, result.getImpressions());
        assertEquals(0, result.getViews());

        verify(videoRepository, times(1)).save(any(Video.class));
    }

    @Test
    public void testListAll() {
        when(videoRepository.findAll()).thenReturn( List.of(videoWithMetadata));

        List<Video> videos = videoService.listAll();

        assertNotNull(videos);
        assertFalse(videos.isEmpty());
        assertEquals(1, videos.size());
    }

    @Test
    public void testUpdateMetadataVideoNotFound() {
        when(videoRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> videoService.updateMetadata("fakeId", videoWithMetadata));
    }

    @Test
    public void testUpdateMetadata_Success() {
        when(videoRepository.findById(any(String.class))).thenReturn(Optional.of(videoWithMetadata));
        String updateTitle = "Updated Title";
        videoWithMetadata.setTitle(updateTitle);
        when(videoRepository.save(any(Video.class))).thenReturn(videoWithMetadata);

        Video updateData = new Video();
        updateData.setTitle(updateTitle);

        Video result = videoService.updateMetadata(UUID.randomUUID().toString(), updateData);

        assertNotNull(result);
        assertEquals(updateTitle, result.getTitle());
        assertEquals("Sample Synopsis", result.getSynopsis()); // Unchanged values remain the same

        verify(videoRepository, times(1)).findById(any(String.class));
        verify(videoRepository, times(1)).save(any(Video.class));
    }

    @Test
    public void testUpdateMetadata_VideoNotFound() {
        when(videoRepository.findById(any(String.class))).thenReturn(Optional.empty());

        Video updateData = new Video();
        updateData.setTitle("Updated Title");

        assertThrows(IllegalArgumentException.class, () -> {
            videoService.updateMetadata(UUID.randomUUID().toString(), updateData);
        });

        verify(videoRepository, times(1)).findById(any(String.class));
        verify(videoRepository, times(0)).save(any(Video.class)); // Save should not be called
    }

    @Test
    public void testUpdateMetadata_PartialUpdate() {
        when(videoRepository.findById(any(String.class))).thenReturn(Optional.of(videoWithMetadata));
        String updateDirector = "Updated Director";
        videoWithMetadata.setDirector(updateDirector);
        when(videoRepository.save(any(Video.class))).thenReturn(videoWithMetadata);

        Video updateData = new Video();
        updateData.setDirector(updateDirector);

        Video result = videoService.updateMetadata(UUID.randomUUID().toString(), updateData);

        assertNotNull(result);
        assertEquals("Sample Title", result.getTitle());  // Unchanged
        assertEquals(updateDirector, result.getDirector());

        verify(videoRepository, times(1)).findById(any(String.class));
        verify(videoRepository, times(1)).save(any(Video.class));
    }
}

