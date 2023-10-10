package com.vj.tain.videostream.services.imp;

import com.vj.tain.videostream.bom.Metadata;
import com.vj.tain.videostream.bom.Video;
import com.vj.tain.videostream.repository.MetadataRepository;
import com.vj.tain.videostream.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MetadataServiceImpTest {

    @InjectMocks
    private MetadataServiceImp metadataServiceImp;

    @Mock
    private MetadataRepository metadataRepository;

    @Mock
    private VideoRepository videoRepository;

    private Video mockedVideo;
    private Metadata mockedMetadata;

    @BeforeEach
    public void setup() {
        mockedVideo = Video.builder()
                .id("someId")
                .content("Sample Content")
                .delisted(false)
                .build();

        mockedMetadata = Metadata.builder()
                .videoId("someId")
                .title("Sample Title")
                .synopsis("Sample Synopsis")
                .director("Sample Director")
                .crew(List.of("Crew1", "Crew2"))
                .yearOfRelease(2023)
                .genre("Drama,Comedy")
                .runningTime(120)
                .format("4K")
                .build();
    }

    @Test
    public void testAddMetadataForVideo() {
        when(metadataRepository.findByVideoId(anyString())).thenReturn(Optional.empty());
        when(videoRepository.findById(anyString())).thenReturn(Optional.of(mockedVideo));
        when(metadataRepository.save(any(Metadata.class))).thenReturn(mockedMetadata);

        Metadata result = metadataServiceImp.add("someId", mockedMetadata);
        assertNotNull(result);
        assertEquals("Sample Title", result.getTitle());
    }

    @Test
    public void testUpdateMetadataForVideo() {
        when(metadataRepository.findByVideoId(anyString())).thenReturn(Optional.of(mockedMetadata));
        when(videoRepository.findById(anyString())).thenReturn(Optional.of(mockedVideo));
        when(metadataRepository.save(any(Metadata.class))).thenReturn(mockedMetadata);

        Metadata updatedMetadata = metadataServiceImp.update("someId", mockedMetadata);
        assertNotNull(updatedMetadata);
        assertEquals("Sample Title", updatedMetadata.getTitle());
    }


    @Test
    public void testGetMetadataForVideo() {
        when(metadataRepository.findByVideoId(anyString())).thenReturn(Optional.of(mockedMetadata));

        Metadata result = metadataServiceImp.getByVideoId("someId");
        assertNotNull(result);
        assertEquals("Sample Title", result.getTitle());
    }

    @Test
    public void testDelistMetadataForVideo() {
        when(videoRepository.findById(anyString())).thenReturn(Optional.of(mockedVideo));
        when(metadataRepository.findByVideoId(anyString())).thenReturn(Optional.of(mockedMetadata));
        when(metadataRepository.save(any(Metadata.class))).thenReturn(mockedMetadata);

        Optional<Metadata> result = metadataServiceImp.delist("someId");
        assertTrue(result.isPresent());
        assertTrue(result.get().isDelisted());
        verify(metadataRepository, times(1)).findByVideoId("someId");
    }

    @Test
    public void testDelistMetadataForNonExistentVideo() {
        when(metadataRepository.findByVideoId(anyString())).thenReturn(Optional.empty());

        Optional<Metadata> result = metadataServiceImp.delist("someId");
        assertTrue(result.isEmpty());
        verify(metadataRepository, times(1)).findByVideoId("someId");
    }

    @Test
    public void testDeleteMetadataForVideo() {
        when(videoRepository.findById(anyString())).thenReturn(Optional.of(mockedVideo));
        when(metadataRepository.findByVideoId(anyString())).thenReturn(Optional.of(mockedMetadata));
        doNothing().when(metadataRepository).deleteById(anyString());

        Optional<Metadata> deletedMetadata = metadataServiceImp.deleteByVideoId("someId");

        assertTrue(deletedMetadata.isPresent());
        assertEquals(mockedMetadata, deletedMetadata.get());
        verify(metadataRepository, times(1)).deleteById(mockedMetadata.getId());
    }


    @Test
    public void testDeleteMetadataForNonExistentVideo() {
        when(metadataRepository.findByVideoId(anyString())).thenReturn(Optional.empty());

        Optional<Metadata> result = metadataServiceImp.deleteByVideoId("someId");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSave() {
        when(videoRepository.findById(anyString())).thenReturn(Optional.of(mockedVideo));
        Mockito.when(metadataRepository.save(mockedMetadata)).thenReturn(mockedMetadata);

        Metadata result = metadataServiceImp.save(mockedMetadata);

        assertNotNull(result);
        assertEquals(mockedMetadata, result);

    }

    @Test
    public void testAddMetadataForVideoWithExistingMetadata() {
        when(metadataRepository.findByVideoId(anyString())).thenReturn(Optional.of(mockedMetadata));

        assertThrows(EntityExistsException.class, () -> metadataServiceImp.add("someId", mockedMetadata));
    }

    @Test
    public void testAddMetadataForNonExistentVideo() {
        when(metadataRepository.findByVideoId(anyString())).thenReturn(Optional.empty());
        when(videoRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> metadataServiceImp.add("someId", mockedMetadata));
    }
    @Test
    public void testGetMetadataForNonExistentVideo() {
        when(metadataRepository.findByVideoId(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> metadataServiceImp.getByVideoId("someId"));
    }

    @Test
    public void testUpdateMetadataForVideoWithoutMetadata() {
        when(metadataRepository.findByVideoId(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> metadataServiceImp.update("someId", mockedMetadata));
    }

}
