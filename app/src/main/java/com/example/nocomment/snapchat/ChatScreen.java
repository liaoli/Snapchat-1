package com.example.nocomment.snapchat;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import org.w3c.dom.Text;

import java.util.List;

public class ChatScreen extends AppCompatActivity {

    private EditText msgET;
    private ListView msgContainer;
    private Button sendButton;
    private ChatAdapter chatAdapter;
    private TextView chat;
    private ImageView backToChatList;
    private ImageView bluetoothBtn;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private final static int REQUEST_ENABLE_BT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_chat_screen);

        msgContainer = (ListView) findViewById(R.id.msgContainer);
        msgET = (EditText) findViewById(R.id.msg);
        sendButton = (Button) findViewById(R.id.sendBtn);
        backToChatList = (ImageView) findViewById(R.id.chatBackToChatList);
        bluetoothBtn = (ImageView) findViewById(R.id.bluetoothBtn);



        chatAdapter = new ChatAdapter(this, new ArrayList<ChatMsg>());
        msgContainer.setAdapter(chatAdapter);




        backToChatList.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChatScreen.this, ChatList.class);
                startActivity(intent);
                ChatScreen.this.finish();
            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = msgET.getText().toString();
                if (TextUtils.isEmpty(msg)) {
                    return;
                }
                sendMsg(msg);
                msgET.setText("");
            }
        });

        msgET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            }
        });



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void scroll() {
        msgContainer.setSelection(msgContainer.getCount() - 1);

    }

    private void sendMsg(String msg) {
        ChatMsg chatMsg = new ChatMsg(true, msg);
        chatAdapter.add(chatMsg);

    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ChatScreen Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.nocomment.snapchat/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
//        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if(mBluetoothAdapter == null){
//
//        }
//        if(!mBluetoothAdapter.isEnabled()){
//            Intent enableBtnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtnIntent, REQUEST_ENABLE_BT);
//        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ChatScreen Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.nocomment.snapchat/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


}
