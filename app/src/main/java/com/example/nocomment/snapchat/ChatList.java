package com.example.nocomment.snapchat;

import android.content.Intent;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.w3c.dom.Text;

import java.util.List;

public class ChatList extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener{

    private ImageView addNewChat;
    private ImageView goToCma;
    private TextView chat;

    private GestureDetector mGestureDetector;
    private RelativeLayout chatListWhole;

    private int verticalMinDistance = 10;
    private int minVelocity = 0;


    // test user
    private String user1 = "Jason", user2 = "Park";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);


        addNewChat = (ImageView) findViewById(R.id.crtNewChat);
        goToCma = (ImageView) findViewById(R.id.backToCam);
        chat = (TextView) findViewById(R.id.chatTitle);
        chatListWhole = (RelativeLayout) findViewById(R.id.chatListWhole);

        mGestureDetector = new GestureDetector(this);

        chatListWhole.setOnTouchListener(this);




        addNewChat.setOnClickListener(new View.OnClickListener(){
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
        goToCma.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChatList.this, Stories.class);
                startActivity(intent);
                ChatList.this.finish();
            }
        });



        chat.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChatList.this, ChatScreen.class);
                startActivity(intent);
                ChatList.this.finish();
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
//
//    class TouhListener implements View.OnTouchListener {
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            // TODO Auto-generated method stub
//
//            return mGestureDetector.onTouchEvent(event);
//        }
//
//    }
//
//    class GestureListener implements GestureDetector.OnGestureListener {
//
//        @Override
//        public boolean onDown(MotionEvent e) {
//            // TODO Auto-generated method stub
//
//            return false;
//        }
//
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//                               float velocityY) {
//            // e1 触摸的起始位置，e2 触摸的结束位置，velocityX X轴每一秒移动的像素速度（大概这个意思） velocityY 就是Ｙ咯
//            //手势左,上为正 ——，右，下为负正
//            if (e2.getX()-e1.getX()>50) {
//                //为什么是50？ 这个根据你的模拟器大小来定，看看模拟器宽度，e2.getX()-e1.getX()<屏幕宽度就ＯＫ
//                Toast.makeText(getApplicationContext(), "向右滑动", Toast.LENGTH_LONG).show();
//                //要触发什么事件都在这里写就OK
//                //如果要跳转到另外一个activity
//                Intent intent=new Intent(ChatList.this, Stories.class);
//                startActivity(intent);
//            }
//            if (Math.abs(e2.getX()-e1.getX())>50) {
//            }
//            return false;
//        }
//
//        @Override
//        public void onLongPress(MotionEvent e) {
//            // TODO Auto-generated method stub\
//
//        }
//
//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2,
//                                float distanceX, float distanceY) {
//            // TODO Auto-generated method stub
//
//            return false;
//        }
//
//        @Override
//        public void onShowPress(MotionEvent e) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public boolean onSingleTapUp(MotionEvent e) {
//            // TODO Auto-generated method stub
//
//            return false;
//        }
//
//    }
}


