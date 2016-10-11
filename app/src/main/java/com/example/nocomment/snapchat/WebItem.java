package com.example.nocomment.snapchat;

/**
 * Created by guomingsun on 11/10/16.
 */

public class WebItem {

    private String webTitle;
    private String webUrl;


    public WebItem(String webTitle, String webUrl) {
        this.webTitle = webTitle;
        this.webUrl = webUrl;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
