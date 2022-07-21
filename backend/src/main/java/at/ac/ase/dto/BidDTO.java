package at.ac.ase.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class BidDTO {

    @NotNull
    private Double offer;

    @NotNull
    @DecimalMin("0.1")
    private Long auctionId;

    public Double getOffer() {
        return offer;
    }

    public void setOffer(Double offer) {
        this.offer = offer;
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }
}
