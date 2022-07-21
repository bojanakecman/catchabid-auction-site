package at.ac.ase.dto;


public class RatingDTO {

    private Long id;
    private Integer rating;
    private RegularUserDTO user;
    private RegularUserDTO ratedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public RegularUserDTO getUser() {
        return user;
    }

    public void setUser(RegularUserDTO user) {
        this.user = user;
    }

    public RegularUserDTO getRatedBy() {
        return ratedBy;
    }

    public void setRatedBy(RegularUserDTO ratedBy) {
        this.ratedBy = ratedBy;
    }
}
