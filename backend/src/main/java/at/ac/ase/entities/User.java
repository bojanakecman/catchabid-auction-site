package at.ac.ase.entities;

import at.ac.ase.dto.RatingDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Boolean active = true;

    @Column(unique = true)
    @NotNull
    @Pattern(regexp="^[a-zA-Z0-9_.+-üäöß]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]{2,3}$",
        message="Please provide a valid email address")
    private String email;

    @Column
    private String phoneNr;

    @Column
    private String passwordHash;

    @Column
    private Boolean isVerified;

    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = "user",
        orphanRemoval = true)
    private Set<Rating> ratings = new HashSet<>();

    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = "user",
        orphanRemoval = true)
    private Set<Bid> bids = new HashSet<>();

    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = "creator",
        cascade = CascadeType.ALL,
        orphanRemoval = true)
    @JsonBackReference
    private Set<AuctionPost> ownedAuctions = new HashSet<>();

    public User(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }

    public Set<AuctionPost> getOwnedAuctions() {
        return ownedAuctions;
    }

    public void setOwnedAuctions(Set<AuctionPost> ownedAuctions) {
        this.ownedAuctions = ownedAuctions;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", active=" + active +
                ", email='" + email + '\'' +
                ", phoneNr='" + phoneNr + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", ratings=" + ratings +
                ", bids=" + bids +
                ", ownedAuctions=" + ownedAuctions +
                '}';
    }
}
