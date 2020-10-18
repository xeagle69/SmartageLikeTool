
package com.example.smartageliketool.data.model.instapost.innerclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EdgeFollowedBy {

    @SerializedName("count")
    @Expose
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
