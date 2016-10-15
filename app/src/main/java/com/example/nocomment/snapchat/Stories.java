package com.example.nocomment.snapchat;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Stories extends AppCompatActivity{

    private ImageView backToChat;
    private ImageView storyGoToDcv;
    private ImageView topStory;
    private RelativeLayout storiesLayout;
    private StoryListViewHorizontal storyListView;
    private StoryListViewHorizontal friendStory;
    private StoryListViewHorizontalAdapter storyListViewAdapter;
    private StoryListViewHorizontalAdapter friendStoryAdapter;
    private RecyclerView recyclerView;
    private TextView liveText;

    private ArrayList<String> bunchUrl = new ArrayList<String>();
    private ArrayList<String> imageUrlArray = new ArrayList<String>();
    private ArrayList<String> bunchTitle = new ArrayList<String>();

    private ArrayList<String> bunchUrlLive = new ArrayList<String>();
    private ArrayList<String> imageUrlArrayLive = new ArrayList<String>();
    private ArrayList<String> bunchTitleLive = new ArrayList<String>();


    private ArrayList<String> bunchUrlDiscover = new ArrayList<String>();
    private ArrayList<String> imageUrlArrayDiscover = new ArrayList<String>();
    private ArrayList<String> bunchTitleDiscover = new ArrayList<String>();

    private final int MESSAGE_RETRIEVED = 0;
    private final int MESSAGE_RETRIEVED_SUBSCRIBED = 1;
    private final int MESSAGE_RETRIEVED_LIVE = 2;

    private WebItem[] webItem;

    private GestureDetector mGestureDetector;



    android.os.Handler handler = new android.os.Handler(new Handler.Callback() {

        public boolean handleMessage(Message message) {
            if (message.what==MESSAGE_RETRIEVED){
                // update UI
                try {
                    JSONObject jObject = new JSONObject(message.obj.toString());
                    JSONArray jArray = jObject.getJSONArray("stories");

                    if (jArray != null) {
                        for (int i=0;i<jArray.length();i++){
                            JSONObject tempObject = (JSONObject) jArray.get(i);
                            tempObject.get("user");
                            bunchTitle.add(tempObject.get("user").toString());
                            imageUrlArray.add(tempObject.get("imageUrl").toString());
                            bunchUrl.add(tempObject.get("imageUrl").toString());

                        }
                        WebItem[] webItem = new WebItem[100];
                        ArrayList<WebItem> webItemList= new ArrayList<WebItem>();
                        for(int i = 0; i < imageUrlArray.size(); i++){
                            webItemList.add(new WebItem(bunchTitle.get(i), imageUrlArray.get(i), bunchUrl.get(i)));
                        }

                        storyListViewAdapter = new StoryListViewHorizontalAdapter(Stories.this, webItemList, imageUrlArray.size());
                        storyListViewAdapter.notifyDataSetChanged();
                        storyListView.setAdapter(storyListViewAdapter);


                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (message.what==MESSAGE_RETRIEVED_SUBSCRIBED){
                try {
                    JSONObject jObject = new JSONObject(message.obj.toString());
                    JSONArray jArray = jObject.getJSONArray("discovery");

                    if (jArray != null) {
                        for (int i=0;i<jArray.length();i++){
                            JSONObject tempObject = (JSONObject) jArray.get(i);
                            tempObject.get("tagName");

                            JSONArray tempObjectArray = tempObject.getJSONArray("item");
                            for(int j = 0; j < tempObjectArray.length(); j++){
                                JSONObject imgUrl = (JSONObject) tempObjectArray.get(j);
                                bunchTitleDiscover.add(tempObject.get("tagName").toString());
                                imageUrlArrayDiscover.add(imgUrl.get("iconUrl").toString());
                                bunchUrlDiscover.add(imgUrl.get("url").toString());
                            }
                        }



                        ArrayList<WebItem> webItemList= new ArrayList<WebItem>();
                        for(int i = 0; i < imageUrlArrayDiscover.size(); i++){
                            webItemList.add(new WebItem(bunchTitleDiscover.get(i), imageUrlArrayDiscover.get(i), bunchUrlDiscover.get(i)));
                        }



                        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                        RecyclerViewAdapter recylerViewAdapter = new RecyclerViewAdapter(Stories.this, webItemList);
                        recyclerView.setAdapter(recylerViewAdapter);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                    }
//                    WebItem webItem2 = new WebItem(bunchTitle.get(2), imageUrlArray.get(2), bunchUrl.get(2));
//                    WebItem webItem3 = new WebItem(bunchTitle.get(3), imageUrlArray.get(3), bunchUrl.get(3));
//                    WebItem webItem4 = new WebItem(bunchTitle.get(4), imageUrlArray.get(4), bunchUrl.get(4));
//                    WebItem webItem5 = new WebItem(bunchTitle.get(5), imageUrlArray.get(5), bunchUrl.get(5));
//                    WebItem webItem6 = new WebItem(bunchTitle.get(6), imageUrlArray.get(6), bunchUrl.get(6));
//                    WebItem webItem7 = new WebItem(bunchTitle.get(7), imageUrlArray.get(7), bunchUrl.get(7));
//                    WebItem webItem8 = new WebItem(bunchTitle.get(8), imageUrlArray.get(8), bunchUrl.get(8));
//                    WebItem webItem9 = new WebItem(bunchTitle.get(9), imageUrlArray.get(9), bunchUrl.get(9));
//                    WebItem webItem10 = new WebItem(bunchTitle.get(10), imageUrlArray.get(10), bunchUrl.get(10));
//                    WebItem webItem11 = new WebItem(bunchTitle.get(11), imageUrlArray.get(11), bunchUrl.get(11));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (message.what==MESSAGE_RETRIEVED_LIVE){
                try {
                    JSONObject jObject = new JSONObject(message.obj.toString());
                    JSONArray jArray = jObject.getJSONArray("stories");

                    if (jArray != null) {
                        for (int i=0;i<jArray.length();i++){
                            JSONObject tempObject = (JSONObject) jArray.get(i);
                            tempObject.get("user");
                            bunchTitleLive.add(tempObject.get("user").toString());
                            imageUrlArrayLive.add(tempObject.get("imageUrl").toString());
                            bunchUrlLive.add(tempObject.get("imageUrl").toString());

                        }

                        ArrayList<WebItem> webItemList= new ArrayList<WebItem>();
                        for(int i = 0; i < imageUrlArrayLive.size(); i++){
                            webItemList.add(new WebItem(bunchTitleLive.get(i),
                                    imageUrlArrayLive.get(i),
                                    bunchUrlLive.get(i)));
                        }

                        friendStoryAdapter = new StoryListViewHorizontalAdapter(Stories.this,
                                webItemList,
                                imageUrlArrayLive.size());
                        friendStoryAdapter.notifyDataSetChanged();
                        friendStory.setAdapter(friendStoryAdapter);

                    }
                    liveText.setVisibility(View.VISIBLE);
                    friendStory.setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_stories);


//        mGestureDetector = new GestureDetector(this, mOnGesture);

        backToChat = (ImageView) findViewById(R.id.storyBackToCma);
        storiesLayout = (RelativeLayout) findViewById(R.id.storiesLayout);
        storyGoToDcv = (ImageView) findViewById(R.id.storyGoToDcv);

        liveText = (TextView) findViewById(R.id.liveText);
//
//        storiesLayout.setOnTouchListener(this);
        storyListView = (StoryListViewHorizontal) findViewById(R.id.storyList);
        friendStory = (StoryListViewHorizontal) findViewById(R.id.friendStory);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewStories);

//        friendStoryAdapter = new StoryListViewHorizontalAdapter(this);
//        friendStoryAdapter.notifyDataSetChanged();
//        friendStory.setAdapter(friendStoryAdapter);


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

        new Thread(new Runnable() {
            @Override
            public void run() {
                String loginId = getLoggedInUserId();
                String discovery = Util.getStories(loginId);
                ArrayList<String> discoveryList = new ArrayList<String>();
                discoveryList.add(discovery);
                Message message = new Message();
                message.what = MESSAGE_RETRIEVED;
                message.obj = discovery;
                handler.sendMessage(message);

                String discoverySubscribed = Util.getSubscribeDiscovery(loginId);
                ArrayList<String> discoverySubscribedList = new ArrayList<String>();
                discoverySubscribedList.add(discovery);
                Message messageSubscribed = new Message();
                messageSubscribed.what = MESSAGE_RETRIEVED_SUBSCRIBED;
                messageSubscribed.obj = discoverySubscribed;
                handler.sendMessage(messageSubscribed);

                String storiesLive = Util.getStories(loginId);
                ArrayList<String> storiesLiveList = new ArrayList<String>();
                storiesLiveList.add(storiesLive);
                Message messageLive = new Message();
                messageLive.what = MESSAGE_RETRIEVED_LIVE;
                messageLive.obj = storiesLive;
                handler.sendMessage(messageLive);



            }
        }).start();

    }

    private String getLoggedInUserId () {
        String loggedInUser = "";

        if (Login.getLoggedinUserId() == "") {
            FileInputStream fis = null;

            try {
                fis = getApplicationContext().openFileInput("user");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                loggedInUser = bufferedReader.readLine();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            loggedInUser = Login.getLoggedinUserId();
        }


        return loggedInUser;

    }

}