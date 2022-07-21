package at.ac.ase.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class RegularUserNotification extends Notification {

    @ManyToOne(fetch = FetchType.EAGER)
    private RegularUser receiver;

    public RegularUser getReceiver() {
        return receiver;
    }

    public void setReceiver(RegularUser receiver) {
        this.receiver = receiver;
    }
}
