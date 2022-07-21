package at.ac.ase.dto;

import java.util.Date;

public class NotificationDTO {

    private Long id;

    private String info;

    private Boolean seen;

    private Date date;

    private AuctionPostSendDTO auctionPostSendDTO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public AuctionPostSendDTO getAuctionPostSendDTO() {
        return auctionPostSendDTO;
    }

    public void setAuctionPostSendDTO(AuctionPostSendDTO auctionPostSendDTO) {
        this.auctionPostSendDTO = auctionPostSendDTO;
    }
}
