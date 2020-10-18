
package com.example.smartageliketool.data.model.instapost;

import com.example.smartageliketool.data.model.instapost.innerclasses.Graphql;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InstaPostResponse {

    @SerializedName("graphql")
    @Expose
    private Graphql graphql;

    public Graphql getGraphql() {
        return graphql;
    }

    public void setGraphql(Graphql graphql) {
        this.graphql = graphql;
    }

}
