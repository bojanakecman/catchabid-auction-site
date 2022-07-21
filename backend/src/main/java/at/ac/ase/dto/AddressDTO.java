package at.ac.ase.dto;

import javax.persistence.Column;

public class AddressDTO {


    private String country;

    private String city;

    private String street;

    private Integer houseNr;

    public AddressDTO() {
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

    @Override
    public String toString() {
        return "AddressDTO{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", houseNr=" + houseNr +
                '}';
    }
}
