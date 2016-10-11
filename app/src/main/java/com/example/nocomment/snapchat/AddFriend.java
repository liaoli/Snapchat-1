package com.example.nocomment.snapchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import java.io.Serializable;


/**
 * Created by Sina on 10/12/2016.
 */

public class AddFriend extends Activity {

    ImageView addFriend;
    TextView friend, header;
    private float x1,x2,y1,y2;
    static final int MIN_DISTANCE = 70;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);


        addFriend = (ImageView) findViewById(R.id.addFriend);
        friend = (TextView) findViewById(R.id.userNameFriend);
        header = (TextView) findViewById(R.id.frientListHeader);


    }


    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchevent.getX();
                y2 = touchevent.getY();
                float deltaX = x2 - x1;
                float deltaY = y2 - y1;
                if (deltaX > MIN_DISTANCE)
                {
                    Intent intent = new Intent(AddFriend.this, CameraView.class);
                    intent.putExtra("backToCamera", "True");
                    startActivity(intent);
                }
                else if( Math.abs(deltaX) > MIN_DISTANCE)
                {
//                    swipeRightToLeft();
                }
                else if(deltaY > MIN_DISTANCE){
//                    swipeTopToBottom();
                }
                else if( Math.abs(deltaY) > MIN_DISTANCE){
//                    swipeBottopmToTop();
                }

                break;
        }

        return false;
    }


}
