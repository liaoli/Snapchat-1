package com.example.nocomment.snapchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class FriendRequest extends AppCompatActivity {

    TextView user;
    TextView message;
    Button accept;
    Button deny;

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
                            Util.acceptFriend(userid,user.getText().toString());
                            FriendRequest.this.finish();

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(FriendRequest.this,"Friend successfully added",
                                            Toast.LENGTH_LONG).show();
                                }
                            });


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

                FriendRequest.this.finish();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(FriendRequest.this,"Friendship request denied",
                                Toast.LENGTH_LONG).show();
                    }
                });

            }

        });

    }

}
