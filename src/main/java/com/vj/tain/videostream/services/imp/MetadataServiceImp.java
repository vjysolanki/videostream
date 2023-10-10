package com.vj.tain.videostream.services.imp;

import com.vj.tain.videostream.bom.Metadata;
import com.vj.tain.videostream.repository.MetadataRepository;
import com.vj.tain.videostream.repository.VideoRepository;
import com.vj.tain.videostream.services.api.MetadataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@Slf4j
public class MetadataServiceImp implements MetadataService {

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Override
    public Metadata add(String vId, Metadata metadata) {
        if (metadata.isEmpty()) {
            throw new IllegalArgumentException("Metadata cannot be empty!");
        }
        //avoid multiple metadata for same video id
        if (metadataRepository.findByVideoId(vId).isPresent()) {
            log.error("META-SERVICE - Metadata already exists for this videoId " + vId);
            throw new EntityExistsException("[ERROR] - Metadata already exists for this videoId " + vId);
        }
        metadata.setVideoId(vId);
        return save(metadata);
    }

    @Override
    public Metadata update(String vId, Metadata newMetadata) {

        if (newMetadata.isEmpty()) {
            throw new IllegalArgumentException("Metadata cannot be empty!");
        }
        Metadata existingMetadata = getByVideoId(vId);

        newMetadata.setId(existingMetadata.getId());
        newMetadata.setVideoId(existingMetadata.getVideoId());
        newMetadata.setVersion(existingMetadata.getVersion());

        return save(newMetadata);
    }


    private Metadata getById(String mId) {
        return metadataRepository.findById(mId)
                .orElseThrow(() -> new EntityNotFoundException("[ERROR] - Metadata you are trying to access doesn't exist!!"));
    }

    @Override
    public Optional<Metadata> getOptionalByVideoId(String vId) {
        return metadataRepository.findByVideoId(vId);
    }

    public Metadata getByVideoId(String vId) {
        return metadataRepository.findByVideoId(vId)
                .orElseThrow(() -> new EntityNotFoundException("[ERROR] - Metadata you are trying to access for videoId " + vId + " doesn't exist!!"));
    }

    @Override
    public Optional<Metadata> delist(String vId) {

        Optional<Metadata> existingMetadata = getOptionalByVideoId(vId);
        if (existingMetadata.isEmpty()) {
            log.warn("META-SERVICE - trying to delete(soft) metadata for non existing video " + vId);
            return Optional.empty();
        }
        Metadata metadata = existingMetadata.get();
        metadata.setDelisted(true);
        return Optional.of(save(metadata));
    }

    @Transactional
    public Optional<Metadata> deleteByVideoId(String vId) {
        Optional<Metadata> existingMetadata = getOptionalByVideoId(vId);
        if (existingMetadata.isEmpty()) {
            log.warn("META-SERVICE - trying to delete(hard) metadata for non existing video " + vId);
            return Optional.empty();
        }
        Metadata metadata = existingMetadata.get();
        metadataRepository.deleteById(metadata.getId());
        return existingMetadata;
    }

    @Transactional
    @Override
    public Metadata save(Metadata metadata) {
        // Ensuring that the videoId references an existing Video
        if (videoRepository.findById(metadata.getVideoId()).isEmpty()) {
            log.warn("META-SERVICE - The provided videoId " + metadata.getVideoId() + " does not reference a valid video.");
            throw new EntityNotFoundException("[ERROR] - The provided videoId " + metadata.getVideoId() + " does not reference a valid video.");
        }
        return metadataRepository.save(metadata);
    }

/* private Metadata deltaUpdate(String vId, Metadata newMetadata) {
        Metadata existingMetadata = getByVideoId(vId);

        existingMetadata.setTitle(newMetadata.getTitle());
        existingMetadata.setSynopsis(newMetadata.getSynopsis());
        existingMetadata.setDirector(newMetadata.getDirector());
        existingMetadata.setCrew(newMetadata.getCrew());
        existingMetadata.setYearOfRelease(newMetadata.getYearOfRelease());
        existingMetadata.setGenre(newMetadata.getGenre());
        existingMetadata.setRunningTime(newMetadata.getRunningTime());
        existingMetadata.setFormat(newMetadata.getFormat());

        return save(existingMetadata);
    }
    private <T> T updateIfNull(T newValue, T currentValue) {
     return newValue == null ? currentValue : newValue;
    }
    */
}
