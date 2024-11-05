package com.gigilife.game.fileloader;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Component
public class FileLoader {

    private ResourceLoader resourceLoader;

    public FileLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String readKeyFromFile(String filePath) throws IOException {

        Resource resource = resourceLoader.getResource(filePath);

        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }
}
