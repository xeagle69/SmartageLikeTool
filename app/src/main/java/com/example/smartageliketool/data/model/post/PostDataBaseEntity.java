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

    @Ignore
    public PostDataBaseEntity() {
    }

    public PostDataBaseEntity(int id, int actualId, String link, String mediaId) {
        this.id = id;
        this.actualId = actualId;
        this.link = link;
        this.mediaId = mediaId;


    }

    @Ignore
    public PostDataBaseEntity(int actualId, String link, String mediaId) {
        this.actualId = actualId;
        this.link = link;
        this.mediaId = mediaId;


    }


    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
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


}
