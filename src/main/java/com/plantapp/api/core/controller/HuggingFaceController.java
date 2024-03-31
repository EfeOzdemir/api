package com.plantapp.api.core.controller;

import com.plantapp.api.core.service.HuggingFaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class HuggingFaceController {

    @Autowired
    private HuggingFaceService service;

    @PostMapping("/send")
    public String send(MultipartFile file) throws IOException {
        return service.query(file);
    }
}