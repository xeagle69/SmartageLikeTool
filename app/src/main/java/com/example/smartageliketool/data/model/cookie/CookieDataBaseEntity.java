package com.example.smartageliketool.data.model.cookie;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "cookie_table")
public class CookieDataBaseEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "actual_id")
    private int actualId;


    @ColumnInfo(name = "cookie_value")
    private String cookie;


    @ColumnInfo(name = "user_agent")
    private String userAgent;


    public CookieDataBaseEntity(int id, int actualId, String cookie, String userAgent) {
        this.id = id;
        this.actualId = actualId;
        this.cookie = cookie;
        this.userAgent = userAgent;
    }


    @Ignore
    public CookieDataBaseEntity(int actualId, String cookie, String userAgent) {
        this.actualId = actualId;
        this.cookie = cookie;
        this.userAgent = userAgent;
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
}
