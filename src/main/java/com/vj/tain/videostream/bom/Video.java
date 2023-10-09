package com.vj.tain.videostream.bom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "video")
@Builder(toBuilder = true)
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Video {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @NotBlank(message = "Video contents cannot be empty!")
    private String content;  // mock video content (string)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private boolean delisted; // for soft delete
    // Analytics
    @JsonIgnore
    private int impressions;
    @JsonIgnore
    private int views;
    // Optimistic lock
    @JsonIgnore
    @Version
    private Long version;
}
