import { Address } from './address';

export class User {
    id: number;
    firstName: string;
    lastName: string;
    passwordHash: string;
    email: string;
    phoneNr: string;
    address: Address;
    preferences: [];
    ratings: [];
}
