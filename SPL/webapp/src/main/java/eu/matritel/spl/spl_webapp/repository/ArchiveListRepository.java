package eu.matritel.spl.spl_webapp.repository;

import eu.matritel.spl.spl_webapp.entity.ArchiveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveListRepository extends JpaRepository<ArchiveEntity , Object> {

/*
*   @Query(value="select * from Users u where u.EMAIL_VERIFICATION_STATUS = 'true'", nativeQuery = true)
  Page<UserEntity> findAllUsersWithConfirmedEmailAddress( Pageable pageableRequest );
* */

   // @Query(value="SELECT * FROM archivelist where session_id_id=?1")
    ArchiveEntity findTopBySessionIdIdOrderByIdDesc(int sessionIdId);


}
