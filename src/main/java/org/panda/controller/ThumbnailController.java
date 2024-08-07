package org.panda.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ThumbnailController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/thumbnails/{folder}/thumbnail.png")
    public ResponseEntity<Resource> getThumbnail(@PathVariable String folder) {
        try {
            Path filePath = Paths.get(uploadDir + File.separator + folder + File.separator + "thumbnails" + File.separator + "thumbnail.png");
            Resource resource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok().body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
