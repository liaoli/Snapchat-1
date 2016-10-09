package com.example.nocomment.snapchat;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Base64;
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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.server.converter.StringToIntConverter;

import java.util.ArrayList;

import org.jivesoftware.smack.chat.Chat;
import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

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
    private TextView chatUserName;

    private TextView date;


    private int verticalMinDistance = 10;
    private int minVelocity = 0;

    private String userName;
    private IntentFilter filter;


    // send and receive related constants (result code)
    private int PICK_IMAGE = 5;
    private String selectedImagePath;

    /*
     * video call related
     */
    String login = "login";




    private final int CAMERA_PIC_REQUEST = 2;

    private FirebaseMessagingService mChatService = null;
    public static final int MESSAGE_READ = 2;
    public static final int IMAGE_READ = 3;



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
        setupChat();


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(FirebaseMessagingService.FRIEND_MESSAGE_ACCEPTED));

        LocalBroadcastManager.getInstance(this).registerReceiver(mImageReceiver,
                new IntentFilter(FirebaseMessagingService.FRIEND_IMAGE_ACCEPTED));

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStart() {
        super.onStart();

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
        chatUserName = (TextView) findViewById(R.id.chatUserName);


        Bundle extras = getIntent().getExtras();
        userName = extras.getString("userName");
        chatTitle.setText(userName);
        chatUserName.setText(userName);

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

//        // notification for the user
//        openDialog("Notification", "By clicking Circle upper left corner can make device " +
//                "\"Discoverable\"" +
//                "\nBy clicking Magnifier upper right corner can enter \"Scanner\"");


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
//                Intent intent = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent.setType("image/*");
//                startActivityForResult(intent, 0);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);


            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });

        cameraBtn.postDelayed(new Runnable() {
            public void run() {
                cameraBtn.setVisibility(View.INVISIBLE);
            }
        }, 6000);




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            // do decode and send image
            Uri selectedImageUri = data.getData();
            final Bitmap bitmap;
            selectedImagePath = getPath(selectedImageUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                sendImg(selectedImagePath, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
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
//        byte[] send = msg.getBytes();
        final String newMsg = msg;
        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String timeForNow = sdf.format(rightNow.getTime());
        ChatMsg sendMsg = new ChatMsg(ChatMsg.RIGHT_MSG, msg, timeForNow);
        chatAdapter.add(sendMsg);
        scrollMyListViewToBottom();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> friend = new ArrayList<String>();
                friend.add(userName);
                String response = Util.sendNotification("b", friend, newMsg, 1);

            }
        }).start();

    }

    private void sendImg(String img, final Bitmap bitmap){
        final String newImg = img;
        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        final String timeForNow = sdf.format(rightNow.getTime());
        ChatMsg sendImg = new ChatMsg(ChatMsg.RIGHT_IMG, img, timeForNow);
        chatAdapter.add(sendImg);
        scrollMyListViewToBottom();




        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> friend = new ArrayList<String>();
                friend.add(userName);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);


                byte[] byteArray = byteArrayOutputStream.toByteArray();

                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                String response = Util.postImage(Login.myId, encodedImage, false);
                Util.sendNotification("b", friend, response, 2);

//                Util.postImage(Login.myId, response, false);

            }
        }).start();
    }

    public void receiveMsg(String msg){

        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String timeForNow = sdf.format(rightNow.getTime());
        ChatMsg rcvMsg = new ChatMsg(ChatMsg.LEFT_MSG, msg, timeForNow);
        chatAdapter.add(rcvMsg);
        scrollMyListViewToBottom();

    }
    public void receiveImg(String img){

        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String timeForNow = sdf.format(rightNow.getTime());
        ChatMsg rcvMsg = new ChatMsg(ChatMsg.LEFT_IMG, img, timeForNow);
        chatAdapter.add(rcvMsg);
        scrollMyListViewToBottom();

    }

    public Bitmap loadBitmap(String url)
    {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try
        {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return bm;
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

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(FirebaseMessagingService.FRIEND_MESSAGE_ACCEPTED)){
                String msg=intent.getStringExtra("message");
                receiveMsg(msg);
            }


        }};

    private BroadcastReceiver mImageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(FirebaseMessagingService.FRIEND_IMAGE_ACCEPTED)){
                String img=intent.getStringExtra("image");
                receiveImg(img);
            }
        }
    };




}
