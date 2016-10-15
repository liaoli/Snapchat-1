package com.example.nocomment.snapchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class CameraScreen extends AppCompatActivity {
    Button send;
    Button post;
    float x1, x2;
    float y1, y2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_screen);

        send = (Button)findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){

                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        String response =util.sendFriendRequest("a","b");

                    }
                }).start();
            }

        });
        post= (Button)findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {



            public void onClick(View v){
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent intent = new Intent(CameraScreen.this, Webview.class);
//                        startActivity(intent);
//
//                    }
//                }).start();
                Intent intent = new Intent(CameraScreen.this, Discover.class);
                        startActivity(intent);
            }

        });

    }   public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN: {
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            }
            case MotionEvent.ACTION_UP: {
                x2 = touchevent.getX();
                y2 = touchevent.getY();


                if (x1 < x2) {

                }
                if (x1 > x2) {



                }
                break;
            }
        }
        return false;
    }
}
