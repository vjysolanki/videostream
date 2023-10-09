package com.vj.tain.videostream.services.imp;

import com.vj.tain.videostream.bom.Metadata;
import com.vj.tain.videostream.repository.MetadataRepository;
import com.vj.tain.videostream.repository.VideoRepository;
import com.vj.tain.videostream.services.api.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class MetadataServiceImp implements MetadataService {

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Override
    public Metadata add(String vId, Metadata metadata) {
        metadata.setVideoId(vId);
        return save(metadata);
    }

    @Override
    public Metadata update(String vId, Metadata newMetadata) {

        Metadata existingMetadata = getByVideoId(vId);

        newMetadata.setId(existingMetadata.getId());
        newMetadata.setVideoId(existingMetadata.getVideoId());

        return save(newMetadata);
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
    }*/

    private Metadata getById(String mId) {
        return metadataRepository.findById(mId)
                .orElseThrow(() -> new EntityNotFoundException("[ERROR] - Metadata you are trying to access doesn't exist!!"));
    }

    public Metadata getByVideoId(String vId) {
        return metadataRepository.findByVideoId(vId)
                .orElseThrow(() -> new EntityNotFoundException("[ERROR] - Metadata you are trying to access for videoId " + vId + " doesn't exist!!"));
    }

    @Override
    public Optional<Metadata> delist(String vId) {

        Optional<Metadata> existingMetadata = metadataRepository.findByVideoId(vId);
        if (existingMetadata.isEmpty()) {
            return Optional.empty();
        }
        Metadata metadata = existingMetadata.get();
        metadata.setDelisted(true);
        return Optional.of(save(metadata));
    }

    @Transactional
    private Optional<Metadata> deleteByVideoId(String vId) {
        Optional<Metadata> existingMetadata = metadataRepository.findByVideoId(vId);
        if (existingMetadata.isEmpty()) {
            return Optional.empty();
        }
        Metadata metadata = existingMetadata.get();
        metadataRepository.deleteById(metadata.getId());
        return Optional.of(save(metadata));
    }

    @Transactional
    private Metadata save(Metadata metadata) {
        // Ensuring that the videoId references an existing Video
        if (videoRepository.findById(metadata.getVideoId()).isEmpty()) {
            throw new EntityNotFoundException("[ERROR] - The provided videoId " + metadata.getVideoId() + " does not reference a valid video.");
        }
        return metadataRepository.save(metadata);
    }

    private <T> T updateIfNull(T newValue, T currentValue) {
        return newValue == null ? currentValue : newValue;
    }

}
