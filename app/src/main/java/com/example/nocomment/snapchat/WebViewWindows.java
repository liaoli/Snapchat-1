package com.example.nocomment.snapchat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by guoming on 10/15/2016.
 */

public class WebViewWindows extends AppCompatActivity{

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        webView = (WebView) findViewById(R.id.webViewWindows);

        Intent intent = getIntent();
        String data = intent.getStringExtra("url");
        webView.loadUrl(data);



    }
}
