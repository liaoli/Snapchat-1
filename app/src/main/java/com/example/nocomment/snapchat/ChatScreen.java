package com.example.nocomment.snapchat;

import android.app.ActionBar;
import android.app.Activity;
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
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.server.converter.StringToIntConverter;

import java.util.ArrayList;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

public class ChatScreen extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener{

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
    private DrawerLayout mDrawerLayout;
    private ImageView drawerLeft;
    private ImageView galleryBtn;
    private ImageView callBtn;
    private ImageView cameraBtn;
    private ImageView videoCallBtn;
    private Button editName;
    private Button blockFriend;
    private Button removeFriend;
    private LinearLayout chatSrnLayout;
    private GestureDetector mGestureDetector;
    private TextView chatTitle;

    private TextView date;

    private ImageView discoverBtn;
    private ImageView scanBtn;

    private int verticalMinDistance = 10;
    private int minVelocity = 0;



    private final int CAMERA_PIC_REQUEST = 2;



    /* *******************************
     * Bluetooth related declaration *
     * *******************************
     */
    // local bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;

    // member object for the bluetooth chat services
    private BluetoothService mChatService = null;

    private String mConnectedDeviceName = null;

    private StringBuffer mOutStringBuffer;

    // intent request code
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_ENABLE_BT = 2;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.chat_drawer_user);

        // get bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


//        if(mBluetoothAdapter == null){
//            openDialog("Warning", "Your device does not have bluetooth supports");
//        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mChatService != null)
            mChatService.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mChatService != null){
            if(mChatService.getState() == BluetoothService.STATE_NONE);{
                mChatService.start();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

        } else{
            if(mChatService == null)
                setupChat();
        }
    }

    private void setupChat(){

        msgContainer = (ListView) findViewById(R.id.msgContainer);
        msgET = (EditText) findViewById(R.id.msg);
        sendButton = (Button) findViewById(R.id.sendBtn);
        backToChatList = (ImageView) findViewById(R.id.chatBackToChatList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerLeft = (ImageView) findViewById(R.id.chatSetting);
        galleryBtn = (ImageView) findViewById(R.id.galleryBtn);
        callBtn = (ImageView) findViewById(R.id.phoneCall);
        cameraBtn = (ImageView) findViewById(R.id.cameraBtn);
        videoCallBtn = (ImageView) findViewById(R.id.videoCallBtn);
        editName = (Button) findViewById(R.id.editName);
        blockFriend = (Button) findViewById(R.id.blockFriend);
        removeFriend = (Button) findViewById(R.id.removeFriend);
        chatSrnLayout = (LinearLayout) findViewById(R.id.chatSrnLayout);
        chatTitle = (TextView) findViewById(R.id.chatTitle);
        date = (TextView) findViewById(R.id.dateToday);

        discoverBtn = (ImageView) findViewById(R.id.discoverableBtn);
        scanBtn = (ImageView) findViewById(R.id.scanBtn);

        // set up top textview to day of the week
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        Calendar dateToday = Calendar.getInstance();
        String dateForToday = dayFormat.format(dateToday.getTime());
        date.setText("Today" + " / " + dateForToday);

        // enable gesture for going back chat list
        mGestureDetector = new GestureDetector(this);
        chatSrnLayout.setOnTouchListener(this);

        // initialize and set adapter for chat msg container
        chatAdapter = new ChatAdapter(this, new ArrayList<ChatMsg>());
        msgContainer.setAdapter(chatAdapter);

        // notification for the user
        openDialog("Notification", "By clicking Circle upper left corner can make device " +
                "\"Discoverable\"" +
                "\nBy clicking Magnifier upper right corner can enter \"Scanner\"");

//        // get bluetooth adapter
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        // ask user to turn on the bluetooth
//        if(!mBluetoothAdapter.isEnabled()){
//            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//        }
//
//        if(mBluetoothAdapter == null){
//            openDialog("Warning", "Your device does not have bluetooth supports");
//        }



        backToChatList.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChatScreen.this, ChatList.class);
                startActivity(intent);
                overridePendingTransition(R.anim.from_right, R.anim.to_left);
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
                    sendMsg(msg);
                    msgET.setText("");
                }
            }
        });

        // open the drawer by click the button upper left corner
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

        discoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBluetoothAdapter.isEnabled())
                    ensureDiscoverable();

            }
        });

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBluetoothAdapter.isEnabled()){
                    Intent intent = new Intent();
                    intent.setClass(ChatScreen.this, DeviceListActivity.class);
                    startActivityForResult(intent, REQUEST_CONNECT_DEVICE_SECURE);
                }

            }
        });

        mChatService = new BluetoothService(ChatScreen.this, mHandler);
        mOutStringBuffer = new StringBuffer("");


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }


    private void scroll() {
        msgContainer.setSelection(msgContainer.getCount() - 1);

    }

    private void sendMsg(String msg) {
        byte[] send = msg.getBytes();
        mChatService.write(send);
        mOutStringBuffer.setLength(0);

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return mGestureDetector.onTouchEvent(me);
    }

    // set swipe gestures for moving from one activity to another
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
            Intent intent = new Intent();
            intent.setClass(ChatScreen.this, ChatList.class);
            startActivity(intent);
            overridePendingTransition(R.anim.from_right, R.anim.to_left);
            ChatScreen.this.finish();

        } else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {

        }

        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    // provide suggestion (warning) to the user
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

    // each time new message is come or sent, the list view will be update to last line
    private void scrollMyListViewToBottom() {
        msgContainer.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                msgContainer.setSelection(msgContainer.getCount() - 1);
            }
        });
    }

    // confirm device is discoverable
    private void ensureDiscoverable(){
        if(mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    private boolean me = true;

    // handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1){
                        case BluetoothService.STATE_CONNECTED:
                            chatTitle.setText(mConnectedDeviceName);
                            chatAdapter.clear();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            chatTitle.setText(R.string.title_connecting);
                            break;
                        case BluetoothService.STATE_LISTEN:
                            break;
                        case BluetoothService.STATE_NONE:
                            chatTitle.setText(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMsg = new String (writeBuf);
                    Calendar rightNow = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                    String timeForNow = sdf.format(rightNow.getTime());
                    ChatMsg sendMsg = new ChatMsg(me, writeMsg, timeForNow);
                    chatAdapter.add(sendMsg);
                    scrollMyListViewToBottom();
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMsg = new String(readBuf, 0, msg.arg1);
                    Calendar rightNowRcv = Calendar.getInstance();
                    SimpleDateFormat sdfRcv = new SimpleDateFormat("hh:mm a");
                    String timeForNowRcv = sdfRcv.format(rightNowRcv.getTime());
                    ChatMsg rcvMsg = new ChatMsg(!me, readMsg, timeForNowRcv);
                    chatAdapter.add(rcvMsg);
                    scrollMyListViewToBottom();
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    Toast.makeText(ChatScreen.this,
                            "Connected to " + mConnectedDeviceName,
                            Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_TOAST:
                        Toast.makeText(ChatScreen.this,
                                msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case REQUEST_CONNECT_DEVICE_SECURE:
                if(resultCode == Activity.RESULT_OK){
                    String address =
                            data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    mChatService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                if(resultCode == Activity.RESULT_OK){
                    setupChat();
                } else {
                    openDialog("Warning", "Bluetooth is still not enabled");
                }
        }
    }




}
