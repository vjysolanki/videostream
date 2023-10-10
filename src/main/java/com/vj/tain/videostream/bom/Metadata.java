package com.vj.tain.videostream.bom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vj.tain.videostream.utils.ListToJsonConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "videoId"))
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
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
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
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // modify through api
    private boolean delisted; // for soft DELETE

    // Optimistic lock
    @Schema(hidden = true)
    @JsonIgnore
    @Version
    private int version;

    @Schema(hidden = true)
    @JsonIgnore
    public boolean isEmpty() {
        return null == title && null == synopsis && null == director
                && (null == crew || crew.isEmpty()) && 0 == yearOfRelease
                && null == genre  && 0 == runningTime  && null == format;
    }
}
