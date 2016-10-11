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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class Discover extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener{


    private ImageView backToStory;
    private RelativeLayout discoverLayout;
    private RecyclerView recyclerView;
    private TextView discoverTitle;

    private GestureDetector mGestureDetector;

    private int verticalMinDistance = 10;
    private int minVelocity = 0;

    private String url = "http://edition.cnn.com";
    private String url2 = "http://www.mobile01.com";

    private String source[] = {
            "https://www.google.com.au/imgres?imgurl=http%3A%2F%2Fwww.halloweenradio.net%2Fimages%2Flayout%2Fsnapcode.png&imgrefurl=http%3A%2F%2Fwww.halloweenradio.net%2Fsnapchat.php&docid=x7kaJi9UA0bwjM&tbnid=jLydJaD1fbjN1M%3A&w=1024&h=1024&client=safari&bih=917&biw=1751&ved=0ahUKEwjbsYytqtLPAhVHXD4KHf8NBOgQMwgzKAMwAw&iact=mrc&uact=8",
            "https://www.google.com.au/imgres?imgurl=http%3A%2F%2Fi2.cdn.turner.com%2Fmoney%2Fdam%2Fassets%2F160329142702-snapchat-maturing-780x439.jpg&imgrefurl=http%3A%2F%2Fmoney.cnn.com%2F2016%2F03%2F29%2Ftechnology%2Fsnapchat%2F&docid=Rf2yJdlTyM_nRM&tbnid=d2eqaRRsG87TrM%3A&w=780&h=439&client=safari&bih=917&biw=1751&ved=0ahUKEwjbsYytqtLPAhVHXD4KHf8NBOgQMwhsKDQwNA&iact=mrc&uact=8"
    };

    String title;
    String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        discoverLayout = (RelativeLayout) findViewById(R.id.discoverLayout);
        backToStory = (ImageView) findViewById(R.id.backToStories);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        discoverTitle = (TextView) findViewById(R.id.discoverTitle);
        mGestureDetector = new GestureDetector(this);

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

        new Thread(runnable).start();


    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Document doc = Jsoup.connect(url).get();


                Elements featureImage = doc.getElementsByTag("img");
                int index = 0;
                for (Element el : featureImage) {
                    //for each element  get the srs url
                    if(index == 0){
                        imgUrl = el.absUrl("src");
                    }



                }
                title = doc.title();
//                imgUrl = getImageFromLinkRel(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(5);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);

            WebItem[] webItem = {
                    new WebItem("Snap", source[0]),
                    new WebItem("Chat", source[1]),
                    new WebItem("Snap1", imgUrl),
                    new WebItem("Chat1", imgUrl),
                    new WebItem("Chat2", imgUrl),
                    new WebItem("Snap2", imgUrl),
                    new WebItem("Chat2", imgUrl),
                    new WebItem("Snap2", imgUrl),
                    new WebItem("Chat2", imgUrl),
                    new WebItem("Snap2", imgUrl),
                    new WebItem("Chat2", imgUrl),
                    new WebItem("Snap2", imgUrl),
                    new WebItem("Chat2", imgUrl),
                    new WebItem("Snap2", imgUrl),

            };

            webItem[0].setWebTitle(title);
            webItem[0].setWebUrl(imgUrl);
            webItem[1].setWebUrl(imgUrl);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            RecylerViewAdapter recylerViewAdapter = new RecylerViewAdapter(Discover.this, webItem);
            recyclerView.setAdapter(recylerViewAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());



        }
    };

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
