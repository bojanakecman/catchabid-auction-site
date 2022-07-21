package at.ac.ase.repository.notification;

import at.ac.ase.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface NotificationRepository extends JpaRepository<Notification, Long> {


}
