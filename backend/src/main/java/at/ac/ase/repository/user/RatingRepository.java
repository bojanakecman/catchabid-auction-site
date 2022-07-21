package at.ac.ase.repository.user;

import at.ac.ase.entities.Rating;
import at.ac.ase.entities.RatingPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingPK> {

}
