export interface AppUser {
    userId: number;
    email: string;
    password: Date;
    token: string;
    refreshToken: string;
    roles: string[];
}