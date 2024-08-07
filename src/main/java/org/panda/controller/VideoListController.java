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
        List<Video> videoList = Arrays.stream(directory.listFiles())
                .filter(File::isDirectory)
                .flatMap(folder -> Arrays.stream(folder.listFiles())
                        .filter(file -> file.getName().endsWith(".mp4")) // Adjust file extension if needed
                        .map(file -> new Video(folder.getName(), file.getName())))
                .collect(Collectors.toList());

        model.addAttribute("videos", videoList);

        for(Video each: videoList){
            log.info(each.getFilename()+","+each.folder);
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
            return folder; // or provide a more user-friendly name
        }
    }

}
