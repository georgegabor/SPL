package eu.matritel.spl.spl_webapp.service;

import eu.matritel.spl.spl_webapp.entity.Site;
import eu.matritel.spl.spl_webapp.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    private SiteRepository siteRepository;


    @Override
    public Site create(Site site) {
        return siteRepository.save(site);
    }

    @Override
    public Optional<Site> findById(int id) {
        return siteRepository.findById(id);
    }

    @Override
    public List<Site> getAllSites() {
        return siteRepository.findAll();
    }
}
