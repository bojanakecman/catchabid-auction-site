package at.ac.ase.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "AuctionHouse")
@JsonInclude(Include.NON_NULL)
public class AuctionHouse extends User {

    @Column
    @NotNull
    private String tid;

    @Column
    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @Embedded
    private Address address;

    public AuctionHouse(){}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AuctionHouse that = (AuctionHouse) o;

        return new EqualsBuilder()
                .append(tid, that.tid)
                .append(name, that.name)
                .append(address, that.address)
                .append(getEmail(),that.getEmail())
                .append(getPhoneNr(),that.getPhoneNr())
                .append(getActive(),that.getActive())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(tid)
                .append(name)
                .append(address)
                .toHashCode();
    }
}
