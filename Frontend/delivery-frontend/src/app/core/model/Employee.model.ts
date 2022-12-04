import { AppUser } from "./AppUser.model";

export interface Employee {
    empId: number,
    user: AppUser,
    firstName: string,
    lastName: string,
    phone: string
}