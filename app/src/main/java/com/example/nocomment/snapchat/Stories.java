package com.example.nocomment.snapchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Stories extends AppCompatActivity {

    private ImageView backToChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);

        backToChat = (ImageView) findViewById(R.id.storyBackToCma);

        backToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Stories.this, ChatList.class);
                startActivity(intent);
                Stories.this.finish();
            }
        });
    }
}



