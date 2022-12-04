import { Injectable } from '@angular/core';

const TOKEN_KEY = 'auth-token';
const REFRESHTOKEN_KEY = 'refresh-token';
const USER_KEY = 'auth-user';

@Injectable()
export class TokenStorageService {
  constructor() { }

  getToken(): string {
    return window.sessionStorage[TOKEN_KEY];
  }

  getRefreshToken(): string {
    return window.sessionStorage[REFRESHTOKEN_KEY];
  }

  saveToken(token: string) {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  saveRefreshToken(refreshToken: string) {
    window.sessionStorage.removeItem(REFRESHTOKEN_KEY);
    window.sessionStorage.setItem(REFRESHTOKEN_KEY, refreshToken);
  }

  destroyToken() {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.removeItem(REFRESHTOKEN_KEY);
  }
}
