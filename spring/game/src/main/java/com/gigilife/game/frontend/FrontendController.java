package com.gigilife.game.frontend;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/*")
public class FrontendController {

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getFrontend() throws IOException {

        Resource resource = new ClassPathResource("static/index.html");
        Path indexPath = Paths.get(resource.getURI());
        String htmlContent = Files.readString(indexPath, StandardCharsets.UTF_8);
        return ResponseEntity.ok(htmlContent);
    }
    
    
}
