package com.vj.tain.videostream.services.imp;

import com.vj.tain.videostream.bom.Metadata;
import com.vj.tain.videostream.bom.Video;
import com.vj.tain.videostream.dto.EngagementDTO;
import com.vj.tain.videostream.dto.VideoDetailsDTO;
import com.vj.tain.videostream.dto.VideoMetadataDTO;
import com.vj.tain.videostream.repository.VideoRepository;
import com.vj.tain.videostream.services.api.MetadataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class VideoServiceImpTest {

    @Autowired
    private VideoServiceImp videoServiceImp;

    @MockBean
    private VideoRepository videoRepository;

    @MockBean
    private MetadataService metadataService;

    private Metadata mockedMetadata;
    private Video mockedVideo;

    @BeforeEach
    public void setup() {

        mockedVideo = Video.builder()
                .id(UUID.randomUUID().toString())
                .content("Sample Content")
                .delisted(false)
                .build();

        mockedMetadata = Metadata.builder()
                .videoId(UUID.randomUUID().toString())
                .title("Sample Title")
                .synopsis("Sample Synopsis")
                .director("Sample Director")
                .crew(List.of("Crew1", "Crew2"))
                .yearOfRelease(2023)
                .genre("Action,Drama,Love")
                .runningTime(120)
                .format("4K")
                .build();
    }

    @Test
    public void testPublish() {
        Mockito.when(videoRepository.save(any())).thenReturn(mockedVideo);

        Video result = videoServiceImp.publish(mockedVideo);

        assertEquals(0, result.getImpressions());
        assertEquals(0, result.getViews());
        verify(videoRepository, times(1)).save(any(Video.class));
    }

    @Test
    public void testLoad() {
        String mockVideoId = UUID.randomUUID().toString();
        mockedVideo.setId(mockVideoId);

        Mockito.when(videoRepository.findById(mockVideoId)).thenReturn(Optional.of(mockedVideo));
        Mockito.when(metadataService.getOptionalByVideoId(mockVideoId)).thenReturn(Optional.of(mockedMetadata));

        VideoDetailsDTO result = videoServiceImp.load(mockVideoId);

        assertEquals(mockVideoId, result.getId());
        assertEquals(mockedMetadata, result.getMetadata());
        assertEquals(1, mockedVideo.getImpressions());
        verify(videoRepository, times(1)).findById(mockVideoId);
        verify(metadataService, times(1)).getOptionalByVideoId(mockVideoId);
    }

    @Test
    public void testDelist() {
        String mockVideoId = UUID.randomUUID().toString();
        mockedVideo.setId(mockVideoId);

        Mockito.when(videoRepository.findById(mockVideoId)).thenReturn(Optional.of(mockedVideo));
        Mockito.when(videoRepository.save(any(Video.class))).thenReturn(mockedVideo);

        Mockito.when(metadataService.delist(mockVideoId)).thenReturn(Optional.of(mockedMetadata));

        Video result = videoServiceImp.delist(mockVideoId);

        assertTrue(result.isDelisted());
        verify(videoRepository, times(1)).findById(mockVideoId);
        verify(metadataService, times(1)).delist(mockVideoId);
    }

    @Test
    public void testPlay() {
        String mockVideoId = UUID.randomUUID().toString();
        mockedVideo.setId(mockVideoId);

        Mockito.when(videoRepository.findById(mockVideoId)).thenReturn(Optional.of(mockedVideo));

        String playUrl = videoServiceImp.play(mockVideoId);

        assertTrue(playUrl.contains(mockVideoId));
        assertEquals(1, mockedVideo.getViews());
        verify(videoRepository, times(1)).findById(mockVideoId);
    }

    @Test
    public void testListAllVideosWithPartialMetadata() {
        List<VideoMetadataDTO> mockList = List.of(new VideoMetadataDTO());
        Mockito.when(videoRepository.findAllProjectedBy()).thenReturn(mockList);

        List<VideoMetadataDTO> result = videoServiceImp.listAllVideosWithPartialMetadata();

        assertEquals(mockList.size(), result.size());
    }

    @Test
    public void testSearchVideos() {
        String mockDirector = "Sample Director";
        String mockGenre = "Sample Genre";
        String mockCrew = "Sample Crew";
        List<VideoMetadataDTO> mockList = List.of(new VideoMetadataDTO());

        Mockito.when(videoRepository.search(mockDirector, mockGenre, mockCrew)).thenReturn(mockList);

        List<VideoMetadataDTO> result = videoServiceImp.searchVideos(mockDirector, mockGenre, mockCrew);

        assertEquals(mockList.size(), result.size());
    }

    @Test
    public void testListAll() {
        List<Video> mockList = List.of(mockedVideo);
        Mockito.when(videoRepository.findAll()).thenReturn(mockList);

        List<Video> result = videoServiceImp.listAll();

        assertEquals(mockList.size(), result.size());
    }

    @Test
    public void testGetEngagementStats() {
        String mockVideoId = UUID.randomUUID().toString();
        mockedVideo.setId(mockVideoId);

        Mockito.when(videoRepository.findById(mockVideoId)).thenReturn(Optional.of(mockedVideo));

        EngagementDTO result = videoServiceImp.getEngagementStats(mockVideoId);

        assertEquals(mockedVideo.getImpressions(), result.getImpressions());
        assertEquals(mockedVideo.getViews(), result.getViews());
    }

    @Test
    public void testSave() {
        Mockito.when(videoRepository.save(mockedVideo)).thenReturn(mockedVideo);

        Video result = videoServiceImp.save(mockedVideo);

        assertNotNull(result);
        assertEquals(mockedVideo, result);
    }

    @Test
    public void testLoadVideoNotFound() {
        String nonExistentVideoId = "nonExistentId";
        Mockito.when(videoRepository.findById(nonExistentVideoId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> videoServiceImp.load(nonExistentVideoId));
    }

    @Test
    public void testPlayVideoNotFound() {
        String nonExistentVideoId = "nonExistentId";
        Mockito.when(videoRepository.findById(nonExistentVideoId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> videoServiceImp.play(nonExistentVideoId));
    }

    @Test
    public void testDelistVideoNotFound() {
        String nonExistentVideoId = "nonExistentId";
        Mockito.when(videoRepository.findById(nonExistentVideoId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> videoServiceImp.delist(nonExistentVideoId));
    }


}
