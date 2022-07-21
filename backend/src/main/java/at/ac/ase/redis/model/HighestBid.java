package at.ac.ase.redis.model;

import java.io.Serializable;

public class HighestBid implements Serializable {

    private Long userId;

    private Double offer;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getOffer() {
        return offer;
    }

    public void setOffer(Double offer) {
        this.offer = offer;
    }
}
