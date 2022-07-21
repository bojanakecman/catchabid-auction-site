package at.ac.ase.service.user.implementation;

import at.ac.ase.dto.RegularUserDTO;
import at.ac.ase.dto.translator.UserDtoTranslator;
import at.ac.ase.entities.Address;
import at.ac.ase.entities.AuctionPost;
import at.ac.ase.entities.RegularUser;
import at.ac.ase.entities.User;
import at.ac.ase.repository.user.UserRepository;
import at.ac.ase.service.user.IRegularUserService;
import at.ac.ase.util.exceptions.EmptyObjectException;
import at.ac.ase.util.exceptions.UserAlreadyExistsException;
import at.ac.ase.util.exceptions.UserNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RegularUserService implements IRegularUserService {

    @Autowired
    private UserRepository userRepository;

    @Lazy
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    UserDtoTranslator userDtoTranslator;

    @Override
    public List<RegularUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public RegularUser getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void changePassword(String email, String password) {
        String passwordHash = encoder.encode(password);
        userRepository.changePassword(email, passwordHash);
    }

    @Override
    public Optional<RegularUser> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public RegularUser updateUser(String email, RegularUserDTO updatedUser) {
        RegularUser user = userDtoTranslator.toRegularUser(updatedUser);
        if (user == null ) {
            throw new EmptyObjectException();
        }else {
            if (StringUtils.isNotBlank(email)) {
                RegularUser persisted = getUserByEmail(email);
                if (persisted == null) {
                    throw new UserNotFoundException();

                }
                if (persisted.equals(user)) {
                    return persisted;
                }
                if (!email.equals(updatedUser.getEmail())){
                    RegularUser exists = getUserByEmail(updatedUser.getEmail());
                    if (exists!=null){
                        throw new UserAlreadyExistsException();
                    }
                }

                return userRepository.save(updatePersistedValues(persisted, user));

            }else {
                throw new EmptyObjectException();
            }
        }

    }

    private RegularUser updatePersistedValues(RegularUser persisted, RegularUser updated) {
        if (StringUtils.isNotBlank(updated.getEmail())) {
            persisted.setEmail(updated.getEmail());
        }
        if (StringUtils.isNotBlank(updated.getFirstName())) {
            persisted.setFirstName(updated.getFirstName());
        }
        if (StringUtils.isNotBlank(updated.getLastName())) {
            persisted.setLastName(updated.getLastName());
        }
        Address updatedAddress = updated.getAddress();
        if (updatedAddress != null) {
            if (persisted.getAddress()==null){
                persisted.setAddress(new Address());
            }
            if (StringUtils.isNotBlank(updatedAddress.getStreet())) {
                persisted.getAddress().setStreet(updatedAddress.getStreet());
            }
            if (updatedAddress.getHouseNr()!=null) {
                persisted.getAddress().setHouseNr(updatedAddress.getHouseNr());
            }
            if (StringUtils.isNotBlank(updatedAddress.getCity())) {
                persisted.getAddress().setCity(updatedAddress.getCity());
            }
            if (StringUtils.isNotBlank(updatedAddress.getCountry())) {
                persisted.getAddress().setCountry(updatedAddress.getCountry());
            }
        }
        if (StringUtils.isNotBlank(updated.getPhoneNr())) {
            persisted.setPhoneNr(updated.getPhoneNr());
        }
        if (updated.getPreferences() != null || !updated.getPreferences().isEmpty()) {
            persisted.setPreferences(updated.getPreferences());
        }

        return persisted;
    }

}
