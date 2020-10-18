
package com.example.smartageliketool.data.model.instapost.innerclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Graphql {

    @SerializedName("shortcode_media")
    @Expose
    private ShortcodeMedia shortcodeMedia;

    public ShortcodeMedia getShortcodeMedia() {
        return shortcodeMedia;
    }

    public void setShortcodeMedia(ShortcodeMedia shortcodeMedia) {
        this.shortcodeMedia = shortcodeMedia;
    }

}
