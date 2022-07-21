package at.ac.ase.service.user.rating;

import at.ac.ase.dto.AuctionPostSendDTO;
import at.ac.ase.dto.RatingDataDTO;
import at.ac.ase.entities.Rating;
import at.ac.ase.entities.RegularUser;
import at.ac.ase.entities.User;
import org.springframework.stereotype.Service;

public interface IRatingService {

    Double calculateRating(String email);

    void setRating(RatingDataDTO ratingData, User user);

    void checkIfUserIsRatedAlready(AuctionPostSendDTO ratingDataDTO, User user);
}
