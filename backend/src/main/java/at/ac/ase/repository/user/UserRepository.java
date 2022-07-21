package at.ac.ase.repository.user;

import at.ac.ase.entities.RegularUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<RegularUser, Long> {

    /**
     * Method which retrieves a single user based on his/her email
     * @param email of the user to be retrieved
     * @return regular user if found
     */
    RegularUser findByEmail(String email);


    /**
     * Method which updates a user's password
     * @param email an email address of user
     * @param passwordHash a hashed new password
     */
    @Modifying
    @Query("UPDATE RegularUser u SET u.passwordHash = :passwordHash WHERE u.email = :email")
    void changePassword(@Param(value = "email")String email,@Param(value = "passwordHash")String passwordHash);
}
