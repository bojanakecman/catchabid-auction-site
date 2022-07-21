package at.ac.ase.service.user.implementation;

import at.ac.ase.dto.AuctionHouseDTO;
import at.ac.ase.dto.translator.AuctionHouseDtoTranslator;
import at.ac.ase.entities.Address;
import at.ac.ase.entities.AuctionHouse;
import at.ac.ase.entities.AuctionPost;
import at.ac.ase.entities.RegularUser;
import at.ac.ase.repository.user.AuctionHouseRepository;
import at.ac.ase.service.user.IAuctionHouseService;
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

@Service
public class AuctionHouseService implements IAuctionHouseService {

    @Autowired
    private AuctionHouseRepository auctionHouseRepository;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuctionHouseDtoTranslator auctionHouseDtoTranslator;

    @Override
    public Optional<AuctionHouse> getAuctionHouseById(Long id) {
        return auctionHouseRepository.findById(id);
    }

    @Override
    public List<AuctionHouse> getAllHouses(){
        return auctionHouseRepository.findAll();
    }

    @Override
    public AuctionHouse getAuctionHouseByEmail(String email) {
        return auctionHouseRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void changePassword(String email, String password) {
        String passwordHash = passwordEncoder.encode(password);
        auctionHouseRepository.changePassword(email, passwordHash);
    }
    public AuctionHouse updateHouse(String email, AuctionHouseDTO updatedAuctionHouseDTO){
        AuctionHouse auctionHouse = auctionHouseDtoTranslator.toAuctionHouse(updatedAuctionHouseDTO);
        if (auctionHouse == null) {
            throw new EmptyObjectException();
        }else {

            if (StringUtils.isNotBlank(email)) {
                AuctionHouse persisted = getAuctionHouseByEmail(email);
                if (persisted == null) {
                    throw new UserNotFoundException();

                }
                if (persisted.equals(auctionHouse)) {
                    return persisted;
                }
                if (email!=updatedAuctionHouseDTO.getEmail()){
                    AuctionHouse exists = getAuctionHouseByEmail(updatedAuctionHouseDTO.getEmail());
                    if (exists!=null){
                        throw new UserAlreadyExistsException();
                    }
                }

                return auctionHouseRepository.save(updatePersistedValues(persisted, auctionHouse));

            }else {
                throw new EmptyObjectException();
            }
        }
    }

    private AuctionHouse updatePersistedValues(AuctionHouse persisted, AuctionHouse updated) {
        if (StringUtils.isNotBlank(updated.getEmail())) {
            persisted.setEmail(updated.getEmail());
        }
        if (StringUtils.isNotBlank(updated.getName())) {
            persisted.setName(updated.getName());
        }
        if (StringUtils.isNotBlank(updated.getTid())) {
            persisted.setTid(updated.getTid());
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
        persisted.setAddress(updated.getAddress());
        if (StringUtils.isNotBlank(updated.getEmail())) {
            persisted.setPhoneNr(updated.getPhoneNr());
        }

        return persisted;
    }
}
