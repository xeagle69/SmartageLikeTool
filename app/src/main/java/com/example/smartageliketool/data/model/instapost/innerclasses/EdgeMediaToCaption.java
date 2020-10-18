
package com.example.smartageliketool.data.model.instapost.innerclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EdgeMediaToCaption {

    @SerializedName("edges")
    @Expose
    private List<Object> edges = null;

    public List<Object> getEdges() {
        return edges;
    }

    public void setEdges(List<Object> edges) {
        this.edges = edges;
    }

}
