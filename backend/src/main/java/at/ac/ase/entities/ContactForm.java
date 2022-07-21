package at.ac.ase.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table
public class ContactForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column
    private String firstName;

    @NotNull
    @Column
    private String lastName;

    @NotNull
    @Embedded
    private Address address;

    @NotNull
    @Column
    private String phoneNr;

    @NotNull
    @Pattern(regexp="^[a-zA-Z0-9_.+-üäöß]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]{2,3}$",
            message="Please provide a valid email address")
    @Column
    private String email;

    @Column(length = 1500)
    private String remark;

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ContactForm{" +
                "id=" + id +
                ", address=" + address +
                ", phoneNr='" + phoneNr + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
