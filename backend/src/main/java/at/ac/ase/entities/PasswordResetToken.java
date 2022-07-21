package at.ac.ase.entities;

import at.ac.ase.util.TokenUtil;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PasswordResetToken")
public class PasswordResetToken {

    private static final int EXPIRATION = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private long token;

    @OneToOne(targetEntity = RegularUser.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private RegularUser user;

    @OneToOne(targetEntity = AuctionHouse.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "auction_house_id")
    private AuctionHouse auctionHouse;

    @Column
    private Date expiryDate;

    public PasswordResetToken() {
    }

    public PasswordResetToken(long token, RegularUser user, AuctionHouse auctionHouse) {
        this.token = token;
        this.user = user;
        this.auctionHouse = auctionHouse;
        this.expiryDate = TokenUtil.calculateExpiryDate(EXPIRATION);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }

    public RegularUser getUser() {
        return user;
    }

    public void setUser(RegularUser user) {
        this.user = user;
    }

    public AuctionHouse getAuctionHouse() {
        return auctionHouse;
    }

    public void setAuctionHouse(AuctionHouse auctionHouse) {
        this.auctionHouse = auctionHouse;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }


}
