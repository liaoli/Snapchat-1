package com.example.nocomment.snapchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;


/**
 * A class that handles new user's signup, after successful signup, the user is logged in and the
 * user info are saved in local storage of the phone/tablet
 */

public class Signup extends AppCompatActivity {

    EditText userName;
    EditText email;
    EditText pwd;
    EditText compwd;
    Button signup;

    private final int MESSAGE_RETRIEVED = 0;
    // UI update handler
    android.os.Handler handler = new android.os.Handler(new Handler.Callback() {

        public boolean handleMessage(Message message) {
            if (message.what==MESSAGE_RETRIEVED){
                // update UI
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message.obj.toString(), duration);
                if(message.obj.toString().contains("Create user success!")){

                    Intent intent =new Intent(Signup.this, CameraView.class);
                    startActivity(intent);
                }
                toast.show();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userName = (EditText) findViewById(R.id.userName);
        email = (EditText) findViewById(R.id.email);
        pwd = (EditText) findViewById(R.id.pwd);
        compwd = (EditText) findViewById(R.id.compwd);
        signup = (Button) findViewById(R.id.sign_up);

        // handling signup button clicks
        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String response = "";

                        if (!pwd.getText().toString().equals(compwd.getText().toString())) {
                            response = "Password is not consistent";
                        }
                        else if (userName.getText().toString().isEmpty() |
                                email.getText().toString().isEmpty() |
                                pwd.getText().toString().isEmpty() |
                                compwd.getText().toString().isEmpty() |
                                signup.getText().toString().isEmpty()) {
                            response = "Please fill in all fields";
                        }

                        else {
                            response = Util.signUP(userName.getText().toString(), email.getText().toString(), pwd.getText().toString());
                            if (response.trim().equals("Create user success!")) {
                                FirebaseInstanceIDService firebaseInstanceIDService = new FirebaseInstanceIDService();
                                firebaseInstanceIDService.registerToken(userName.getText().toString());

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




}