package at.ac.ase.service.auth;

import at.ac.ase.entities.AuctionHouse;
import at.ac.ase.entities.RegularUser;
import at.ac.ase.entities.User;
import net.minidev.json.JSONObject;

import java.util.Map;

public interface IAuthService {

    /**
     * Method which authenticates an user or an auction house based on a sent email and password
     * @param userData map containing user data - email and password
     * @return newly generated token
     */
    public JSONObject authenticate(Map<String,String> userData);

    /**
     * Method which send a password reset token to user's email address
     * @param user user requested a password reset token
     */
    public void sendPasswordResetToken(User user);
}
