package at.ac.ase.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class AuctionPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creator")
    private User creator;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    @NotNull
    private LocalDateTime startTime;

    @Column
    @NotNull
    private LocalDateTime endTime;

    @Column
    @NotNull
    private Double minPrice;

    @Column(length = 1500)
    private String description;

    @Column
    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] image;

    @NotNull
    @Column
    private String name;

    @Embedded
    private Address address;

    @OneToOne
    @JoinColumn(name = "bid_id", referencedColumnName = "id")
    @JsonManagedReference(value = "post_highest_bid")
    private Bid highestBid;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
        name = "auction_subscriptions",
        joinColumns = { @JoinColumn(name = "auction_post_id") },
        inverseJoinColumns = { @JoinColumn(name = "regular_user_id") })
    private Set<RegularUser> subscriptions = new HashSet<>();

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "payment")
    private Payment payment;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="contact_form")
    private ContactForm contactForm;

    @Enumerated(EnumType.STRING)
    private AuctionPopularity auctionPopularity;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public Bid getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(Bid highestBid) {
        this.highestBid = highestBid;
    }

    public Set<RegularUser> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<RegularUser> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public boolean isUpcoming() {
        return this.status == Status.UPCOMING && startTime.isAfter(LocalDateTime.now(ZoneOffset.UTC));
    }

    public boolean isActive() {
        return (this.status == Status.UPCOMING || this.status == Status.ACTIVE)
                && startTime.isBefore(LocalDateTime.now(ZoneOffset.UTC)) && endTime.isAfter(LocalDateTime.now(ZoneOffset.UTC));
    }

    public ContactForm getContactForm() {
        return contactForm;
    }

    public void setContactForm(ContactForm contactForm) {
        this.contactForm = contactForm;
    }

    public AuctionPopularity getAuctionPopularity() {
        return auctionPopularity;
    }

    public void setAuctionPopularity(AuctionPopularity auctionPopularity) {
        this.auctionPopularity = auctionPopularity;
    }
}
