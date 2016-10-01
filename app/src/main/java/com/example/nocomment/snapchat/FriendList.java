package com.example.nocomment.snapchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by guomingsun on 30/9/16.
 */

public class FriendList extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener{


    private ListView friendList;
    private ImageView backToChatList;

    private GestureDetector mGestureDetector;
    private RelativeLayout friendLayout;

    private int verticalMinDistance = 10;
    private int minVelocity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_friend_list);

        friendList = (ListView) findViewById(R.id.chatFriendList);
        backToChatList = (ImageView) findViewById(R.id.backToChatList);
        friendLayout = (RelativeLayout) findViewById(R.id.friendLayout);

        mGestureDetector = new GestureDetector(this);

        friendLayout.setOnTouchListener(this);

        backToChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FriendList.this, ChatList.class);
                startActivity(intent);
                overridePendingTransition(R.anim.from_right, R.anim.to_left);
                FriendList.this.finish();
            }
        });

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return mGestureDetector.onTouchEvent(me);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
            Intent intent = new Intent();
            intent.setClass(FriendList.this, ChatList.class);
            startActivity(intent);
            overridePendingTransition(R.anim.from_right, R.anim.to_left);
            FriendList.this.finish();

        } else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {

        }

        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
