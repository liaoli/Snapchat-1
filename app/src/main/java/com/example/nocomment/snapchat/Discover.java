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
import java.util.ArrayList;
import java.util.List;


public class Discover extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener{


    private ImageView backToStory;
    private RelativeLayout discoverLayout;
    private RecyclerView recyclerView;
    private TextView discoverTitle;

    private GestureDetector mGestureDetector;

    private int verticalMinDistance = 10;
    private int minVelocity = 0;

    private String url = "http://edition.cnn.com/2016/10/11/politics/donald-trump-republican-paul-ryan-2016-election/index.html";
    private String url2 = "http://www.mobile01.com";
    private ArrayList<String> bunchUrl = new ArrayList<String>();
    private ArrayList<String> imageUrlArray = new ArrayList<String>();
    private ArrayList<String> bunchTitle = new ArrayList<String>();


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

                bunchUrl.add("http://edition.cnn.com/2016/10/11/politics/donald-trump-republican-paul-ryan-2016-election/index.html");
                bunchUrl.add("http://edition.cnn.com/2016/10/12/politics/lavrov-russia-us-election/index.html");
                bunchUrl.add("http://edition.cnn.com/2016/10/11/arts/michael-wolf-photography/index.html");
                bunchUrl.add("http://edition.cnn.com/2016/10/10/politics/hillary-clinton-donald-trump-nasty-race/index.html");
                bunchUrl.add("http://edition.cnn.com/2016/10/10/arts/maurizio-savini-singapore-chewing-gum-art/index.html");
                bunchUrl.add("http://edition.cnn.com/2016/10/05/arts/stolen-dutch-artworks-westfries-museum/index.html");
                bunchUrl.add("http://money.cnn.com/2016/09/28/technology/volkswagen-id-electric/index.html");
                bunchUrl.add("http://edition.cnn.com/2016/10/03/arts/painting-bought-for-25-dollars-could-be-an-original-raphael/index.html");
                bunchUrl.add("http://edition.cnn.com/2016/10/03/design/former-jails-transformed/index.html");
                bunchUrl.add("http://edition.cnn.com/2016/10/03/autos/laferrari-aperta-paris-motor-show/index.html");
                bunchUrl.add("http://money.cnn.com/2016/10/11/news/economy/south-africa-pravin-gordhan-fraud/index.html");
                bunchUrl.add("http://edition.cnn.com/2016/10/08/politics/donald-trump-video-women-remarks-republicans/index.html");


                for(int i = 0; i < bunchUrl.size(); i++){
                    Document doc = Jsoup.connect(bunchUrl.get(i)).get();
                    Elements featureImage = doc.getElementsByTag("img");
                    int index = 0;
                    for (Element el : featureImage) {
                        //for each element  get the srs url
                        if(index == 3)
                            imageUrlArray.add(el.absUrl("src"));
                        index++;
                    }
                    String tempTitle = doc.title();
                    bunchTitle.add(tempTitle);

                }

//                Document doc = Jsoup.connect(url).get();
//
//
//                Elements featureImage = doc.getElementsByTag("img");
//                int index = 0;
//                for (Element el : featureImage) {
//                    //for each element  get the srs url
//
//                    if(index == 3)
//                        imgUrl= el.absUrl("src");
//
//                    index++;
//                }
//                title = doc.title();
            } catch (IOException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);

//            WebItem[] webItem = {
//                    new WebItem(title, imgUrl),
//                    new WebItem("Chat", imgUrl),
//                    new WebItem("Snap1", imgUrl)
////                    new WebItem("Chat1", imgUrl[2]),
////                    new WebItem("Chat2", imgUrl[3]),
////                    new WebItem("Snap2", imgUrl[4]),
////                    new WebItem("Chat2", imgUrl[5]),
////                    new WebItem("Snap2", imgUrl[6]),
////                    new WebItem("Chat2", imgUrl[7]),
//
//            };

            WebItem webItem0 = new WebItem(bunchTitle.get(0), imageUrlArray.get(0), bunchUrl.get(0));
            WebItem webItem1 = new WebItem(bunchTitle.get(1), imageUrlArray.get(1), bunchUrl.get(1));
            WebItem webItem2 = new WebItem(bunchTitle.get(2), imageUrlArray.get(2), bunchUrl.get(2));
            WebItem webItem3 = new WebItem(bunchTitle.get(3), imageUrlArray.get(3), bunchUrl.get(3));
            WebItem webItem4 = new WebItem(bunchTitle.get(4), imageUrlArray.get(4), bunchUrl.get(4));
            WebItem webItem5 = new WebItem(bunchTitle.get(5), imageUrlArray.get(5), bunchUrl.get(5));
            WebItem webItem6 = new WebItem(bunchTitle.get(6), imageUrlArray.get(6), bunchUrl.get(6));
            WebItem webItem7 = new WebItem(bunchTitle.get(7), imageUrlArray.get(7), bunchUrl.get(7));
            WebItem webItem8 = new WebItem(bunchTitle.get(8), imageUrlArray.get(8), bunchUrl.get(8));
            WebItem webItem9 = new WebItem(bunchTitle.get(9), imageUrlArray.get(9), bunchUrl.get(9));
            WebItem webItem10 = new WebItem(bunchTitle.get(10), imageUrlArray.get(10), bunchUrl.get(10));
            WebItem webItem11 = new WebItem(bunchTitle.get(11), imageUrlArray.get(11), bunchUrl.get(11));

            WebItem[] webItem = {
                    webItem0, webItem1, webItem2, webItem3, webItem4, webItem5, webItem6, webItem7,
                    webItem8, webItem9, webItem10, webItem11
            };

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
