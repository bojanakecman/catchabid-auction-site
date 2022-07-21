package at.ac.ase.service.payment.implementation;

import at.ac.ase.entities.AuctionPost;
import at.ac.ase.service.payment.IPaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentsService implements IPaymentService {

    @Value("${application.stripe.api-key}")
    private String stripeApiKey;

    @Value("${application.currency}")
    private String currency;

    /**
     * Method for creating a Payment intent object using Stripe library
     *
     * @param auctionPost {@link AuctionPost} object the payment intent should be created for
     * @return Created {@link PaymentIntent} object including client_id
     */
    public PaymentIntent createPaymentIntent(AuctionPost auctionPost) {

        Stripe.apiKey = stripeApiKey;

        List<Object> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", auctionPost.getHighestBid().getOffer().intValue() * 100);
        params.put("currency", currency.toLowerCase());
        params.put("payment_method_types",
            paymentMethodTypes);

        Map<String, String> metadata = new HashMap<>();
        metadata.put("auctionId", auctionPost.getId().toString());
        metadata.put("bidId", auctionPost.getHighestBid().getId().toString());
        metadata.put("customerId",
            auctionPost.getHighestBid().getUser().getId().toString());
        params.put("metadata", metadata);

        try {
            return PaymentIntent.create(params);
        } catch (StripeException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean checkPaymentCharged(String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            return paymentIntent.getCharges().getData().get(0).getPaid();
        } catch (StripeException e) {
            return false;
        }
    }

}
