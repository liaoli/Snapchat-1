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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * Created by Sina on 10/12/2016.
 * A dialog fragment that shows the QR code version of the user's username,
 * along with the options to a user to see his/her friends list and the ability to add a new friend
 */

public class AddFriendDialog extends DialogFragment {

    Button btnAddFriend;
    Button btnMyFriends;
    LinearLayout shareImageFrag;
    TextView showUserName;
    ImageView qrCode;
    Bitmap qrBitmap = null;
    public final static int WHITE = 0xFFFFFFFF;
    public final static int BLACK = 0xFF000000;
    public final static int WIDTH = 400;
    public final static int HEIGHT = 400;


    // a method that initializes view of the layout and available options(buttons)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.add_friend_dialog, container, false);

        shareImageFrag = (LinearLayout) view.findViewById(R.id.addFriendFrag);
        showUserName = (TextView) view.findViewById(R.id.showUserName);
        qrCode = (ImageView) view.findViewById(R.id.qrCode);


        try {
            qrBitmap = encodeAsBitmap(getLoggedInUserId());
            qrCode.setImageBitmap(qrBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        showUserName.setText(getLoggedInUserId());



        btnAddFriend  = (Button) view.findViewById(R.id.addFriend);
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddFriend.class);
                startActivity(intent);
            }
        });


        btnMyFriends  = (Button) view.findViewById(R.id.myFriends);
        btnMyFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FriendList.class);
                startActivity(intent);
            }
        });


        return view;
    }


    // encoding the username (string) to a QR code bitmap
    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }



    // getting the userId of the logged in user by checking the local storage or the username used
    // in login page if this is the first time the user is logging in
    private String getLoggedInUserId() {
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
