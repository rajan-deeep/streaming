package org.panda.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class VideoListController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/videos")
    public String listVideos(Model model) throws IOException {
        File directory = new File(uploadDir);

        // Check if directory exists and is not empty
        if (!directory.exists() || !directory.isDirectory()) {
            model.addAttribute("message", "No videos found. Please upload a video.");
            return "video-list";
        }

        List<Video> videoList = Arrays.stream(directory.listFiles())
                .filter(File::isDirectory) // Look for directories
                .flatMap(folder -> Arrays.stream(folder.listFiles())
                        .filter(File::isFile) // Ensure we are listing files
                        .map(file -> new Video(folder.getName(), file.getName())))
                .collect(Collectors.toList());

        if (videoList.isEmpty()) {
            model.addAttribute("message", "No videos found. Please upload a video.");
        } else {
            model.addAttribute("videos", videoList);
        }

        for (Video each : videoList) {
            log.info(each.getFilename() + "," + each.getFolder());
        }

        return "video-list";
    }

    static class Video {
        private final String folder;
        private final String filename;

        public Video(String folder, String filename) {
            this.folder = folder;
            this.filename = filename;
        }

        public String getFolder() {
            return folder;
        }

        public String getFilename() {
            return filename;
        }

        public String getName() {
            return filename; // or provide a more user-friendly name
        }
    }
}
