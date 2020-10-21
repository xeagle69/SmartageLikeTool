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
//***************************************************************************************************

    public void saveToken(String token) {
        prefs.edit().putString("token", token).apply();
    }


    public String loadToken() {
        return prefs.getString("token", "").trim();
    }


//***************************************************************************************************

    public void saveIgClaim(String igClaim) {
        prefs.edit().putString("ig_claim", igClaim).apply();
    }

    public String loadIgClaim() {
        return prefs.getString("ig_claim", "").trim();
    }

    //***************************************************************************************************

    public void saveInstagramAjax(String instagramAjax) {
        prefs.edit().putString("instagram_ajax", instagramAjax).apply();
    }

    public String loadInstagramAjax() {
        return prefs.getString("instagram_ajax", "").trim();
    }

    //***************************************************************************************************


    public void saveCfrtoken(String cfrToken) {
        prefs.edit().putString("cfr_token", cfrToken).apply();
    }

    public String loadCfrtoken() {
        return prefs.getString("cfr_token", "").trim();
    }

    //***************************************************************************************************

    public void saveInstagramAppId(String instagramAppId) {
        prefs.edit().putString("instagram_app_id", instagramAppId).apply();
    }

    public String loadInstagramAppId() {
        return prefs.getString("instagram_app_id", "").trim();
    }

    //***************************************************************************************************


    public void saveLikeCount(Integer likeCount) {
        prefs.edit().putInt("like_count", likeCount).apply();
    }

    public Integer loadLikeCount() {
        return prefs.getInt("like_count", 0);
    }

    //***************************************************************************************************


}
