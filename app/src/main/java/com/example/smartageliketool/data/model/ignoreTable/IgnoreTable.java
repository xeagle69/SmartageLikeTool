package com.example.smartageliketool.data.model.ignoreTable;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "ignore_table")
public class IgnoreTable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "cookie_id")
    private int cookieId;

    @ColumnInfo(name = "post_id")
    private int postId;


    public IgnoreTable(int id, int cookieId, int postId) {
        this.id = id;
        this.cookieId = cookieId;
        this.postId = postId;
    }

    @Ignore
    public IgnoreTable(int cookieId, int postId) {
        this.cookieId = cookieId;
        this.postId = postId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCookieId() {
        return cookieId;
    }

    public void setCookieId(int cookieId) {
        this.cookieId = cookieId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
