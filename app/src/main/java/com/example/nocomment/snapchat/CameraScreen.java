package com.example.nocomment.snapchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class CameraScreen extends AppCompatActivity {
    Button send;
    Button post;
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
                        String response =util.sendFriendRequest("a","b");

                    }
                }).start();
            }

        });
        post= (Button)findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {



            public void onClick(View v){

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String response =util.getUsers("a");

                    }
                }).start();
            }

        });
    }
}
