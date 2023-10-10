package com.vj.tain.videostream.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vj.tain.videostream.bom.Metadata;
import com.vj.tain.videostream.bom.Video;
import com.vj.tain.videostream.dto.EngagementDTO;
import com.vj.tain.videostream.dto.RawVideoDTO;
import com.vj.tain.videostream.dto.VideoDetailsDTO;
import com.vj.tain.videostream.dto.VideoMetadataDTO;
import com.vj.tain.videostream.services.imp.VideoServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VideoController.class)
class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VideoServiceImp videoService;

    private Metadata mockedMetadata;
    private Video mockedVideo;

    private RawVideoDTO rawVideoDTO;
    final private  String base64VideoContent = "SGVsbG8sIHdvcmxkIQ==";

    @BeforeEach
    public void setup() {
        rawVideoDTO = new RawVideoDTO(base64VideoContent);
        byte[] videoBytes = Base64.getDecoder().decode(rawVideoDTO.getBase64RawContents());


        mockedVideo = Video.builder()
                .id(UUID.randomUUID().toString())
                .base64RawContents(videoBytes)
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
    public void testPublishVideo() throws Exception {
        Mockito.when(videoService.publishRaw(any(RawVideoDTO.class))).thenReturn(mockedVideo);

        mockMvc.perform(post("/videos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(mockedVideo)))
                .andExpect(status().isCreated())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(mockedVideo)));
        verify(videoService).publishRaw(rawVideoDTO);
    }

    @Test
    public void testDelistVideo() throws Exception {
        String videoId = mockedVideo.getId();
        Mockito.when(videoService.delist(videoId)).thenReturn(mockedVideo);

        mockMvc.perform(delete("/videos/" + videoId)
                        .param("soft", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(mockedVideo)));
        verify(videoService).delist(videoId);
    }


    @Test
    public void testLoadVideoById() throws Exception {
        String videoId = mockedVideo.getId();
        VideoDetailsDTO videoDetails = new VideoDetailsDTO();
        Mockito.when(videoService.load(videoId)).thenReturn(videoDetails);

        mockMvc.perform(get("/videos/" + videoId + "/load")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(videoDetails)));
        verify(videoService).load(videoId);
    }

    @Test
    public void testPlayVideo() throws Exception {
        String videoId = mockedVideo.getId();
        String videoUrl = "http://samplevideourl.com" + "/"+videoId;
        Mockito.when(videoService.play(videoId)).thenReturn(videoUrl);

        mockMvc.perform(get("/videos/" + videoId + "/play")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(videoUrl));
        verify(videoService).play(videoId);
    }

    @Test
    public void testListAllVideos() throws Exception {
        List<VideoMetadataDTO> videoMetadataList = List.of(new VideoMetadataDTO());
        Mockito.when(videoService.listAllVideosWithPartialMetadata()).thenReturn(videoMetadataList);

        mockMvc.perform(get("/videos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(videoMetadataList)));
        verify(videoService).listAllVideosWithPartialMetadata();
    }

    @Test
    public void testListAllVideosWithFullDetails() throws Exception {
        List<Video> videoList = List.of(mockedVideo);
        Mockito.when(videoService.listAll()).thenReturn(videoList);

        mockMvc.perform(get("/videos/only")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(videoList)));
        verify(videoService).listAll();
    }

    @Test
    public void testGetVideoEngagement() throws Exception {
        String videoId = mockedVideo.getId();
        EngagementDTO engagement = new EngagementDTO();
        Mockito.when(videoService.getEngagementStats(videoId)).thenReturn(engagement);

        mockMvc.perform(get("/videos/" + videoId + "/engagement")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(engagement)));
        verify(videoService).getEngagementStats(videoId);
    }

    @Test
    public void testSearchVideos() throws Exception {
        List<VideoMetadataDTO> videos = List.of(new VideoMetadataDTO());
        Mockito.when(videoService.searchVideos(any(), any(), any())).thenReturn(videos);

        mockMvc.perform(get("/videos/search")
                        .param("director", "sampleDirector")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(videos)));
        verify(videoService).searchVideos(any(), any(), any());
    }

    @Test
    public void testLoadVideoByIdWithInvalidId() throws Exception {
        String invalidVideoId = "  ";

        mockMvc.perform(get("/videos/" + invalidVideoId + "/load")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDelistVideoWithInvalidId() throws Exception {
        String invalidVideoId = "  ";

        mockMvc.perform(delete("/videos/" + invalidVideoId)
                        .param("soft", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoadVideoById_NotFound() throws Exception {
        String videoId = mockedVideo.getId();
        Mockito.when(videoService.load(videoId))
                .thenThrow(new EntityNotFoundException("Video not found for ID: " + videoId));

        mockMvc.perform(get("/videos/" + videoId + "/load")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Video not found for ID: " + videoId, result.getResolvedException().getMessage()));

        verify(videoService).load(videoId);
    }

    @Test
    public void testPublishVideo_AlreadyExists() throws Exception {

        Mockito.when(videoService.publishRaw(rawVideoDTO))
                .thenThrow(new EntityExistsException("Video with the same content already exists."));

        mockMvc.perform(post("/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockedVideo)))
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityExistsException))
                .andExpect(result -> assertEquals("Video with the same content already exists.", result.getResolvedException().getMessage()));
        verify(videoService).publishRaw(rawVideoDTO);
    }

    @Test
    public void testPublishEmptyBase64Video() {
        RawVideoDTO rawVideoDTO = new RawVideoDTO("");
        Mockito.when(videoService.publishRaw(rawVideoDTO)).thenCallRealMethod();
        assertThrows(IllegalArgumentException.class, () -> videoService.publishRaw(rawVideoDTO));
    }


    @Test
    public void testPublishNullBase64Video() {
        RawVideoDTO rawVideoDTO = new RawVideoDTO(null);
        Mockito.when(videoService.publishRaw(rawVideoDTO)).thenCallRealMethod();
        assertThrows(IllegalArgumentException.class, () -> videoService.publishRaw(rawVideoDTO));
    }
}
