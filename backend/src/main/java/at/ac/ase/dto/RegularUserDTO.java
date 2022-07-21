package at.ac.ase.dto;

import at.ac.ase.entities.*;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public class RegularUserDTO {

    private Long id;

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String phoneNr;
    @NotNull
    private String passwordHash;
    @NotNull
    private String email;

    private Boolean isVerified;


    private AddressDTO address;

    private Set<Bid> bids = new HashSet<>();
    private Boolean active = true;
    private Set<RatingDTO> ratings = new HashSet<>();
    private Set<AuctionPostSendDTO> ownedAuctions = new HashSet<>();
    private Set<Category> preferences = new HashSet<>();

    public Set<Category> getPreferences() {
        return preferences;
    }

    public void setPreferences(Set<Category> preferences) {
        this.preferences = preferences;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<RatingDTO> getRatings() {
        return ratings;
    }

    public void setRatings(Set<RatingDTO> ratings) {
        this.ratings = ratings;
    }

    public Set<AuctionPostSendDTO> getOwnedAuctions() {
        return ownedAuctions;
    }

    public void setOwnedAuctions(Set<AuctionPostSendDTO> ownedAuctions) {
        this.ownedAuctions = ownedAuctions;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }
}
