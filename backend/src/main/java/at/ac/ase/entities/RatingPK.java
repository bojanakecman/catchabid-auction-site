package at.ac.ase.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RatingPK implements Serializable {

    @Column(name = "auction_post_id")
    public Long auctionPostId;
    @Column(name = "rated_by")
    public Long ratedById;

    public RatingPK(){};

    public RatingPK(Long auctionPost, Long ratedById){
        this.auctionPostId = auctionPost;
        this.ratedById = ratedById;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingPK ratingPK = (RatingPK) o;
        return Objects.equals(auctionPostId, ratingPK.auctionPostId) &&
                Objects.equals(ratedById, ratingPK.ratedById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auctionPostId, ratedById);
    }
}
