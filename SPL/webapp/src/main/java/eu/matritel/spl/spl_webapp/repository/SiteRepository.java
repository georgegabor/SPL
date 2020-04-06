package eu.matritel.spl.spl_webapp.repository;

import eu.matritel.spl.spl_webapp.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends JpaRepository<Site, Integer> {
}
