package at.ac.ase.repository.user;

import at.ac.ase.entities.AuctionHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AuctionHouseRepository extends JpaRepository<AuctionHouse, Long> {
    /**
     * Method which retrieves a single auction house on its email
     * @param email of the auction house to be retrieved
     * @return auction house if found
     */
    AuctionHouse findByEmail(String email);

    /**
     * Method which updates a auction house's password
     * @param email an email address of auction house
     * @param passwordHash a hashed new password
     */
    @Modifying
    @Query("UPDATE AuctionHouse u SET u.passwordHash = :passwordHash WHERE u.email = :email")
    void changePassword(@Param(value = "email")String email,@Param(value = "passwordHash")String passwordHash);

}
