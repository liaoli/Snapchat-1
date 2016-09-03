package com.example.nocomment.snapchat;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

public class ChatList extends AppCompatActivity {

    ImageView addNewChat;
    ImageView backToChatList;
    ImageView goToCma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        addNewChat = (ImageView) findViewById(R.id.crtNewChat);
        goToCma = (ImageView) findViewById(R.id.backToCam);
        addNewChat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                jumpToFriendList();
            }

        });

        // supposed to go to camera
        // since camera has not been finished, i set it go to story
        goToCma.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChatList.this, Stories.class);
                startActivity(intent);
                ChatList.this.finish();
            }
        });


        final String [] friends={"Rose","Jack"};

    }

    public void jumpToFriendList(){
        setContentView(R.layout.chat_friend_list);
        backToChatList = (ImageView) findViewById(R.id.backToChatList);
        backToChatList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                jumpBackToChatList();
            }

        });

    }

    public void jumpBackToChatList(){
        setContentView(R.layout.activity_chat_list);
        addNewChat = (ImageView) findViewById(R.id.crtNewChat);
        addNewChat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                jumpToFriendList();
            }

        });
    }


}
