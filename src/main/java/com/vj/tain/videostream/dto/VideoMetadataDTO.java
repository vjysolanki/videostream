package com.vj.tain.videostream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoMetadataDTO {
    private String title;
    private String director;
    private List<String> crew;
    private String genre;
    private int runningTime;
}
