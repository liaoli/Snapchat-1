package com.example.nocomment.snapchat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by guomingsun on 15/10/16.
 */

public class ImageViewWindows extends AppCompatActivity {

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageview);

        imageView = (ImageView) findViewById(R.id.imageViewWindow);

        Intent intent = getIntent();
        String data = intent.getStringExtra("url");
        Picasso.with(this.getApplicationContext())
                .load(data)
                .into(imageView);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 5000);


    }
}
