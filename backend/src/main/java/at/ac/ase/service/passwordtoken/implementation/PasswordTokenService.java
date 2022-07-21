package at.ac.ase.service.passwordtoken.implementation;

import at.ac.ase.entities.AuctionHouse;
import at.ac.ase.entities.PasswordResetToken;
import at.ac.ase.entities.RegularUser;
import at.ac.ase.repository.token.PasswordTokenRepository;
import at.ac.ase.repository.user.AuctionHouseRepository;
import at.ac.ase.repository.user.UserRepository;
import at.ac.ase.service.passwordtoken.IPasswordTokenService;
import at.ac.ase.util.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordTokenService implements IPasswordTokenService {

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionHouseRepository auctionHouseRepository;

    @Override
    @Transactional
    public PasswordResetToken getPasswordResetTokenByToken(String email, long token) {
        RegularUser user = userRepository.findByEmail(email);
        AuctionHouse auctionHouse = auctionHouseRepository.findByEmail(email);
        if(user == null && auctionHouse == null){
            throw new UserNotFoundException();
        }
        return passwordTokenRepository.findByToken(user, auctionHouse, token);
    }
}
