package com.vj.tain.videostream.bom;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

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
    private String id;

    @Lob
    private String content;  // mock video content (string)

    //META-DATA
    private String title;
    private String synopsis;
    private String director;
    @Convert(converter = ListToJsonConverter.class)
    private List<String> crew;
    private int yearOfRelease;
    private String genre;
    private int runningTime;
    private String format;
    private boolean delisted; // for soft delete

    // Analytics
    private int impressions;
    private int views;

    // Optimistic lock
    @Version
    private Long version;
}
