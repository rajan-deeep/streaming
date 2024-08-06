package org.panda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UploadPageController {

    @GetMapping("/upload")
    public String showUploadForm() {
        return "upload"; // This should match the name of your Thymeleaf template (upload.html)
    }
}
