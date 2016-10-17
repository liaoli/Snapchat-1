package com.example.nocomment.snapchat;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
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
import android.util.DisplayMetrics;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
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

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.bitmap;

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

    private int MESSAGE_RETRIEVED = 100;

    private TextView date;


    private int verticalMinDistance = 200;
    private int minVelocity = 0;

    private String userName;
    private IntentFilter filter;

    private Uri photoUri;


    // send and receive related constants (result code)
    private final int CAMERA_PIC_REQUEST = 1;
    private final int PICK_IMAGE = 0;
    static final int REQUEST_TAKE_PHOTO = 2;
    private String selectedImagePath;
    private String tempImagePath;

    String mCurrentPhotoPath;

    /*
     * video call related
     */
    String login = "login";


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
//        mGestureDetector = new GestureDetector(this);
        mGestureDetector = new GestureDetector(this);


        // enable gesture on listview but also disable gesture to open drawer
        msgContainer.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(mGestureDetector.onTouchEvent(motionEvent)){
                    return true;
                }
                return false;
            }
        });

        chatSrnLayout.setOnTouchListener(this);

        // initialize and set adapter for chat msg container
        chatAdapter = new ChatAdapter(this, new ArrayList<ChatMsg>());
        msgContainer.setAdapter(chatAdapter);

        msgET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollMyListViewToBottom();
            }
        });


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
                chatUserName.setText(userName);
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "CaptureImage");
                values.put(MediaStore.Images.Media.BUCKET_ID, "CaptureImage");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                photoUri = ChatScreen.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent CaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                CaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(CaptureIntent, CAMERA_PIC_REQUEST);

            }
        });

        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                EditNameDialog editNameDialog = new EditNameDialog(ChatScreen.this);
                editNameDialog.show(fm, "Hi");
            }
        });

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

        } else if(requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK){
            String string = Environment.getExternalStorageDirectory().toString();
            final Bitmap bitmap;
            String path;
            path = photoUri.getPath();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                sendImgFromCamera(path, bitmap);
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


//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        View v = getCurrentFocus();
//
//        if (v != null &&
//                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
//                v instanceof EditText &&
//                !v.getClass().getName().startsWith("android.webkit.")) {
//            int scrcoords[] = new int[2];
//            v.getLocationOnScreen(scrcoords);
//            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
//            float y = ev.getRawY() + v.getTop() - scrcoords[1];
//
//            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
//                //hideKeyboard(this);
//        }
//        return super.dispatchTouchEvent(ev);
//    }
//
//    public static void hideKeyboard(Activity activity) {
//        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
//            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
//        }
//    }


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

                String response = Util.postImage(Login.getLoggedinUserId(), encodedImage, false);
                Util.sendNotification(Login.getLoggedinUserId(), friend, response, 2);

            }
        }).start();
        scrollMyListViewToBottom();
    }

    private void sendImgFromCamera(String img, final Bitmap bitmap){

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> friend = new ArrayList<String>();
                friend.add(userName);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);


                byte[] byteArray = byteArrayOutputStream.toByteArray();

                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                String response = Util.postImage(Login.getLoggedinUserId(), encodedImage, false);
                Message msgImg = new Message();
                msgImg.what = MESSAGE_RETRIEVED;
                msgImg.obj = response;
                handler.sendMessage(msgImg);
                Util.sendNotification(Login.getLoggedinUserId(), friend, response, 2);

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
        ChatMsg rcvImg = new ChatMsg(ChatMsg.LEFT_IMG, img, timeForNow);
        chatAdapter.add(rcvImg);
        scrollMyListViewToBottom();

    }

    android.os.Handler handler = new android.os.Handler(new Handler.Callback() {

        public boolean handleMessage(Message message) {
            if (message.what==MESSAGE_RETRIEVED){
                // update UI
                Calendar rightNow = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                final String timeForNow = sdf.format(rightNow.getTime());
                ChatMsg sendImg = new ChatMsg(ChatMsg.RIGHT_CAMERA, message.obj.toString(), timeForNow);
                chatAdapter.add(sendImg);
                scrollMyListViewToBottom();

            }
            return false;
        }
    });


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