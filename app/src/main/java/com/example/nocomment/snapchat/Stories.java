package com.example.nocomment.snapchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Stories extends AppCompatActivity{

    private ImageView backToChat;
    private ImageView storyGoToDcv;
    private ImageView topStory;
    private RelativeLayout storiesLayout;
    private StoryListViewHorizontal storyListView;
    private StoryListViewHorizontal friendStory;
    private StoryListViewHorizontalAdapter storyListViewAdapter;
    private StoryListViewHorizontalAdapter friendStoryAdapter;

    private GestureDetector mGestureDetector;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);


//        mGestureDetector = new GestureDetector(this, mOnGesture);

        backToChat = (ImageView) findViewById(R.id.storyBackToCma);
        storiesLayout = (RelativeLayout) findViewById(R.id.storiesLayout);
        storyGoToDcv = (ImageView) findViewById(R.id.storyGoToDcv);
        topStory = (ImageView) findViewById(R.id.topStory);
//
//        storiesLayout.setOnTouchListener(this);
        storyListView = (StoryListViewHorizontal) findViewById(R.id.storyList);
        friendStory = (StoryListViewHorizontal) findViewById(R.id.friendStory);
        storyListViewAdapter = new StoryListViewHorizontalAdapter(this);
        storyListViewAdapter.notifyDataSetChanged();
        storyListView.setAdapter(storyListViewAdapter);

        friendStoryAdapter = new StoryListViewHorizontalAdapter(this);
        friendStoryAdapter.notifyDataSetChanged();
        friendStory.setAdapter(friendStoryAdapter);


        backToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Stories.this, CameraView.class);
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

}