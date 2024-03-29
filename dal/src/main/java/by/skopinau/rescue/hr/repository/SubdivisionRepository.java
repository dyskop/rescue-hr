package by.skopinau.rescue.hr.repository;

import by.skopinau.rescue.hr.entity.Subdivision;
import org.springframework.stereotype.Repository;

@Repository
public interface SubdivisionRepository extends BaseRepository<Subdivision> {
    Subdivision findBySubdivisionTitle(String subdivisionTitle);
}