package com.example.nocomment.snapchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // for convinence to test the chat list and chat screen
        Button button = (Button) findViewById(R.id.ChatListButton);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ChatList.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });



    }

}
