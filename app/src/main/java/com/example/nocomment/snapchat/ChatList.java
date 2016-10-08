package com.example.nocomment.snapchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChatList extends AppCompatActivity {
    ListView listView;
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        final String [] friends={"Rose","Jack"};
        listView=(ListView)findViewById(R.id.listView);
        adapter=new ArrayAdapter(ChatList.this, R.layout.shareimage,friends);
        listView.setAdapter(adapter);
    }
}
