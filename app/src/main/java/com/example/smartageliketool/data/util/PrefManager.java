package com.example.smartageliketool.data.util;

import android.content.SharedPreferences;

import com.google.gson.Gson;


public class PrefManager {
    private SharedPreferences prefs;
    private Gson gson;


    public PrefManager(SharedPreferences prefs) {
        this.prefs = prefs;
        gson = new Gson();

    }


    public void saveToken(String token) {
        prefs.edit().putString("token", token).apply();
    }

    public String loadToken() {
        return prefs.getString("token", "").trim();
    }


}
