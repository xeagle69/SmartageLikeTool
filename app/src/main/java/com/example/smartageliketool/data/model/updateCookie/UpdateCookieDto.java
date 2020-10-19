package com.example.smartageliketool.data.model.updateCookie;

import com.google.gson.annotations.SerializedName;

public class UpdateCookieDto {

    @SerializedName("igWwwClaim")
    private String igWwwClaim;

    @SerializedName("instagramAjax")
    private String instagramAjax;

    @SerializedName("csrfToken")
    private String csrfToken;

    @SerializedName("igAppId")
    private String igAppId;


    public UpdateCookieDto(String igWwwClaim, String instagramAjax, String csrfToken, String igAppId) {
        this.igWwwClaim = igWwwClaim;
        this.instagramAjax = instagramAjax;
        this.csrfToken = csrfToken;
        this.igAppId = igAppId;
    }


    public String getIgWwwClaim() {
        return igWwwClaim;
    }

    public void setIgWwwClaim(String igWwwClaim) {
        this.igWwwClaim = igWwwClaim;
    }

    public String getInstagramAjax() {
        return instagramAjax;
    }

    public void setInstagramAjax(String instagramAjax) {
        this.instagramAjax = instagramAjax;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public String getIgAppId() {
        return igAppId;
    }

    public void setIgAppId(String igAppId) {
        this.igAppId = igAppId;
    }
}
