package at.ac.ase.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class Rating {

    @Id
    private RatingPK id;

    @Column
    @NotNull
    private Integer rating;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;


    public RatingPK getId() {
        return id;
    }

    public void setId(RatingPK id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
