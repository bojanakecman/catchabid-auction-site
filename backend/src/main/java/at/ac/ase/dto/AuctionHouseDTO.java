package at.ac.ase.dto;

import at.ac.ase.entities.Address;
import at.ac.ase.entities.AuctionPost;
import at.ac.ase.entities.Rating;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class AuctionHouseDTO {

    private Long id;
    private Boolean active = true;

    @NotNull
    private String tid;

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String phoneNr;

    @NotNull
    private String passwordHash;

    private Boolean isVerifed;

    private AddressDTO address;

    private Set<AuctionPost> ownedAuctions = new HashSet<>();

    private Set<Rating> ratings = new HashSet<>();

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public Set<AuctionPost> getOwnedAuctions() {
        return ownedAuctions;
    }

    public void setOwnedAuctions(Set<AuctionPost> ownedAuctions) {
        this.ownedAuctions = ownedAuctions;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

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

    public Boolean getVerifed() {
        return isVerifed;
    }

    public void setVerifed(Boolean verifed) {
        isVerifed = verifed;
    }
}
