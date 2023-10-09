package com.vj.tain.videostream.dto;

import com.vj.tain.videostream.bom.Metadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoDetailsDTO {
    private String id;
    private String content; // from Video
    private boolean delisted;
    private Metadata metadata; // entire metadata entity

    // getters and setters
}
