package com.example.nocomment.snapchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import java.util.ArrayList;

public class Webview extends AppCompatActivity {

    float x1, x2;
    float y1, y2;
    ArrayList<String> stringList = new ArrayList<>();

    WebView myWebView;
    int index = 0;
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
//        Context context = getApplicationContext();
//        imageView=(ImageView)findViewById(R.id.imageViewWeb);
//        Picasso.with(context).load("https://developer.android.com/guide/webapps/webview.html").into(imageView);

        myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        stringList.add("http://edition.cnn.com/2016/10/05/arts/stolen-dutch-artworks-westfries-museum/index.html");
        stringList.add("https://developer.android.com/guide/webapps/webview.html");

        myWebView.loadUrl(stringList.get(index));
//
//
//        SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
//        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Insert your code here
////                myWebView.loadUrl("https://developer.android.com/guide/webapps/webview.html"); // refreshes the WebView
//            }
//        });
    }


//    public boolean onTouchEvent(MotionEvent touchevent) {
//        switch (touchevent.getAction()) {
//            // when user first touches the screen we get x and y coordinate
//            case MotionEvent.ACTION_DOWN: {
//                x1 = touchevent.getX();
//                y1 = touchevent.getY();
//                break;
//            }
//            case MotionEvent.ACTION_UP: {
//                x2 = touchevent.getX();
//                y2 = touchevent.getY();
//
//
//                if (x1 < x2) {
//                    if (stringList.size() <= index - 1)
//                        index += 1;
//                    myWebView.loadUrl(stringList.get(index));
//                }
//                if (x1 > x2) {
//                    if (index >= 0) {
//                        index -= 1;
//                        myWebView.loadUrl(stringList.get(index));
//                    }
//
//
//                }
//                break;
//            }
//        }
//        return false;
//    }
}
