package eu.matritel.spl.spl_webapp.controller;

import eu.matritel.spl.spl_webapp.service.IOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/attachments")
public class AttachmentController {

    @Autowired
    IOService IOService;

    @GetMapping("/checklist_snapshot/{id}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String id, final HttpServletResponse response) {
        response.setContentType("image/png");
        Resource file = loadFileAsResource(id);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                "")
                .body(file);
    }

    public Resource loadFileAsResource(String fileName) {
        try {

            Path filePath = Paths.get(IOService.getSaveLocation()).toAbsolutePath().normalize().resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            System.out.println("Resource" + resource);
            if(resource.exists()) {
                return resource;
            } else {
                throw new MalformedURLException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
