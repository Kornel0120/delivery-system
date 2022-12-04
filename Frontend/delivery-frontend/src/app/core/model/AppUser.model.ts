/*private long userId;
    private String email;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;*/

export interface AppUser {
    userId: number;
    email: string;
    password: Date;
    token: string;
    refreshToken: string;
    roles: string[];
}