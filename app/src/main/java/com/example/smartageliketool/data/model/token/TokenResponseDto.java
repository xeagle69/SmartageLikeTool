package com.example.smartageliketool.data.model.token;

import com.google.gson.annotations.SerializedName;

public class TokenResponseDto {

    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
