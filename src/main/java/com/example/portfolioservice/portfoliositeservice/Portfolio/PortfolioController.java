package com.example.portfolioservice.portfoliositeservice.Portfolio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin
public class PortfolioController {

    @Autowired
    private IPAddressFinder ipAddressFinder;

    @GetMapping
    public ResponseEntity<Resource> download(HttpServletRequest request) throws IOException {

        String clientIp = ipAddressFinder.getClientIp(request);
        System.out.println("clientIp: " + clientIp);

        ClassPathResource res = new ClassPathResource("src/main/resources/resume-navjot-singh.pdf");
        File file = new File(res.getPath());

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resume-navjot-singh.pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        header.add("IP Address", clientIp);

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}
