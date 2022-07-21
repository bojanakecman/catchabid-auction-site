package at.ac.ase.dto.translator;

import at.ac.ase.dto.RatingDTO;
import at.ac.ase.dto.RegularUserDTO;
import at.ac.ase.entities.AuctionPost;
import at.ac.ase.entities.Rating;
import at.ac.ase.entities.RegularUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDtoTranslator {

    @Autowired
    private AddressDtoTranslator addressDtoTranslator;

    @Autowired
    private RatingDtoTranslator ratingDtoTranslator;

    @Autowired
    private AuctionDtoTranslator auctionDtoTranslator;

    @Autowired
    private ModelMapper modelMapper;


    public RegularUserDTO toRegularUserDTO(RegularUser regularUser){
        RegularUserDTO regularUserDTO = new RegularUserDTO();
        regularUserDTO.setId(regularUser.getId());
        regularUserDTO.setEmail(regularUser.getEmail());
        regularUserDTO.setAddress(addressDtoTranslator.toAddressDTO(regularUser.getAddress()));
        regularUserDTO.setActive(regularUser.getActive());
        regularUserDTO.setFirstName(regularUser.getFirstName());
        regularUserDTO.setLastName(regularUser.getLastName());
        regularUserDTO.setPasswordHash(regularUser.getPasswordHash());
        regularUserDTO.setPhoneNr(regularUser.getPhoneNr());
        regularUserDTO.setOwnedAuctions(auctionDtoTranslator.toDtoSet(regularUser.getOwnedAuctions()));
        regularUserDTO.setRatings(ratingDtoTranslator.toDtoSet(regularUser.getRatings()));
        regularUserDTO.setPreferences(regularUser.getPreferences());
        regularUserDTO.setVerified(regularUser.getVerified());
        return regularUserDTO;
    }

    public RegularUser toRegularUser(RegularUserDTO regularUserDTO){
        RegularUser regularUser = new RegularUser();
        regularUser.setId(regularUserDTO.getId());
        regularUser.setEmail(regularUserDTO.getEmail());
        regularUser.setAddress(addressDtoTranslator.toAddress(regularUserDTO.getAddress()));
        regularUser.setActive(regularUserDTO.getActive());
        regularUser.setFirstName(regularUserDTO.getFirstName());
        regularUser.setLastName(regularUserDTO.getLastName());
        regularUser.setPasswordHash(regularUserDTO.getPasswordHash());
        regularUser.setPhoneNr(regularUserDTO.getPhoneNr());
        //regularUser.setRatings(regularUserDTO.getRatings());
        regularUser.setPreferences(regularUserDTO.getPreferences());
        regularUser.setVerified(regularUserDTO.getVerified());
        return regularUser;
    }

}
