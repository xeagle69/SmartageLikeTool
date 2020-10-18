
package com.example.smartageliketool.data.model.postList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostEntity {

    @SerializedName("type_")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("mediaId")
    @Expose
    private String mediaId;
    @SerializedName("ownerId")
    @Expose
    private String ownerId;
    @SerializedName("ownerUsername")
    @Expose
    private String ownerUsername;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("isVideo")
    @Expose
    private Boolean isVideo;
    @SerializedName("publishDate")
    @Expose
    private String publishDate;
    @SerializedName("requestedLikes")
    @Expose
    private Integer requestedLikes;
    @SerializedName("initialLikes")
    @Expose
    private Integer initialLikes;
    @SerializedName("finishDate")
    @Expose
    private Object finishDate;
    @SerializedName("userHash")
    @Expose
    private Object userHash;
    @SerializedName("totalLikes")
    @Expose
    private Integer totalLikes;
    @SerializedName("totalReports")
    @Expose
    private Integer totalReports;
    @SerializedName("totalIgnores")
    @Expose
    private Integer totalIgnores;
    @SerializedName("likedByMe")
    @Expose
    private Boolean likedByMe;
    @SerializedName("ignoredByMe")
    @Expose
    private Boolean ignoredByMe;
    @SerializedName("reportedByMe")
    @Expose
    private Boolean reportedByMe;
    @SerializedName("remainingLikes")
    @Expose
    private Integer remainingLikes;
    @SerializedName("isDone")
    @Expose
    private Boolean isDone;
    @SerializedName("isBlocked")
    @Expose
    private Boolean isBlocked;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("activatedAt")
    @Expose
    private String activatedAt;
    @SerializedName("modifiedAt")
    @Expose
    private Object modifiedAt;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("accountId")
    @Expose
    private Integer accountId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(Boolean isVideo) {
        this.isVideo = isVideo;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public Integer getRequestedLikes() {
        return requestedLikes;
    }

    public void setRequestedLikes(Integer requestedLikes) {
        this.requestedLikes = requestedLikes;
    }

    public Integer getInitialLikes() {
        return initialLikes;
    }

    public void setInitialLikes(Integer initialLikes) {
        this.initialLikes = initialLikes;
    }

    public Object getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Object finishDate) {
        this.finishDate = finishDate;
    }

    public Object getUserHash() {
        return userHash;
    }

    public void setUserHash(Object userHash) {
        this.userHash = userHash;
    }

    public Integer getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(Integer totalLikes) {
        this.totalLikes = totalLikes;
    }

    public Integer getTotalReports() {
        return totalReports;
    }

    public void setTotalReports(Integer totalReports) {
        this.totalReports = totalReports;
    }

    public Integer getTotalIgnores() {
        return totalIgnores;
    }

    public void setTotalIgnores(Integer totalIgnores) {
        this.totalIgnores = totalIgnores;
    }

    public Boolean getLikedByMe() {
        return likedByMe;
    }

    public void setLikedByMe(Boolean likedByMe) {
        this.likedByMe = likedByMe;
    }

    public Boolean getIgnoredByMe() {
        return ignoredByMe;
    }

    public void setIgnoredByMe(Boolean ignoredByMe) {
        this.ignoredByMe = ignoredByMe;
    }

    public Boolean getReportedByMe() {
        return reportedByMe;
    }

    public void setReportedByMe(Boolean reportedByMe) {
        this.reportedByMe = reportedByMe;
    }

    public Integer getRemainingLikes() {
        return remainingLikes;
    }

    public void setRemainingLikes(Integer remainingLikes) {
        this.remainingLikes = remainingLikes;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }

    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(String activatedAt) {
        this.activatedAt = activatedAt;
    }

    public Object getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Object modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

}
