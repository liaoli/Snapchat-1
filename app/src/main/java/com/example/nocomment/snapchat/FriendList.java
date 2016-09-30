package com.example.nocomment.snapchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by guomingsun on 30/9/16.
 */

public class FriendList extends AppCompatActivity{


    private ListView friendList;
    private ImageView backToChatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_friend_list);

        friendList = (ListView) findViewById(R.id.chatFriendList);
        backToChatList = (ImageView) findViewById(R.id.backToChatList);

        backToChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FriendList.this, ChatList.class);
                startActivity(intent);
                FriendList.this.finish();
            }
        });

    }
}
