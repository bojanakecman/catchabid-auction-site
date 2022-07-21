package at.ac.ase.service.user.rating.implementation;

import at.ac.ase.dto.AuctionPostSendDTO;
import at.ac.ase.dto.RatingDataDTO;
import at.ac.ase.entities.*;
import at.ac.ase.repository.user.AuctionHouseRepository;
import at.ac.ase.repository.user.RatingRepository;
import at.ac.ase.repository.user.UserRepository;
import at.ac.ase.service.user.rating.IRatingService;
import at.ac.ase.util.exceptions.InvalidRatingDataException;
import at.ac.ase.util.exceptions.UserAlreadyRatedException;
import at.ac.ase.util.exceptions.UserAndCreatorAreTheSameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class RatingService implements IRatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionHouseRepository auctionHouseRepository;


    @Override
    public Double calculateRating(String email) {
        Set<Rating> ratingSet = null;
        RegularUser regularUser = userRepository.findByEmail(email);
        if (regularUser != null){
            ratingSet = regularUser.getRatings();
        } else {
            AuctionHouse auctionHouse = auctionHouseRepository.findByEmail(email);
            ratingSet = auctionHouse.getRatings();
        }
        Double sum = 0.0;
        for(Rating r: ratingSet){
            sum += r.getRating();
        }
        return sum / ratingSet.size();

    }

    @Override
    public void checkIfUserIsRatedAlready(AuctionPostSendDTO auctionPostSendDTO, User user){
        RatingPK ratingPK = new RatingPK(auctionPostSendDTO.getId(), user.getId());
        Optional<Rating> rating = ratingRepository.findById(ratingPK);
        if (rating.isPresent()) {
            throw new UserAlreadyRatedException();
        } else if (auctionPostSendDTO.getCreatorEmail().equals(user.getEmail())){
            throw new UserAndCreatorAreTheSameException();
        }
    }

    @Override
    public void setRating(RatingDataDTO ratingData, User user) {
        if(validateRatingData(ratingData)){
            RatingPK ratingPK = new RatingPK(ratingData.getAuctionPost().getId(), user.getId());
            Rating rating = new Rating();
            rating.setId(ratingPK);
            RegularUser regularUser = userRepository.findByEmail(ratingData.getAuctionPost().getCreatorEmail());
            if (regularUser != null){
               if (!regularUser.getEmail().equals(user.getEmail())){
                   Double userRating = calculateRating(regularUser.getEmail());
                   if(regularUser.getRatings().size() >= 15 && userRating >= 4.0){
                       if(!regularUser.getVerified()) {
                           regularUser.setVerified(true);
                           userRepository.save(regularUser);
                       }
                   }
                   rating.setUser(regularUser);
               } else {
                   throw new UserAndCreatorAreTheSameException();
               }
            } else {
                AuctionHouse auctionHouse = auctionHouseRepository.findByEmail(ratingData.getAuctionPost().getCreatorEmail());
                rating.setUser(auctionHouse);
            }
            rating.setRating(ratingData.getRatingValue());
            ratingRepository.save(rating);
        } else {
            throw new InvalidRatingDataException();
        }
    }

    private boolean validateRatingData(RatingDataDTO ratingDataDTO){
        boolean correctRatingValue = false;
        boolean correctTimePeriod = false;
        if (ratingDataDTO.getRatingValue() <= 5 && ratingDataDTO.getRatingValue() >= 1){
            correctRatingValue = true;
        }
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        Duration duration = Duration.between(ratingDataDTO.getAuctionPost().getEndTime(), now);
        if(duration.toDays() <= 30){
            correctTimePeriod = true;
        }
        return correctRatingValue && correctTimePeriod;
    }
}
