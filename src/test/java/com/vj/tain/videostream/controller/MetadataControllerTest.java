package com.vj.tain.videostream.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vj.tain.videostream.bom.Metadata;
import com.vj.tain.videostream.bom.Video;
import com.vj.tain.videostream.services.api.MetadataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MetadataController.class)
public class MetadataControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    public void testAddMetadata() throws Exception {
        Mockito.when(metadataService.add(any(String.class), any(Metadata.class))).thenReturn(mockedMetadata);

        mockMvc.perform(post("/videos/" + mockedMetadata.getVideoId() + "/metadata")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockedMetadata)))
                .andExpect(status().isCreated())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(mockedMetadata)));
    }

    @Test
    public void testAddMetadataInvalidVideoId() throws Exception {
        Metadata invalidMetadata = new Metadata();
        String invalidVideoId = "  ";

        mockMvc.perform(post("/videos/{videoId}/metadata", invalidVideoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidMetadata)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateMetadata() throws Exception {
        Mockito.when(metadataService.update(any(String.class), any(Metadata.class))).thenReturn(mockedMetadata);

        mockMvc.perform(put("/videos/" + mockedMetadata.getVideoId() + "/metadata")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockedMetadata)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(mockedMetadata)));
    }


    @Test
    public void testUpdateMetadataInvalidVideoId() throws Exception {
        Metadata invalidMetadata = new Metadata();
        String invalidVideoId = "  ";

        mockMvc.perform(put("/videos/{videoId}/metadata", invalidVideoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidMetadata)))
                .andExpect(status().isBadRequest());
    }

}
