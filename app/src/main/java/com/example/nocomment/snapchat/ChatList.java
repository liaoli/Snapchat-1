package com.example.nocomment.snapchat;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ChatList extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {

    private ImageView addNewChat;
    private ImageView goToCma;
    private TextView chat;

    private GestureDetector mGestureDetector;
    private RelativeLayout chatListWhole;

    private int verticalMinDistance = 10;
    private int minVelocity = 0;

    private final int MESSAGE_RETRIEVED = 0;
    private ArrayList<String> listdata;
    private ChatListAdapter chatListAdapter;
    private ListView chatList;

    android.os.Handler handler = new android.os.Handler(new Handler.Callback() {

        public boolean handleMessage(Message message) {
            if (message.what == MESSAGE_RETRIEVED) {
                // update UI
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                try {
                    JSONObject jObject = new JSONObject(message.obj.toString());
                    JSONArray jArray = jObject.getJSONArray("user");
                    listdata = new ArrayList<String>();

                    if (jArray != null) {
                        for (int i = 0; i < jArray.length(); i++) {
                            listdata.add(jArray.get(i).toString());
                        }
                    }
                    chatListAdapter = new ChatListAdapter(ChatList.this, listdata);
                    chatList.setAdapter(chatListAdapter);

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
        setContentView(R.layout.activity_chat_list);


        addNewChat = (ImageView) findViewById(R.id.crtNewChat);
        goToCma = (ImageView) findViewById(R.id.backToCam);
        chat = (TextView) findViewById(R.id.chatTitle);
        chatListWhole = (RelativeLayout) findViewById(R.id.chatListWhole);
        chatList = (ListView) findViewById(R.id.chatList);

        mGestureDetector = new GestureDetector(this);

        chatListWhole.setOnTouchListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String b = Util.getFriends(Login.myId);
                ArrayList<String> a = new ArrayList<String>();
                a.add(b);
                a.add("bye bye");
                Message message = new Message();
                message.what = MESSAGE_RETRIEVED;
                message.obj = b;
                handler.sendMessage(message);
            }
        }).start();


        addNewChat.setOnClickListener(new View.OnClickListener() {
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
        goToCma.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChatList.this, Stories.class);
                startActivity(intent);
                overridePendingTransition(R.anim.from_right, R.anim.to_left);
                ChatList.this.finish();
            }
        });


        chat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChatList.this, ChatScreen.class);
                startActivity(intent);
                ChatList.this.finish();
            }
        });

        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(ChatList.this, ChatScreen.class);
                String userName = (String) chatList.getItemAtPosition(position);
                intent.putExtra("userName", userName);
                startActivity(intent);
                overridePendingTransition(R.anim.from_left, R.anim.to_right);
                ChatList.this.finish();
            }
        });
        SwipeDetector swipeDetector = new SwipeDetector();
        chatList.setOnTouchListener(swipeDetector);


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
            intent.setClass(ChatList.this, Stories.class);
            startActivity(intent);
            overridePendingTransition(R.anim.from_right, R.anim.to_left);
            ChatList.this.finish();


        } else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {

        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return mGestureDetector.onTouchEvent(me);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


}




