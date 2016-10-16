package com.example.nocomment.snapchat;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by guomingsun on 16/10/16.
 */

public class ImageViewBitmapWindows extends AppCompatActivity {

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageview);

        imageView = (ImageView) findViewById(R.id.imageViewWindow);

        Intent intent = getIntent();
        String data = intent.getStringExtra("url");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 10;
        imageView.setImageBitmap(BitmapFactory.decodeFile(data,options));

    }
}
