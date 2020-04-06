package eu.matritel.spl.spl_webapp.service;

import eu.matritel.spl.spl_webapp.entity.Site;

import java.util.List;
import java.util.Optional;

public interface SiteService {

    Site create(Site site);

    Optional<Site> findById(int id);

    List<Site> getAllSites();
}
