package com.vj.tain.videostream.services.api;

import com.vj.tain.videostream.bom.Metadata;

import java.util.Optional;

public interface MetadataService {
    public Metadata add(String vId, Metadata metadata);

    public Metadata update(String vId, Metadata metadata);

    public  Optional<Metadata>  delist(String vId);

    Optional<Metadata> getOptionalByVideoId(String vId);

}
