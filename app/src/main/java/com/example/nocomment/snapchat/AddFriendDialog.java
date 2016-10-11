package com.example.nocomment.snapchat;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Sina on 10/12/2016.
 */

public class AddFriendDialog extends DialogFragment {

    Button btnAddFriend;
    LinearLayout shareImageFrag;
    TextView showUserName;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.add_friend_dialog, container, false);

        shareImageFrag = (LinearLayout) view.findViewById(R.id.addFriendFrag);
        showUserName = (TextView) view.findViewById(R.id.showUserName);
        showUser();



        btnAddFriend  = (Button) view.findViewById(R.id.addFriend);
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddFriend.class);
                startActivity(intent);
            }
        });


        return view;
    }



    private void showUser() {

        String loggedInUser = "";

        if (Login.getLoggedinUserId() == "") {
            FileInputStream fis = null;

            try {
                fis = getActivity().openFileInput("user");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                loggedInUser = bufferedReader.readLine();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        else {
            loggedInUser = Login.getLoggedinUserId();
        }
        showUserName.setText(loggedInUser);
    }




}
