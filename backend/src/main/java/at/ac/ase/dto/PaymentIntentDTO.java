package at.ac.ase.dto;

import javax.validation.constraints.NotNull;

public class PaymentIntentDTO {

    @NotNull
    private Long auctionId;

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }
}
