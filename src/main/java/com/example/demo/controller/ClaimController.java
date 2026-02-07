package com.example.demo.controller;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.ClaimService;

import java.util.Map;

@RestController
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam MultipartFile file) throws Exception {
        return claimService.processClaim(file);
    }
}
