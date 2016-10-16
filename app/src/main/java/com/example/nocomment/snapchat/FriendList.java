package com.example.nocomment.snapchat;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.R.attr.onClick;

/**
 * Created by guomingsun on 30/9/16.
 */

public class FriendList extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener{


    private ListView friendList;
    private ImageView backToChatList;
    private final int MESSAGE_RETRIEVED = 0;
    ArrayAdapter<String> frdAdapter;

    private GestureDetector mGestureDetector;
    private RelativeLayout friendLayout;

    private int verticalMinDistance = 10;
    private int minVelocity = 0;
    private int topVisiblePosition = -1;
    private TextView topHeader;

    private ArrayList<String> listdata;


    android.os.Handler handler = new android.os.Handler(new Handler.Callback() {

        public boolean handleMessage(Message message) {
            if (message.what==MESSAGE_RETRIEVED){
                // update UI
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                try {
                    JSONObject jObject = new JSONObject(message.obj.toString());
                    JSONArray jArray = jObject.getJSONArray("user");
                    listdata = new ArrayList<String>();

                    if (jArray != null) {
                        for (int i=0;i<jArray.length();i++){
                            listdata.add(jArray.get(i).toString());
                        }
                    }
                    frdAdapter = new ArrayAdapter<>(context,
                            R.layout.friends_list_listview, listdata);
                    friendList.setAdapter(frdAdapter);
                    friendList.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {

                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                            if (firstVisibleItem != topVisiblePosition) {
//                                topVisiblePosition = firstVisibleItem;
//                                final String header = listdata.get(firstVisibleItem).substring(0,1);
//                                topHeader.setText(header);
                            }
                        }
                    });


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
        setContentView(R.layout.chat_friend_list);

        backToChatList = (ImageView) findViewById(R.id.backToChatList);
        friendLayout = (RelativeLayout) findViewById(R.id.friendLayout);
        topHeader = (TextView) findViewById(R.id.frientListHeader);

        friendList = (ListView) findViewById(R.id.chatFriendList);



        new Thread(new Runnable() {
            @Override
            public void run() {
                String response = Util.getFriends(getLoggedInUserId());
                Message message = new Message();
                message.what = MESSAGE_RETRIEVED;
                message.obj = response;
                handler.sendMessage(message);

            }
        }).start();


        mGestureDetector = new GestureDetector(this);

//        friendLayout.setOnTouchListener(this);

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
            Intent intent = new Intent();
            intent.setClass(FriendList.this, CameraView.class);
            intent.putExtra("backToCamera", "True");
            startActivity(intent);
            overridePendingTransition(R.anim.from_left, R.anim.to_right);
            FriendList.this.finish();
        }

        return false;
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }




    private String getLoggedInUserId() {
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