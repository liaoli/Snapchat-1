package com.example.nocomment.snapchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {


    TextView textView;
    Button loginButton;
    Button signupButton;

    private final int MESSAGE_RETRIEVED = 0;
    android.os.Handler handler = new android.os.Handler(new Handler.Callback() {

        public boolean handleMessage(Message message) {
            if (message.what==MESSAGE_RETRIEVED){
                // update UI
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message.obj.toString(), duration);

                toast.show();

            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {

                loginButton = (Button)findViewById(R.id.login);
                signupButton = (Button) findViewById(R.id.signup);

                loginButton.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v){
                        Intent intent = new Intent(MainActivity.this, FriendShipRequest.class);
                        startActivity(intent);
                    }

                });
                signupButton.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v){
                        Intent intent = new Intent(MainActivity.this, Signup.class);
                        startActivity(intent);
                    }

                });
                if(checkUserLogin()){
                    Intent intent = new Intent(MainActivity.this,FriendShipRequest.class);
                    startActivity(intent);
                }
            }
        }).start();

    }
    private boolean checkUserLogin(){
        boolean result=false;
        try {
            Context context = getApplicationContext();

            FileInputStream fis = context.openFileInput("user");
            InputStreamReader isr = new InputStreamReader(fis);

            BufferedReader bufferedReader = new BufferedReader(isr);

            String user=bufferedReader.readLine();
            String password=bufferedReader.readLine();

            String response = util.login(user,password).trim();

            if(response.equals("login successfully"))
            {result = true;}
            else{ Message message = new Message();
                message.what = MESSAGE_RETRIEVED;
                message.obj = response;
                handler.sendMessage(message);}
            bufferedReader.close();
            isr.close();fis.close();
            return result;
        } catch (FileNotFoundException e) {

            return false;
        } catch (IOException e) {

            return false;
        }
    }
}
