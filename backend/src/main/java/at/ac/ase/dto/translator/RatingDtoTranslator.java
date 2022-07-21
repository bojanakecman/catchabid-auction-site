package at.ac.ase.dto.translator;

import at.ac.ase.dto.RatingDTO;
import at.ac.ase.entities.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RatingDtoTranslator {

    @Autowired
    private UserDtoTranslator userDtoTranslator;

    public RatingDTO toRatingDTO(Rating rating){
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setRating(rating.getRating());
        //ratingDTO.setUser(userDtoTranslator.toRegularUserDTO(rating.getUser()));
        return ratingDTO;
    }

    public Set<RatingDTO> toDtoSet(Set<Rating> ratingSet){
        return ratingSet.stream().map(this::toRatingDTO).collect(Collectors.toSet());
    }
}
