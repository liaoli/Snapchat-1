package com.example.nocomment.snapchat;

/**
 * Created by guomingsun on 11/10/16.
 */

public class WebItem {

    private String webTitle;
    private String webUrl;
    private String webViewUrl;


    public WebItem(String webTitle, String webUrl, String webViewUrl) {
        this.webTitle = webTitle;
        this.webUrl = webUrl;
        this.webViewUrl = webViewUrl;
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

    public String getWebViewUrl() {
        return webViewUrl;
    }

    public void setWebViewUrl(String webViewUrl) {
        this.webViewUrl = webViewUrl;
    }
}