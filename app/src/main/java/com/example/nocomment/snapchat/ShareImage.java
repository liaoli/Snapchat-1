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
//    Button btnShare;
//    String encodedImage;


    public ShareImage() {

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


        return view;
    }




    private void createStory() {
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



            Intent i = new Intent();
//            ByteArrayInputStream image = (ByteArrayInputStream) i.getSerializableExtra("image");
////
//            ObjectInputStream objectInputStream = null;
//            objectInputStream = new ObjectInputStream(image);
//            Bitmap tmp = (Bitmap) objectInputStream.readObject();

//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            tmp.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
//            byte[] byteArray = byteArrayOutputStream.toByteArray();

//            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

//            byte[] decodedString = Base64.decode(tmp, Base64.DEFAULT);
//            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);



            Bitmap bmp = null;
            String filename = i.getStringExtra("image");
            try {
                FileInputStream is = getActivity().openFileInput(filename);
                bmp = BitmapFactory.decodeStream(is);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


//
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            bmp.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            Util.postImage(loggedInUser, encodedImage, true);





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