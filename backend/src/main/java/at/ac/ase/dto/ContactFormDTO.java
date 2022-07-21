package at.ac.ase.dto;


public class ContactFormDTO {

    private Long id;

    private Long auctionPostId;

    private String firstName;

    private String lastName;

    private String phoneNr;

    private String email;

    private String country;

    private String city;

    private String street;

    private Integer houseNr;

    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuctionPostId() {
        return auctionPostId;
    }

    public void setAuctionPostId(Long auctionPostId) {
        this.auctionPostId = auctionPostId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getHouseNr() {
        return houseNr;
    }

    public void setHouseNr(Integer houseNr) {
        this.houseNr = houseNr;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ContactFormDTO{" +
                "id=" + id +
                ", auctionPostId=" + auctionPostId +
                ", phoneNr='" + phoneNr + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", houseNr=" + houseNr +
                '}';
    }
}
