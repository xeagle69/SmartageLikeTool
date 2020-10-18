package com.example.smartageliketool.data.model.post;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "post_table")
public class PostDataBaseEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "actual_id")
    private int actualId;


    @ColumnInfo(name = "link")
    private String link;


    @ColumnInfo(name = "mediaId")
    private String mediaId;


    @ColumnInfo(name = "is_liked")
    private boolean isLike;


    @ColumnInfo(name = "is_liked_with_cookie")
    private boolean isLikeWithCoockie;


    public PostDataBaseEntity(int id, int actualId, String link, String mediaId, boolean isLike, boolean isLikeWithCoockie) {
        this.id = id;
        this.actualId = actualId;
        this.link = link;
        this.mediaId = mediaId;
        this.isLike = isLike;
        this.isLikeWithCoockie = isLikeWithCoockie;
    }

    @Ignore
    public PostDataBaseEntity(int actualId, String link,String mediaId, boolean isLike, boolean isLikeWithCoockie) {
        this.actualId = actualId;
        this.link = link;
        this.mediaId = mediaId;
        this.isLike = isLike;
        this.isLikeWithCoockie = isLikeWithCoockie;

    }


    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public boolean isLikeWithCoockie() {
        return isLikeWithCoockie;
    }

    public void setLikeWithCoockie(boolean likeWithCoockie) {
        isLikeWithCoockie = likeWithCoockie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActualId() {
        return actualId;
    }

    public void setActualId(int actualId) {
        this.actualId = actualId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
}
