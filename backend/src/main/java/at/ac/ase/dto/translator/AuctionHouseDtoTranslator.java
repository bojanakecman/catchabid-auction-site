package at.ac.ase.dto.translator;

import at.ac.ase.dto.AuctionHouseDTO;
import at.ac.ase.entities.AuctionHouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuctionHouseDtoTranslator {

    @Autowired
    private AddressDtoTranslator addressDtoTranslator;

    public AuctionHouseDTO toAuctionHouseDTO(AuctionHouse auctionHouse){
        AuctionHouseDTO auctionHouseDTO = new AuctionHouseDTO();
        auctionHouseDTO.setTid(auctionHouse.getTid());
        auctionHouseDTO.setAddress(addressDtoTranslator.toAddressDTO(auctionHouse.getAddress()));
        auctionHouseDTO.setName(auctionHouse.getName());
        auctionHouseDTO.setId(auctionHouse.getId());
        auctionHouseDTO.setActive(auctionHouse.getActive());
        auctionHouseDTO.setOwnedAuctions(auctionHouse.getOwnedAuctions());
        auctionHouseDTO.setPasswordHash(auctionHouse.getPasswordHash());
        auctionHouseDTO.setPhoneNr(auctionHouse.getPhoneNr());
        auctionHouseDTO.setEmail(auctionHouse.getEmail());
        auctionHouseDTO.setOwnedAuctions(auctionHouse.getOwnedAuctions());
        auctionHouseDTO.setVerifed(auctionHouse.getVerified());
        return auctionHouseDTO;
    }

    public AuctionHouse toAuctionHouse(AuctionHouseDTO houseDTO){
        AuctionHouse auctionHouse = new AuctionHouse();
        auctionHouse.setTid(houseDTO.getTid());
        auctionHouse.setAddress(addressDtoTranslator.toAddress(houseDTO.getAddress()));
        auctionHouse.setName(houseDTO.getName());
        auctionHouse.setId(houseDTO.getId());
        auctionHouse.setActive(houseDTO.getActive());
        auctionHouse.setOwnedAuctions(houseDTO.getOwnedAuctions());
        auctionHouse.setPasswordHash(houseDTO.getPasswordHash());
        auctionHouse.setPhoneNr(houseDTO.getPhoneNr());
        auctionHouse.setEmail(houseDTO.getEmail());
        auctionHouse.setOwnedAuctions(houseDTO.getOwnedAuctions());
        auctionHouse.setVerified(houseDTO.getVerifed());
        return auctionHouse;
    }
}
