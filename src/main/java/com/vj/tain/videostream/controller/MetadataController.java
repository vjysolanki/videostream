package com.vj.tain.videostream.controller;

import com.vj.tain.videostream.bom.Metadata;
import com.vj.tain.videostream.services.api.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/videos/{videoId}/metadata")
public class MetadataController {

    @Autowired
    private MetadataService metadataService;

    @PostMapping
    public ResponseEntity<Metadata> addMetadata(@PathVariable String videoId,
                                                @Valid @RequestBody Metadata metadata) {
        Metadata savedMetadata = metadataService.add(videoId,metadata);
        return new ResponseEntity<>(savedMetadata, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Metadata> updateMetadata(@PathVariable String videoId,
                                                   @Valid @RequestBody Metadata metadata) {
        Metadata updatedMetadata = metadataService.update(videoId, metadata);
        return new ResponseEntity<>(updatedMetadata, HttpStatus.OK);
    }

}
