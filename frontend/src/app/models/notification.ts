import { AuctionPost } from './auctionpost';

export interface Notification {
  id?: number;
  info?: number;
  seen?: boolean;
  date?: Date;
}
