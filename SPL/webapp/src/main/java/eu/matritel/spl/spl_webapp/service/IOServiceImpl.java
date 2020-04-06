package eu.matritel.spl.spl_webapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;


@Service
@Component
public class IOServiceImpl implements IOService {
    @Value("${fileUpload.path}")
    private String saveLocation;

    @Override
    public void saveFile(String fileName, String imgAsString) {
        Character whattoremove = '"';

        imgAsString = imgAsString.replaceAll(whattoremove.toString(), "");
        String home = saveLocation + "/";
        try (FileOutputStream imageOutFile = new FileOutputStream(home + fileName)) {
            // Converting a Base64 String into Image byte array
            byte[] imageByteArray = Base64.getMimeDecoder().decode(imgAsString);
            imageOutFile.write(imageByteArray);
        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        }
    }

    public String getSaveLocation() {
        return saveLocation;
    }
}