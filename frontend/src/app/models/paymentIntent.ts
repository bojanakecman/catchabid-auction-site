export interface PaymentIntent {
  id?: string;
  amount?: number;
  captureMethod: string;
  clientSecret: string;
  confirmationMethod: string;
  auctionId: number;
  customerId: number;
  bidId: number;
}
