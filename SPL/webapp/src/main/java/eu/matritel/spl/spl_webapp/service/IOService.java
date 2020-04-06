package eu.matritel.spl.spl_webapp.service;

public interface IOService {
    public String getSaveLocation();
    void saveFile(String fileName, String imgAsString);
}
