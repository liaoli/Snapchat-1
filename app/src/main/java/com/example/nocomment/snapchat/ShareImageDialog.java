package com.example.nocomment.snapchat;

import android.annotation.SuppressLint;
import android.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.ArrayAdapter;
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
    String userId = "";
    private final int MESSAGE_RETRIEVED = 0;
    ArrayAdapter<String> chatListAdapter;


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
                final Context context = getActivity();
                int duration = Toast.LENGTH_SHORT;

                ArrayList<String> friendsList = new ArrayList<>();

                try {
                    JSONObject jObject = new JSONObject(message.obj.toString());
                    JSONArray jArray = jObject.getJSONArray("user");

                    if (jArray.length() > 0) {
                        for (int i = 0; i < jArray.length(); i++) {
                            friendsList.add(jArray.get(i).toString());
                        }

                        chatListAdapter = new ArrayAdapter<>(context,
                                R.layout.list_view, friendsList);

                        friendListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                        friendListView.setAdapter(chatListAdapter);
                        friendListView.setVisibility(View.VISIBLE);
                        btnShare.setVisibility(View.VISIBLE);
                    }

                    else {

                        Toast.makeText(getActivity(),"No friends found",Toast.LENGTH_LONG).show();

                    }

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
                    btnShare.setVisibility(View.GONE);
                }
                else {
                    showFriends();
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
                else {
                    Toast.makeText(getActivity(),"Select a friend first",Toast.LENGTH_LONG).show();
                }

            }
        });


        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = chatListAdapter.getItem(position);

                if (selectedFriends.contains(item)) {
                    selectedFriends.remove(item);

                }
                else {
                    friendListView.setItemChecked(position, true);
                    selectedFriends.add(item);
                }

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

                String imageLink = Util.postImage(loggedInUser, encodedImage, true);
                Util.sendNotification(loggedInUser, selectedFriends, imageLink, 3);


            }
        }).start();


        Toast.makeText(getActivity().getApplicationContext(), "Story Created Successfully",
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

                Util.sendNotification(loggedInUser, selectedFriends, imageLink, 2);


            }
        }).start();

        Toast.makeText(getActivity(), "Shared Successfully",
                Toast.LENGTH_SHORT).show();

        shareImageFrag.setVisibility(View.GONE);
        selectedFriends.clear();
        dismiss();

    }


    private void showFriends() {

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











