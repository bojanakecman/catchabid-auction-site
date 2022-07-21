package at.ac.ase.service.auth.implementation;

import at.ac.ase.entities.AuctionHouse;
import at.ac.ase.entities.RegularUser;
import at.ac.ase.repository.user.AuctionHouseRepository;
import at.ac.ase.repository.user.UserRepository;
import at.ac.ase.service.auth.IRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Registraction service that manages registration requests from {@link at.ac.ase.controllers.AuthController}
 */
@Service
public class RegisterService implements IRegisterService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuctionHouseRepository auctionHouseRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public RegularUser registerUser(RegularUser userToRegister) {
        userToRegister.setPasswordHash(passwordEncoder.encode(userToRegister.getPasswordHash()));
        return (RegularUser) userRepository.save(userToRegister);
    }

    @Override
    public AuctionHouse registerHouse(AuctionHouse ahouse) {
        ahouse.setPasswordHash(passwordEncoder.encode(ahouse.getPasswordHash()));
        ahouse.setVerified(true);
        return auctionHouseRepository.save(ahouse);
    }

}
