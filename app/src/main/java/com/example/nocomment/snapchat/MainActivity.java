package com.example.nocomment.snapchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button loginButton;
    Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //textView = (TextView) findViewById(R.id.userName);
        loginButton = (Button)findViewById(R.id.login);
        signupButton = (Button) findViewById(R.id.signup);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move from ActivityOne to ActivityTwo
                Intent intent = new Intent(MainActivity.this,Login.class);
              //  intent.putExtra("keyOne","Hello world");
                startActivity(intent);
            }
        });


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move from ActivityOne to ActivityTwo
                Intent intent = new Intent(MainActivity.this,Signup.class);
              //  intent.putExtra("keyOne","Hello world");
                startActivity(intent);
            }
        });

    }
}
