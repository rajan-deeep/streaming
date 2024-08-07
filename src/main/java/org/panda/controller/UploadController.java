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
                                   Model model) throws IOException {
        // Save the uploaded file
        String originalFilename = file.getOriginalFilename();
        String folderName = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        Path filePath = Paths.get(uploadDir + File.separator + folderName + File.separator + originalFilename);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        // Convert the uploaded video to HLS
        String inputFilePath = filePath.toString();
        String outputFolderPath = filePath.getParent().toString();

        try {
            VideoConversionUtil.convertToHLS(inputFilePath, outputFolderPath);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Video conversion failed: " + e.getMessage());
            return "uploadstatus";
        }

        // Add attributes for Thymeleaf
        model.addAttribute("filename", originalFilename);
        model.addAttribute("folder", folderName);
        model.addAttribute("filesize", file.getSize());
        model.addAttribute("defaultQuality", "low"); // You can dynamically set this based on some logic

        return "uploadstatus";
    }

}
