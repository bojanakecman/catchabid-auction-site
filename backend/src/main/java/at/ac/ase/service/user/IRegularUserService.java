package at.ac.ase.service.user;

import at.ac.ase.dto.RegularUserDTO;
import at.ac.ase.entities.RegularUser;

import java.util.List;
import java.util.Optional;

public interface IRegularUserService {

    /**
     * Method which retrieves all users from the database
     * @return list of regular users
     */
    List<RegularUser> getAllUsers();

    /**
     * Method which retrieves a single user based on his/her email
     * @param email of the user to be retrieved
     * @return regular user if found
     */
    RegularUser getUserByEmail(String email);

    /**
     * Method which updates a user's password
     * @param email an email address of user
     * @param password a new password
     */
    void changePassword(String email, String password);

    /**
     * Method which retrieves a user based on its id
     * @param id of an auction house
     * @return auction house if found
     */
    Optional<RegularUser> getUserById(Long id);

    /**
     * Method which updates user based on a given object
     * @param email original email - in case email in user is changed
     * @param user updated user object
     * @return updated RegularUser
     */
    RegularUser updateUser(String email, RegularUserDTO user);
}
