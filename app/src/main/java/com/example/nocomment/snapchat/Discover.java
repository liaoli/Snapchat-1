package com.example.nocomment.snapchat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Discover extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener{


    private ImageView backToStory;
    private RelativeLayout discoverLayout;
    private RecyclerView recyclerView;
    private TextView discoverTitle;

    private GestureDetector mGestureDetector;

    private int verticalMinDistance = 150;
    private int minVelocity = 0;

    private String url = "http://edition.cnn.com/2016/10/11/politics/donald-trump-republican-paul-ryan-2016-election/index.html";
    private String url2 = "http://www.mobile01.com";
    private ArrayList<String> bunchUrl = new ArrayList<String>();
    private ArrayList<String> imageUrlArray = new ArrayList<String>();
    private ArrayList<String> bunchTitle = new ArrayList<String>();
    private final int MESSAGE_RETRIEVED = 0;

    String title;
    String imgUrl;


    android.os.Handler handler = new android.os.Handler(new Handler.Callback() {

        public boolean handleMessage(Message message) {
            if (message.what==MESSAGE_RETRIEVED){
                // update UI
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
                                bunchTitle.add(tempObject.get("tagName").toString());
                                imageUrlArray.add(imgUrl.get("iconUrl").toString());
                                bunchUrl.add(imgUrl.get("url").toString());
                            }
                        }

//                        WebItem[] webItem = new WebItem[100];
                        ArrayList<WebItem> webItemList= new ArrayList<WebItem>();
                        for(int i = 0; i < imageUrlArray.size(); i++){
                            webItemList.add(new WebItem(bunchTitle.get(i), imageUrlArray.get(i), bunchUrl.get(i)));
                        }

                        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                        RecyclerViewAdapter recylerViewAdapter = new RecyclerViewAdapter(Discover.this, webItemList);
                        recyclerView.setAdapter(recylerViewAdapter);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                    }

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
        setContentView(R.layout.activity_discover);

        discoverLayout = (RelativeLayout) findViewById(R.id.discoverLayout);
        backToStory = (ImageView) findViewById(R.id.backToStories);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        discoverTitle = (TextView) findViewById(R.id.discoverTitle);
        mGestureDetector = new GestureDetector(this);

        recyclerView.setOnTouchListener(new OnSwipeTouchListener(this) {

            public void onSwipeRight() {
                Intent intent = new Intent();
                intent.setClass(Discover.this, Stories.class);
                startActivity(intent);
                overridePendingTransition(R.anim.from_left, R.anim.to_right);
                Discover.this.finish();
            }
        });

        discoverLayout.setOnTouchListener(this);



        backToStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Discover.this, Stories.class);
                startActivity(intent);
                overridePendingTransition(R.anim.from_left, R.anim.to_right);
                Discover.this.finish();

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                String discovery = Util.getDiscovery();
                ArrayList<String> discoveryList = new ArrayList<String>();
                discoveryList.add(discovery);
                discoveryList.add("bye bye");
                Message message = new Message();
                message.what = MESSAGE_RETRIEVED;
                message.obj = discovery;
                handler.sendMessage(message);

            }
        }).start();


    }

    private static String getImageFromLinkRel(Document document) {
        Element link = document.select("link[rel=image_src]").first();
        if (link != null) {
            return link.attr("abs:href");
        }
        return null;
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


        } else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {

            Intent intent = new Intent();
            intent.setClass(Discover.this, Stories.class);
            startActivity(intent);
            overridePendingTransition(R.anim.from_left, R.anim.to_right);
            Discover.this.finish();

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





