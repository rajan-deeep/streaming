package org.panda.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@Slf4j
public class StreamController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/hls/{folder}/master.m3u8")
    public ResponseEntity<Resource> getMasterPlaylist(@PathVariable String folder) {
        try {
            Path filePath = Paths.get(uploadDir + File.separator + folder + File.separator + "master.m3u8");
            Resource resource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok().body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/hls/{folder}/{quality}/index.m3u8")
    public ResponseEntity<Resource> getQualityPlaylist(@PathVariable String folder, @PathVariable String quality) {
        try {
            Path filePath = Paths.get(uploadDir + File.separator + folder + File.separator + quality + File.separator + "index.m3u8");
            Resource resource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok().body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/hls/{folder}/{quality}/{file:.+}")
    public ResponseEntity<Resource> getMediaSegment(@PathVariable String folder, @PathVariable String quality, @PathVariable String file) {
        try {
            log.info("this folder {},this quality {},this segment {} retrieving", folder, quality, file);
            Path filePath = Paths.get(uploadDir + File.separator + folder + File.separator + quality + File.separator + file);
            Resource resource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok().body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
