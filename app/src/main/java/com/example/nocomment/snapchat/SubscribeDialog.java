package com.example.nocomment.snapchat;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by guomingsun on 16/10/16.
 */

public class SubscribeDialog extends DialogFragment {

    private Button subscribeBtn;
    private Context context;
    private String tagName;
    private int MESSAGE_RETRIEVED = 0;


    public SubscribeDialog(Context context, String tagName) {
        this.context = context;
        this.tagName = tagName;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View convertView = inflater.inflate(R.layout.subscribe_dialog, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        subscribeBtn = (Button) convertView.findViewById(R.id.subscribeBtn);

        subscribeBtn.setOnClickListener(sendListener);


        return convertView;
    }

    android.os.Handler handler = new android.os.Handler(new Handler.Callback() {

        public boolean handleMessage(Message message) {
            if (message.what == MESSAGE_RETRIEVED) {
                getDialog().dismiss();
            }
            return false;
        }
    });



    private Runnable mutiThread = new Runnable(){
        public void run(){

            subscribeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String myid = getLoggedInUserId();
                    Util.postSubscribe(myid, tagName);
                }
            });
        }
    };

    private View.OnClickListener sendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v == subscribeBtn){

                Thread thread = new Thread(mutiThread);
                thread.start();
            }
        }
    };

    private String getLoggedInUserId () {
        String loggedInUser = "";

        if (Login.getLoggedinUserId() == "") {
            FileInputStream fis = null;

            try {
                fis = context.getApplicationContext().openFileInput("user");
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
