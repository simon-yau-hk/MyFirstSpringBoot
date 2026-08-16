package org.example.dto.auth;

public class AuthResponse {

    private String token;
    private String type = "Bearer";
    private long expiresInMs;
    private AuthUserResponse user;

    public AuthResponse() {
    }

    public AuthResponse(String token, long expiresInMs, AuthUserResponse user) {
        this.token = token;
        this.expiresInMs = expiresInMs;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getExpiresInMs() {
        return expiresInMs;
    }

    public void setExpiresInMs(long expiresInMs) {
        this.expiresInMs = expiresInMs;
    }

    public AuthUserResponse getUser() {
        return user;
    }

    public void setUser(AuthUserResponse user) {
        this.user = user;
    }
}
