package com.example.nocomment.snapchat;

import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.server.converter.StringToIntConverter;

import java.util.ArrayList;

import org.w3c.dom.Text;

import java.util.List;

public class ChatScreen extends AppCompatActivity {

    /* ************************
     * UI related declaration *
     * ************************
     */
    private EditText msgET;
    private ListView msgContainer;
    private Button sendButton;
    private ChatAdapter chatAdapter;
    private TextView chat;
    private ImageView backToChatList;
    private ImageView bluetoothBtn;
    private DrawerLayout mDrawerLayout;
    private ImageView drawerLeft;
    private ImageView galleryBtn;
    private ImageView callBtn;
    private ImageView cameraBtn;
    private ImageView videoCallBtn;



    private final int CAMERA_PIC_REQUEST = 2;



    /* *******************************
     * Bluetooth related declaration *
     * *******************************
     */
    // local bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;

    // member object for the chat services
    private BluetoothService mChatService = null;

    private String mConnectedDeviceName = null;

    private StringBuffer mOutStringBuffer;

    // intent request code
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.chat_drawer_user);

        msgContainer = (ListView) findViewById(R.id.msgContainer);
        msgET = (EditText) findViewById(R.id.msg);
        sendButton = (Button) findViewById(R.id.sendBtn);
        backToChatList = (ImageView) findViewById(R.id.chatBackToChatList);
        bluetoothBtn = (ImageView) findViewById(R.id.bluetoothBtn);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerLeft = (ImageView) findViewById(R.id.chatSetting);
        galleryBtn = (ImageView) findViewById(R.id.galleryBtn);
        callBtn = (ImageView) findViewById(R.id.phoneCall);
        cameraBtn = (ImageView) findViewById(R.id.cameraBtn);
        videoCallBtn = (ImageView) findViewById(R.id.videoCallBtn);


//
//
//
        chatAdapter = new ChatAdapter(this, new ArrayList<ChatMsg>());
        msgContainer.setAdapter(chatAdapter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//

        if(!mBluetoothAdapter.isEnabled()){
            openDialog("Warning", "Bluetooth is not on\nPlease turn it on");
        }

        bluetoothBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            }
        });

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
                }else{
                    if(!mBluetoothAdapter.isEnabled()){
                        openDialog("Warning", "Bluetooth is not on\nPlease turn it on");
                    } else{
                        sendMsg(msg);
                        msgET.setText("");
                    }
                }

            }
        });

        msgET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            }
        });

//        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.chat_drawer_user, new String[]{"hello"}));



//
        drawerLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        // initialize the bluetooth service to perform bluetooth connection
        //mChatService = new BluetoothService(this, mHandler);

        // initialize the buffer for outgoing msg
        mOutStringBuffer = new StringBuffer("");
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        if( requestCode == CAMERA_PIC_REQUEST)
//        {
//            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//            ImageView image =(ImageView) findViewById(R.id.PhotoCaptured);
//            image.setImageBitmap(thumbnail);
//        }
//        else
//        {
//            //Toast.makeText(demo.this, "Picture NOt taken", Toast.LENGTH_LONG);
//        }
//
//    }


//    private class DrawerItemClickListener implements ListView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            //selectItem(position);
//        }
//    }
//
//    /** Swaps fragments in the main content view */
//    private void selectItem(int position) {
//        // Create a new fragment and specify the planet to show based on position
//        Fragment fragment = new PlanetFragment();
//        Bundle args = new Bundle();
//        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
//        fragment.setArguments(args);
//
//        // Insert the fragment by replacing any existing fragment
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.content_frame, fragment)
//                .commit();
//
//        // Highlight the selected item, update the title, and close the drawer
//        mDrawerList.setItemChecked(position, true);
//        setTitle(mPlanetTitles[position]);
//        mDrawerLayout.closeDrawer(mDrawerList);
//    }

    private void scroll() {
        msgContainer.setSelection(msgContainer.getCount() - 1);

    }

    private void sendMsg(String msg) {
        ChatMsg chatMsg = new ChatMsg(true, msg);

        byte[] send = msg.getBytes();
        mChatService.write(send);
        mOutStringBuffer.setLength(0);

        chatAdapter.add(chatMsg);

    }




    private void openDialog(String title, String content) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                //按下按鈕後執行的動作，沒寫則退出Dialog
                            }
                        }
                )
                .show();
    }
//
//    // confirm device is discoverable
//    private void ensureDiscoverable(){
//        if(mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
//            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//            startActivity(discoverableIntent);
//        }
//    }
//
//    // establish connection with other device
//    private void connectDevice(Intent data, boolean secure){
//        // get the device mac address
//        String macAddress = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//
//        // get the bluetooth object
//        BluetoothDevice bluethDevice = mBluetoothAdapter.getRemoteDevice(macAddress);
//
//        // attemp to connect to the device
//        mChatService.connect(bluethDevice, secure);
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        switch (requestCode){
//            case REQUEST_CONNECT_DEVICE_SECURE:
//                if(requestCode == ChatScreen.this.RESULT_OK){
//                    connectDevice(data,true);
//                }
//                break;
//            case REQUEST_CONNECT_DEVICE_INSECURE:
//                if (requestCode == this.RESULT_OK){
//                    connectDevice(data, false);
//                }
//                break;
//
//        }
//    }
//
//    // update the status on the action bar
//    private void setStatus(CharSequence subTitle){
//
//        if(null == this)
//            return;
//
//        final ActionBar actionBar = this.getActionBar();
//        if(null == actionBar)
//            return;
//
//        actionBar.setSubtitle(subTitle);
//    }
//
//    private void setStatus(int resId){
//        if(null == this){
//            return;
//        }
//        final ActionBar actionBar = this.getActionBar();
//        if(null == actionBar){
//            return;
//        }
//
//        actionBar.setSubtitle(resId);
//    }
//
//    // handler that gets information back from the BluetoothChatService
//    private final Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg){
//            //FragmentActivity activity = getActivity();
//            switch (msg.what){
//                case Constants.MESSAGE_STATE_CHANGE:
//                    switch (msg.arg1){
//                        case BluetoothService.STATE_CONNECTED:
//                            setStatus(getString(R.string.title_connected, mConnectedDeviceName));
//                            chatAdapter.clear();
//                            break;
//                        case BluetoothService.STATE_CONNECTING:
//                            setStatus(R.string.title_connecting);
//                            break;
//                        case BluetoothService.STATE_LISTEN:
//                            break;
//                        case BluetoothService.STATE_NONE:
//                            setStatus(R.string.title_not_connected);
//                            break;
//                    }
//                    break;
//                case Constants.MESSAGE_WRITE:
//                    byte[] writeBuf = (byte[]) msg.obj;
//                    // construct a string from the buffer
//                    String writeMsg = new String (writeBuf);
//                    //chatAdapter.add(mConnectedDeviceName + ": " + writeMsg);
//                    break;
//                case Constants.MESSAGE_READ:
//                    byte[] readBuf = (byte[]) msg.obj;
//                    // construct a string from the valid bytes in the buffer
//                    String readMsg = new String(readBuf, 0, msg.arg1);
//                    //chatAdapter.add(mConnectedDeviceName + ": " + readMsg);
//                    break;
//                case Constants.MESSAGE_DEVICE_NAME:
//                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
//                    if(null != ChatScreen.this){
//                        Toast.makeText(ChatScreen.this, "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case Constants.MESSAGE_TOAST:
//                    if(null != ChatScreen.this){
//                        Toast.makeText(ChatScreen.this, msg.getData().getString(Constants.TOAST), Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//            }
//        }
//    };


}
