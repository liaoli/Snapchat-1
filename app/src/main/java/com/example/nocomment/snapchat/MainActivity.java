package com.example.nocomment.snapchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {


    Button loginButton;
    Button signupButton;
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
                        Intent intent = new Intent(MainActivity.this, Login.class);
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
                    Intent intent = new Intent(MainActivity.this, CameraView.class);
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

            String response = Util.login(user,password).trim();

            if(response.equals("login successfully"))
            {result = true;}
            bufferedReader.close();
            isr.close();fis.close();
            return result;
        } catch (FileNotFoundException e) {
            Log.e("",e.getMessage());
            return false;
        } catch (IOException e) {
            Log.e("", e.getMessage());
            return false;
        }
    }
}