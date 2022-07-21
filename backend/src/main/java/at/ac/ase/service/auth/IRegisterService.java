package at.ac.ase.service.auth;

import at.ac.ase.entities.Address;
import at.ac.ase.entities.AuctionHouse;
import at.ac.ase.entities.RegularUser;
public interface IRegisterService {

    /**
     * Method that registers a regular user
     * @param user regular user's data
     * @return newly created regular user
     */
    public RegularUser registerUser(RegularUser user);

    /**
     * Method that registers an auction house
     * @param auctionHouse auction house's data
     * @return newly created action house
     */
    public AuctionHouse registerHouse(AuctionHouse auctionHouse);
}
