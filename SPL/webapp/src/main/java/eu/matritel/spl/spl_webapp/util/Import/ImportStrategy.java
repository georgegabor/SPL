package eu.matritel.spl.spl_webapp.util.Import;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface ImportStrategy {
    public String[][] parseFile(MultipartFile file);
}
