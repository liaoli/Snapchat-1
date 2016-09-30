package com.example.nocomment.snapchat;

import android.content.Intent;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class ChatList extends AppCompatActivity {

    private ImageView addNewChat;
    private ImageView goToCma;
    private TextView chat;

    // test user
    private String user1 = "Jason", user2 = "Park";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);


        addNewChat = (ImageView) findViewById(R.id.crtNewChat);
        goToCma = (ImageView) findViewById(R.id.backToCam);
        chat = (TextView) findViewById(R.id.chatTitle);


        addNewChat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ChatList.this, FriendList.class);
                startActivity(intent);
                ChatList.this.finish();
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



        chat.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChatList.this, ChatScreen.class);
                startActivity(intent);
                ChatList.this.finish();
            }
        });

    }




}
