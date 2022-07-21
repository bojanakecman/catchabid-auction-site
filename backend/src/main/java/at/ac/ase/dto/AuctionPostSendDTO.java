package at.ac.ase.dto;

import at.ac.ase.entities.Address;
import at.ac.ase.entities.RegularUser;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class AuctionPostSendDTO {
    private Long id;

    private String creatorName;

    private Long creatorId;

    private String creatorEmail;

    private Boolean creatorVerified;

    private String category;

    private String status;

    private String auctionName;

    private String description;

    private String country;

    private String city;

    private String address;

    private Integer houseNr;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Double minPrice;

    private Double highestBid;

    private Address location;

    private String image;

    private Boolean paid;

    private Boolean contactFormSubmitted;

    private Set<RegularUserDTO> subscriptions = new HashSet<>();

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getHouseNr() {
        return houseNr;
    }

    public void setHouseNr(Integer houseNr) {
        this.houseNr = houseNr;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(Double highestBid) {
        this.highestBid = highestBid;
    }

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }

    public String getAuctionName() {
        return auctionName;
    }

    public void setAuctionName(String auctionName) {
        this.auctionName = auctionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Boolean getContactFormSubmitted() {
        return contactFormSubmitted;
    }

    public void setContactFormSubmitted(Boolean contactFormSubmitted) {
        this.contactFormSubmitted = contactFormSubmitted;
    }

    public Set<RegularUserDTO> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<RegularUserDTO> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Boolean getCreatorVerified() {
        return creatorVerified;
    }

    public void setCreatorVerified(Boolean creatorVerified) {
        this.creatorVerified = creatorVerified;
    }
}
