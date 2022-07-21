package at.ac.ase.service.user;

import at.ac.ase.dto.AuctionHouseDTO;
import at.ac.ase.entities.AuctionHouse;
import at.ac.ase.entities.AuctionPost;

import java.util.List;
import java.util.Optional;

public interface IAuctionHouseService {

    /**
     * Method which retrieves an auction house based on its id
     * @param id of an auction house
     * @return auction house if found
     */
    Optional<AuctionHouse> getAuctionHouseById(Long id);

    /**
     * Method which retrieves all auction houses from the database
     * @return list of auction houses
     */
    List<AuctionHouse> getAllHouses();

    /**
     * Method which retrieves a single auction house based on its email
     * @param email of the auction house to be retrieved
     * @return auction house if found
     */
    AuctionHouse getAuctionHouseByEmail(String email);

    /**
     * Method which updates a auction house's password
     * @param email an email address of auction house
     * @param password a new password
     */
    void changePassword(String email, String password);

    /**
     * Method which updates an auction house
     * @param email an email address of auction house that should be updated
     * @param updatedAuctionHouseDTO updatedAuctionHouse
     */
    AuctionHouse updateHouse(String email, AuctionHouseDTO updatedAuctionHouseDTO);

}
