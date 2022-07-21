package at.ac.ase.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "RegularUser")
public class RegularUser extends User {

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
        name = "regular_user_preferences",
        joinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Category> preferences = new HashSet<>();

    @Column
    @NotNull
    @Size(min = 1, max = 50)
    private String firstName;

    @Column
    @NotNull
    @Size(min = 1, max = 50)
    private String lastName;

    @Embedded
    private Address address;

    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonManagedReference(value = "user_highest_bid")
    private Set<Bid> bids = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(
            name = "auction_subscriptions",
            joinColumns = { @JoinColumn(name = "regular_user_id" ) },
            inverseJoinColumns = { @JoinColumn(name = "auction_post_id") })
    private Set<AuctionPost> subscriptions = new HashSet<>();

    public RegularUser(){}

    public Set<Category> getPreferences() {
        return preferences;
    }

    public void setPreferences(Set<Category> preferences) {
        this.preferences = preferences;
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


    @Override
    public Set<Bid> getBids() {
        return bids;
    }

    @Override
    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }

    public Set<AuctionPost> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<AuctionPost> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RegularUser that = (RegularUser) o;

        return new EqualsBuilder()
                .append(preferences, that.preferences)
                .append(getEmail(),that.getEmail())
                .append(firstName, that.firstName)
                .append(lastName, that.lastName)
                .append(address, that.address)
                .append(bids, that.bids)
                .append(getPhoneNr(), that.getPhoneNr())
                .append(getActive(), that.getActive())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(preferences)
                .append(firstName)
                .append(lastName)
                .append(address)
                .append(bids)
                .toHashCode();
    }
}
