package at.ac.ase.controllers;

import at.ac.ase.dto.translator.AuctionHouseDtoTranslator;
import at.ac.ase.dto.translator.UserDtoTranslator;
import at.ac.ase.entities.AuctionHouse;
import at.ac.ase.entities.RegularUser;
import at.ac.ase.entities.*;
import at.ac.ase.service.auth.IAuthService;
import at.ac.ase.service.auth.IRegisterService;
import at.ac.ase.service.passwordtoken.implementation.PasswordTokenService;
import at.ac.ase.service.user.IAuctionHouseService;
import at.ac.ase.service.user.IRegularUserService;
import at.ac.ase.util.exceptions.ResetTokenNotFound;
import at.ac.ase.util.exceptions.UserAlreadyExistsException;
import at.ac.ase.util.exceptions.UserNotFoundException;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Login controller that declares and implements REST methods and forward these requests to {@link at.ac.ase.service.auth.implementation.AuthService}
 */
@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private IAuthService authService;
    @Autowired
    private IRegisterService registerService;
    @Autowired
    private IRegularUserService regularUserService;
    @Autowired
    private IAuctionHouseService auctionHouseService;
    @Autowired
    private PasswordTokenService tokenService;
    @Autowired
    private UserDtoTranslator userDtoTranslator;
    @Autowired
    private AuctionHouseDtoTranslator auctionHouseDtoTranslator;

    @RequestMapping(value = "/registerUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity register(@RequestBody RegularUser regularUser){
        try {
            logger.info("Registering the user " + regularUser.getFirstName() + " " + regularUser.getLastName());
            RegularUser user = registerService.registerUser(regularUser);
            return user != null ? ResponseEntity.status(HttpStatus.OK).body(userDtoTranslator.toRegularUserDTO(user)) : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DataAccessException e){
            throw new UserAlreadyExistsException();
        }
    }

    @RequestMapping(value = "/registerHouse", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody AuctionHouse auctionHouse){
        try {
            logger.info("Registering the house " + auctionHouse.getName());
            AuctionHouse user = registerService.registerHouse(auctionHouse);
            return user != null ? ResponseEntity.status(HttpStatus.OK).body(auctionHouseDtoTranslator.toAuctionHouseDTO(user)) : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DataAccessException e){
            throw new UserAlreadyExistsException();
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@RequestBody Map<String,String> userData){
        logger.info("Authenticating the user with the email " + userData.get("email"));
        JSONObject token = authService.authenticate(userData);
        return token != null ? ResponseEntity.status(HttpStatus.OK).body(token) : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @RequestMapping(value = "/requestPasswordReset", method = RequestMethod.POST)
    public ResponseEntity requestPasswordReset(@RequestBody String email){
        RegularUser user = regularUserService.getUserByEmail(email);
        AuctionHouse auctionHouse = auctionHouseService.getAuctionHouseByEmail(email);
        if(user == null && auctionHouse == null){
            throw new UserNotFoundException();
        }
        if(user != null) {
            authService.sendPasswordResetToken(user);
        }else{
            authService.sendPasswordResetToken(auctionHouse);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = "/sendResetPasswordToken", method = RequestMethod.POST)
    public ResponseEntity sendResetPasswordToken(@RequestBody Map<String, String> tokenData){
        long token = Long.parseLong(tokenData.get("token"));
        String email = tokenData.get("email");
        PasswordResetToken resetToken = tokenService.getPasswordResetTokenByToken(email, token);
        if(resetToken == null){
           throw new ResetTokenNotFound();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ResponseEntity resetPassword(@RequestBody Map<String, String> userData ){
        String email = userData.get("email");
        String password = userData.get("password");
        RegularUser user = regularUserService.getUserByEmail(email);
        AuctionHouse auctionHouse = auctionHouseService.getAuctionHouseByEmail(email);
        if(user == null && auctionHouse == null){
            throw new UserNotFoundException();
        }
        if(user != null) {
            regularUserService.changePassword(email, password);
        }else{
            auctionHouseService.changePassword(email, password);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
