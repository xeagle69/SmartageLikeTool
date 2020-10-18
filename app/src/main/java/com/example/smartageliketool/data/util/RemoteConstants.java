package com.example.smartageliketool.data.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class RemoteConstants {

    public static String API_BASE = "https://nitrolike.instablizer.com/apiv1/";

    public static final String GET_TOKEN = "tokens";
    public static final String GET_POSTS = "cookie_posts";
    public static final String GET_COOKIE = "valid_cookies";


    public static boolean isRootGiven() {
        if (isRootAvailable()) {
            Process process = null;
            try {
                process = Runtime.getRuntime().exec(new String[]{"su", "-c", "id"});
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String output = in.readLine();
                if (output != null && output.toLowerCase().contains("uid=0"))
                    return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (process != null)
                    process.destroy();
            }
        }

        return false;
    }

    public static boolean isRootAvailable() {
        for (String pathDir : System.getenv("PATH").split(":")) {
            if (new File(pathDir, "su").exists()) {
                return true;
            }
        }
        return false;
    }

}
