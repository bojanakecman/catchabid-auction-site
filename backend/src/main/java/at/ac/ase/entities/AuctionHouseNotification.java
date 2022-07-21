package at.ac.ase.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class AuctionHouseNotification extends Notification {

    @ManyToOne(fetch = FetchType.EAGER)
    private AuctionHouse receiver;

    public AuctionHouse getReceiver() {
        return receiver;
    }

    public void setReceiver(AuctionHouse receiver) {
        this.receiver = receiver;
    }
}
