package org.autogarden.service;

public class TokenManager {

    private static TokenManager tokenManager;
    private String token;

    public static synchronized TokenManager getInstance() {
        if(tokenManager == null) {
            tokenManager = new TokenManager();
        }
        return tokenManager;
    }

    private TokenManager() {
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
