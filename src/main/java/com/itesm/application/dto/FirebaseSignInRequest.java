package com.itesm.application.dto;

public class FirebaseSignInRequest {
    private String email;
    private String password;
    private boolean returnSecureToken;

    public FirebaseSignInRequest() {}

    public FirebaseSignInRequest(String email, String password, boolean returnSecureToken) {
        this.email = email;
        this.password = password;
        this.returnSecureToken = returnSecureToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isReturnSecureToken() {
        return returnSecureToken;
    }

    public void setReturnSecureToken(boolean returnSecureToken) {
        this.returnSecureToken = returnSecureToken;
    }
}

