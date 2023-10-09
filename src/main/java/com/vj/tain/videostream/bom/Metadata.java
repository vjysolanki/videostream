package com.vj.tain.videostream.bom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vj.tain.videostream.utils.ListToJsonConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Metadata {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;
    @NotBlank(message = "Video ID cannot be empty!")
    private String videoId;

    private String title;
    private String synopsis;
    private String director;
    @Convert(converter = ListToJsonConverter.class)
    private List<String> crew;
    private int yearOfRelease;
    private String genre;
    private int runningTime;
    private String format;
    private boolean delisted; // for soft DELETE

    // Optimistic lock
    @JsonIgnore
    @Version
    private int version;
}
