
package com.example.smartageliketool.data.model.cookie;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCookieResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cookie")
    @Expose
    private String cookie;
    @SerializedName("userAgent")
    @Expose
    private String userAgent;
    @SerializedName("isUsed")
    @Expose
    private Boolean isUsed;
    @SerializedName("igWwwClaim")
    @Expose
    private String igWwwClaim;
    @SerializedName("instagramAjax")
    @Expose
    private String instagramAjax;
    @SerializedName("csrfToken")
    @Expose
    private String csrfToken;
    @SerializedName("igAppId")
    @Expose
    private String igAppId;
    @SerializedName("accountId")
    @Expose
    private Integer accountId;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
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

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
