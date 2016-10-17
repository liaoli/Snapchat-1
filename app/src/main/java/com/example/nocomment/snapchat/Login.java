package com.example.nocomment.snapchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;


/**
 * A class that handles user's login to the database, after the first login, the login details
 * are saved into local storage of the phone/tablet
 */


public class Login extends AppCompatActivity {

    EditText userName;
    EditText pwd;
    Button login;
    static String userId = "";

    private final int MESSAGE_RETRIEVED = 0;
    // UI update handler
    android.os.Handler handler = new android.os.Handler(new Handler.Callback() {

        public boolean handleMessage(Message message) {
            if (message.what==MESSAGE_RETRIEVED){
                // update UI
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message.obj.toString(), duration);

                toast.show();
                if(message.obj.toString().contains("login successfully")){

                    Intent intent =new Intent(Login.this, CameraView.class);
                    startActivity(intent);
                }

            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.button);
        userName = (EditText) findViewById(R.id.login_userName);
        pwd = (EditText) findViewById(R.id.login_password);

        // handling login button click
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String response = "";

                        if (userName.getText().toString().isEmpty() |
                                pwd.getText().toString().isEmpty()) {
                            response = "Please fill in all fields";
                        }
                        else {

                            response = Util.login(userName.getText().toString(), pwd.getText().toString());

                            if (response.trim().equals("login successfully")) {
                                FirebaseInstanceIDService firebaseInstanceIDService = new FirebaseInstanceIDService();
                                firebaseInstanceIDService.registerToken(userName.getText().toString());
                                userId = userName.getText().toString();

                                Context context = getApplicationContext();

                                FileOutputStream outputStream;

                                try {
                                    outputStream = openFileOutput("user", Context.MODE_PRIVATE);
                                    outputStream.write(userName.getText().toString().getBytes());
                                    outputStream.write("\n".getBytes());
                                    outputStream.write(pwd.getText().toString().getBytes());
                                    outputStream.close();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                        Message message = new Message();
                        message.what = MESSAGE_RETRIEVED;
                        message.obj = response;
                        handler.sendMessage(message);


                    }
                }).start();
            }

        });
    }


    public static String getLoggedinUserId() {
        return userId;
    }




}