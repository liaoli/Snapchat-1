package com.example.nocomment.snapchat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * Created by guomingsun on 3/09/2016.
 */
public class ChatFriendListAdapter extends ArrayAdapter<String>{

    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<String> mFrdList;
    private ArrayList<String> friendsList;

    String TAG = "chat adapter";

    public ChatFriendListAdapter(Context context, ArrayList<String> frdList) {
        super(context, R.layout.item_in_friend_list, frdList);
        mContext = context;
        mFrdList = frdList;

    }



    android.os.Handler handler = new android.os.Handler(new Handler.Callback() {

        public boolean handleMessage(Message message) {
                String msg=(String) message.obj;
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

            return false;
        }
    });




    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        if(convertView == null){
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_in_friend_list, null);
            ImageView addFriend = (ImageView) convertView.findViewById(R.id.addFriend);


            final TextView friend = (TextView) convertView.findViewById(R.id.userNameFriend);
            addFriend.setOnClickListener(new View.OnClickListener() {
                JSONArray jArray;
                JSONObject jObject;
                boolean flag;

                @Override
                public void onClick(View v) {

                    Log.d(TAG, friend.getText().toString());
                    Log.d(TAG, getLoggedInUserId());

                    jObject = new JSONObject();
                    jArray = new JSONArray();
                    flag = false;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String response = Util.getFriends(getLoggedInUserId());

                            try {
                                jObject = new JSONObject(response.toString());
                                jArray = jObject.getJSONArray("user");
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }

                            for (int i=0; i < jArray.length(); i++) {
                                try {
                                    if (jArray.get(i).equals(friend.getText().toString())) {
                                        flag =  true;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (flag) {

                                Message msg=new Message();
                                msg.obj="Selected user is already a friend";
                                handler.sendMessage(msg);

                            }
                            else {
                                Util.sendFriendRequest(getLoggedInUserId(), friend.getText().toString());
                                friendsList = new ArrayList<>();
                                friendsList.add(friend.getText().toString());
                                Util.sendNotification(getLoggedInUserId(), friendsList,
                                        "Friends Request", 0);

                                Message msg=new Message();
                                msg.obj="Friend request was successfully sent to " +
                                        friend.getText().toString();
                                handler.sendMessage(msg);
                            }
                   }
                    }).start();

                }
            });

        friend.setText(mFrdList.get(position));


        }
        return convertView;
    }



    private String getLoggedInUserId() {
        String loggedInUser = "";

        if (Login.getLoggedinUserId() == "") {
            FileInputStream fis = null;

            try {
                fis = getContext().openFileInput("user");
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
