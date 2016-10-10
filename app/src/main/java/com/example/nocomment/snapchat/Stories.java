package com.example.nocomment.snapchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Stories extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener{

    private ImageView backToChat;
    private ImageView storyGoToDcv;
    private RelativeLayout storiesLayout;
    private StoryListViewHorizontal storyListView;
    private StoryListViewHorizontalAdapter storyListViewAdapter;

    private GestureDetector mGestureDetector;

    private int verticalMinDistance = 10;
    private int minVelocity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);


        mGestureDetector = new GestureDetector(this);

        backToChat = (ImageView) findViewById(R.id.storyBackToCma);
        storiesLayout = (RelativeLayout) findViewById(R.id.storiesLayout);
        storyGoToDcv = (ImageView) findViewById(R.id.storyGoToDcv);

        storiesLayout.setOnTouchListener(this);
        storyListView = (StoryListViewHorizontal) findViewById(R.id.storyList);
        storyListViewAdapter = new StoryListViewHorizontalAdapter(this);
        storyListViewAdapter.notifyDataSetChanged();
        storyListView.setAdapter(storyListViewAdapter);

        backToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Stories.this, ChatList.class);
                startActivity(intent);
                overridePendingTransition(R.anim.from_left, R.anim.to_right);
                Stories.this.finish();
            }
        });

        storyGoToDcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Stories.this, Discover.class);
                startActivity(intent);
                overridePendingTransition(R.anim.from_right, R.anim.to_left);
                Stories.this.finish();
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
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
            Intent intent = new Intent();
            intent.setClass(Stories.this, Discover.class);
            startActivity(intent);
            overridePendingTransition(R.anim.from_right, R.anim.to_left);
            Stories.this.finish();

        } else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
            Intent intent = new Intent();
            intent.setClass(Stories.this, ChatList.class);
            startActivity(intent);
            overridePendingTransition(R.anim.from_left, R.anim.to_right);
            Stories.this.finish();

        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return mGestureDetector.onTouchEvent(me);
    }
}



