package com.example.nocomment.snapchat;

import android.annotation.SuppressLint;
import android.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by Sina on 10/8/2016.
 */

public class ShareImageDialog extends DialogFragment {

    Button btnStory;
    Button btnShare;
    Button btnShareWithFriends;
    Bitmap bitmap;
    LinearLayout shareImageFrag;
    ArrayList<String> selectedFriends;
    private ListView friendListView;
    private int topVisiblePosition = -1;
    String userId = "";
    private final int MESSAGE_RETRIEVED = 0;
    private ChatListAdapter chatListAdapter;

    public ShareImageDialog() {
        super();
    }

    public ShareImageDialog(Bitmap bitmap) {
        this.bitmap = bitmap;
    }




    android.os.Handler handler = new android.os.Handler(new Handler.Callback() {

        public boolean handleMessage(Message message) {
            if (message.what == MESSAGE_RETRIEVED) {
                // update UI
                Context context = getActivity();
                int duration = Toast.LENGTH_SHORT;
                ArrayList<String> friendsList = new ArrayList<>();
                try {
                    JSONObject jObject = new JSONObject(message.obj.toString());
                    JSONArray jArray = jObject.getJSONArray("user");

                    if (jArray != null) {
                        for (int i = 0; i < jArray.length(); i++) {
                            friendsList.add(jArray.get(i).toString());
                        }
                    }
                    chatListAdapter = new ChatListAdapter(getActivity(), friendsList);
                    friendListView.setAdapter(chatListAdapter);
                    friendListView.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return false;
        }
    });



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.share_image_dialog, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        userId = getLoggedInUserId();
        selectedFriends = new ArrayList<>();

        friendListView = (ListView) view.findViewById(R.id.friendsList);
        shareImageFrag = (LinearLayout) view.findViewById(R.id.shareImageFrag);

        shareImageFrag.setVisibility(View.VISIBLE);

        friendListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        btnStory = (Button) view.findViewById(R.id.story);
        btnStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createStory();
            }
        });


        btnShareWithFriends = (Button) view.findViewById(R.id.shareWithFriends);
        btnShareWithFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (friendListView.getVisibility() == View.VISIBLE) {
                    friendListView.setVisibility(View.GONE);
                }
                else {
                    showFriends();
                }

                if (friendListView != null) {
                    if (btnShare.getVisibility() == View.VISIBLE) {
                        btnShare.setVisibility(View.GONE);
                    } else {
                        btnShare.setVisibility(View.VISIBLE);
                    }
                }
            }
        });



        btnShare = (Button) view.findViewById(R.id.share);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selectedFriends.isEmpty()) {
                    shareImage();
                }

            }
        });


        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = chatListAdapter.getItem(position);

                friendListView.setItemChecked(position, true);
                friendListView.setSelection(position);



                if (selectedFriends.contains(item)) {

                    selectedFriends.remove(item);
                }
                else {
                    selectedFriends.add(item);
                }


                Toast.makeText(getActivity(), selectedFriends.toString(), Toast.LENGTH_LONG)
                        .show();
            }
        });


        return view;
    }



    private void createStory() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                String loggedInUser = getLoggedInUserId();


                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                Util.postImage(loggedInUser, encodedImage, true);


            }
        }).start();


        Toast.makeText(getActivity().getApplicationContext(), "Story Saved Successfully",
                Toast.LENGTH_SHORT).show();
        shareImageFrag.setVisibility(View.GONE);
        dismiss();

    }


    private void shareImage() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                String loggedInUser = getLoggedInUserId();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                String imageLink = Util.postImage(loggedInUser, encodedImage, false);

                Util.sendNotification(Login.getLoggedinUserId(), selectedFriends, imageLink, 2);
            }
        }).start();

//        Toast.makeText(getActivity().getApplicationContext(), "Story Saved Successfully",
//                Toast.LENGTH_SHORT).show();
        shareImageFrag.setVisibility(View.GONE);
        dismiss();

    }


    private void showFriends() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String b = Util.getFriends(getLoggedInUserId());
                ArrayList<String> a = new ArrayList<String>();
                a.add(b);
                a.add("bye bye");
                Message message = new Message();
                message.what = MESSAGE_RETRIEVED;
                message.obj = b;
                handler.sendMessage(message);
            }
        }).start();
    }



    private String getLoggedInUserId () {
        String loggedInUser = "";

        if (Login.getLoggedinUserId() == "") {
            FileInputStream fis = null;

            try {
                fis = getActivity().openFileInput("user");
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




















//    @Override
//    public void run() {
//        friendsListString = Util.getFriends(userId);
//
//        try {
//
//            JSONObject jObject = new JSONObject(friendsListString.toString());
//            JSONArray jArray = jObject.getJSONArray("user");
//            friendsList = new ArrayList<String>();
//
//            if (jArray != null) {
//                for (int i = 0; i < jArray.length(); i++) {
//                    friendsList.add(jArray.get(i).toString());
//                }
//            }
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        friendsListAdapter = new ChatFriendListAdapter(getActivity(), friendsList);
//        friendListView.setAdapter(friendsListAdapter);
//        friendListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (firstVisibleItem != topVisiblePosition) {
////                                topVisiblePosition = firstVisibleItem;
////                                final String header = listdata.get(firstVisibleItem).substring(0,1);
////                                topHeader.setText(header);
//                }
//            }
//        });
//
//    }