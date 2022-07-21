package at.ac.ase.dto;

public class RatingDataDTO {

    private AuctionPostSendDTO auctionPost;

    private Integer ratingValue;

    public AuctionPostSendDTO getAuctionPost() {
        return auctionPost;
    }

    public void setAuctionPost(AuctionPostSendDTO auctionPost) {
        this.auctionPost = auctionPost;
    }

    public Integer getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Integer ratingValue) {
        this.ratingValue = ratingValue;
    }

}
