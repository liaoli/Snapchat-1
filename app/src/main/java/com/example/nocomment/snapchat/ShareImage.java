package com.example.nocomment.snapchat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;


/**
 * Created by Sina on 10/8/2016.
 */

public class ShareImage extends DialogFragment {

    Button btnStory;
    Button btnShare;
    Bitmap bitmap=null;


    public ShareImage () {
        super();
    }

    public ShareImage(Bitmap bitmap) {
        this.bitmap = bitmap;
        System.out.println("bitmap "+bitmap);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.share_image, container, false);

        btnStory  = (Button) view.findViewById(R.id.story);
        btnStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createStory();
            }
        });


        btnShare  = (Button) view.findViewById(R.id.share);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendImage();
            }
        });


        return view;
    }




    private void createStory() {

        new Thread(new Runnable() {
            @Override
            public void run() {

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



                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                Util.postImage(loggedInUser, encodedImage, true);


            }
        }).start();

    }



    private void sendImage() {

        new Thread(new Runnable() {
            @Override
            public void run() {

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



                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                Util.postImage(loggedInUser, encodedImage, false);


            }
        }).start();

    }


}




//
//
//
//

//
//
//
//
//
//        btnStory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                createStory();
//            }
//
//        });
//
//
//        btnShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//
//        });

// Inflate the layout for this fragment