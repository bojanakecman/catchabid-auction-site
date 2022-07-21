package at.ac.ase.dto.translator;

import at.ac.ase.dto.PaymentIntentResponseDTO;
import com.stripe.model.PaymentIntent;
import org.springframework.stereotype.Component;

@Component
public class PaymentIntentToPaymentIntentResponseTranslator {

    public PaymentIntentResponseDTO paymentIntentToPaymentIntentResponse(
        PaymentIntent paymentIntent) {

        PaymentIntentResponseDTO pirDTO = new PaymentIntentResponseDTO();

        if (paymentIntent != null) {
            pirDTO.setId(paymentIntent.getId());
            pirDTO.setAmount(paymentIntent.getAmount());
            pirDTO.setClientSecret(paymentIntent.getClientSecret());
            pirDTO.setCaptureMethod(paymentIntent.getCaptureMethod());
            pirDTO.setConfirmationMethod(paymentIntent.getConfirmationMethod());
            pirDTO.setAuctionId(Long.valueOf(paymentIntent.getMetadata().get("auctionId")));
            pirDTO.setBidId(Long.valueOf(paymentIntent.getMetadata().get("bidId")));
            pirDTO.setAuctionId(Long.valueOf(paymentIntent.getMetadata().get("auctionId")));
        }

        return pirDTO;
    }


}
