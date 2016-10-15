package com.example.nocomment.snapchat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class FriendShipRequest extends AppCompatActivity {

    TextView user;
    TextView message;
    Button accept;
    Button deny;
    ImageView imageView;

    public final static int WHITE = 0xFFFFFFFF;
    public final static int BLACK = 0xFF000000;
    public final static int WIDTH = 400;
    public final static int HEIGHT = 400;
    public final static String STR = "A string to be encoded as QR code";

    private final int MESSAGE_RETRIEVED = 1;
//    android.os.Handler handler = new android.os.Handler(new Handler.Callback() {
//
//        public boolean handleMessage(Message message) {
//            if (message.what==MESSAGE_RETRIEVED){
//                // update UI
//                Context context = getApplicationContext();
//                int duration = Toast.LENGTH_SHORT;
//                Toast toast = Toast.makeText(context, message.obj.toString(), duration);
//
//                toast.show();
////                if(message.obj.toString().contains("login successfully")){
////
////                    Intent intent =new Intent(Login.this,CameraScreen.class);
////                    startActivity(intent);
////                }
//
//            }
//            return false;
//        }
//    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_ship_request);
        user = (TextView) findViewById(R.id.user);
        message = (TextView) findViewById(R.id.message);

        accept =(Button)findViewById(R.id.accept);
        deny =(Button)findViewById(R.id.deny);
        Intent intent = getIntent();

        String userid = intent.getStringExtra("userid");
        String messages = intent.getStringExtra("message");

        user.setText(userid);
        message.setText(messages);
        Context context = getApplicationContext();
        imageView=(ImageView)findViewById(R.id.imageView);
//        Picasso.with(context).load(messages).into(imageView);


        try {
            Bitmap bitmap = encodeAsBitmap(STR);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }



        accept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                        Context context = getApplicationContext();
                        FileInputStream fis = context.openFileInput("user");
                        InputStreamReader isr = new InputStreamReader(fis);

                        BufferedReader bufferedReader = new BufferedReader(isr);

                        String userid=bufferedReader.readLine();
                            bufferedReader.close();
                        util.acceptFriend(userid,user.getText().toString());
                    } catch (FileNotFoundException e) {
                        Log.e("",e.getMessage());

                    } catch (IOException e) {
                        Log.e("", e.getMessage());

                    }
                    }
                }).start();
            }

        });
        deny.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }

        });

    }
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

}
