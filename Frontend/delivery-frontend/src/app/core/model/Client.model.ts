import { AppUser } from "./AppUser.model";

export interface Client {
    empId: number,
    user: AppUser,
    firstName: string,
    lastName: string,
    phone: string
}