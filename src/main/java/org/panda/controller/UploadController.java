package org.panda.controller;

import org.panda.util.VideoConversionUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UploadController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("bitrate") String bitrate,
                                   Model model) throws IOException {
        // Save the uploaded file
        String originalFilename = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + File.separator + originalFilename);
        Files.write(filePath, file.getBytes());

        // Use the filename (without extension) as the folder name
        String folderName = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        String outputFolderPath = uploadDir + File.separator + folderName;

        // Convert the uploaded video to HLS
        String inputFilePath = filePath.toString();

        try {
            VideoConversionUtil.convertToHLS(inputFilePath, outputFolderPath);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Video conversion failed: " + e.getMessage());
            return "uploadstatus";
        }

        // Add attributes for Thymeleaf
        model.addAttribute("filename", originalFilename);
        model.addAttribute("bitrate", bitrate);
        model.addAttribute("folder", folderName);
        model.addAttribute("filesize", file.getSize());
        model.addAttribute("defaultQuality", "low"); // You can dynamically set this based on some logic

        return "uploadstatus";
    }

}
