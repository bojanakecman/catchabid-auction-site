package at.ac.ase.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonBackReference(value = "post_highest_bid")
    private AuctionPost auction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference(value = "user_highest_bid")
    private RegularUser user;

    @Column
    @NotNull
    @DecimalMin("0.1")
    private Double offer;

    private LocalDateTime dateTime = LocalDateTime.now(ZoneOffset.UTC);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuctionPost getAuction() {
        return auction;
    }

    public void setAuction(AuctionPost auction) {
        this.auction = auction;
    }

    public RegularUser getUser() {
        return user;
    }

    public void setUser(RegularUser user) {
        this.user = user;
    }

    public Double getOffer() {
        return offer;
    }

    public void setOffer(Double offer) {
        this.offer = offer;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
