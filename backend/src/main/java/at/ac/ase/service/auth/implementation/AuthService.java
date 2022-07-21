package at.ac.ase.service.auth.implementation;

import at.ac.ase.entities.AuctionHouse;
import at.ac.ase.entities.PasswordResetToken;
import at.ac.ase.entities.RegularUser;
import at.ac.ase.entities.User;
import at.ac.ase.repository.token.PasswordTokenRepository;
import at.ac.ase.repository.user.AuctionHouseRepository;
import at.ac.ase.repository.user.UserRepository;
import at.ac.ase.service.auth.IAuthService;
import at.ac.ase.util.TokenUtil;
import at.ac.ase.util.exceptions.AuthorizationException;
import at.ac.ase.util.exceptions.EmailNotSentException;
import at.ac.ase.util.exceptions.TokenGenerationException;
import at.ac.ase.util.exceptions.UserNotFoundException;
import com.nimbusds.jose.JOSEException;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;

/**
 * Authentication service that manages authentication requests from {@link at.ac.ase.controllers.AuthController}
 */
@Service
public class AuthService implements IAuthService {

    @Autowired
    AuctionHouseRepository auctionHouseRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    PasswordTokenRepository passwordTokenRepository;
    @Autowired
    JavaMailSender emailSender;

    @Override
    public JSONObject authenticate(Map<String,String> userData) {
        String email = userData.get("email");
        String password = userData.get("password");
        AuctionHouse house = auctionHouseRepository.findByEmail(email);
        RegularUser user = userRepository.findByEmail(email);
        if(house != null) {
            if(passwordEncoder.matches(password, house.getPasswordHash())){
                try{
                    JSONObject token = new JSONObject();
                    token.appendField("token",TokenUtil.generateToken(email, password));
                    return token;
                } catch (JOSEException e){
                    throw new TokenGenerationException();
                }
            } else {
                throw new AuthorizationException();
            }
        }
        if(user != null) {
            if (passwordEncoder.matches(password, user.getPasswordHash())) {
                try {
                    JSONObject token = new JSONObject();
                    token.appendField("token", TokenUtil.generateToken(email, password));
                    return token;
                } catch (JOSEException e) {
                    throw new TokenGenerationException();
                }
            } else {
                throw new AuthorizationException();
            }
        }
        throw new UserNotFoundException();
    }

    @Override
    @Transactional
    public void sendPasswordResetToken(User user) {
        PasswordResetToken token;
        long resetToken = (long) Math.abs(1000000000000000L + new SecureRandom().nextFloat() * 9000000000000000L);
        if(user instanceof RegularUser) {
            RegularUser regularUser = (RegularUser) user;
            token = new PasswordResetToken(resetToken, regularUser, null);
            passwordTokenRepository.deleteTokenByUserId(regularUser, null);
        }else{
            AuctionHouse auctionHouse = (AuctionHouse)user;
            token = new PasswordResetToken(resetToken, null, auctionHouse);
            passwordTokenRepository.deleteTokenByUserId(null, auctionHouse);
        }
        passwordTokenRepository.save(token);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply.catchabid@gmail.com");
            message.setTo(user.getEmail());
            message.setSubject("Reset Password Token");
            message.setText("Your reset password token is " + resetToken);
            emailSender.send(message);
        }catch (MailException e){
            throw new EmailNotSentException();
        }
    }


}
