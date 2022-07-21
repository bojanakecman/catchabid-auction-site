package at.ac.ase.service.payment;

import at.ac.ase.entities.AuctionPost;
import com.stripe.model.PaymentIntent;

public interface IPaymentService {

    PaymentIntent createPaymentIntent(AuctionPost auctionPost);

    boolean checkPaymentCharged(String paymentIntentId);

}
