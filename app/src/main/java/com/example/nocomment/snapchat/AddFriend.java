package com.example.nocomment.snapchat;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Sina on 10/12/2016.
 */

public class AddFriend extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {

    private ListView friendsListView;
    private Button searchUser, searchQR, searchContact;
    private final int MESSAGE_RETRIEVED = 0;
    private ChatFriendListAdapter frdAdapter;
    private GestureDetector mGestureDetector;

    private int verticalMinDistance = 10;
    private int minVelocity = 0;
    private int topVisiblePosition = -1;
    private TextView topHeader;




    EditText searchText;
    String userId;
    String scannedUserId;
    String TAG = "Add Friends Activity";



    android.os.Handler handler = new android.os.Handler(new Handler.Callback() {

        public boolean handleMessage(Message message) {
            if (message.what==MESSAGE_RETRIEVED){
                // update UI
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                try {
                    JSONObject jObject = new JSONObject(message.obj.toString());
                    JSONArray jArray = jObject.getJSONArray("user");
                    ArrayList<String> friendsArray = new ArrayList<>();

                    if (jArray.length() > 0) {
                        for (int i=0;i<jArray.length();i++){
                            friendsArray.add(jArray.get(i).toString());
                        }

                    }
                    else {
                        runOnUiThread(new Runnable() {
                            public void run() {

                                Toast.makeText(AddFriend.this,"User not found",Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    frdAdapter = new ChatFriendListAdapter(AddFriend.this, friendsArray);
                    friendsListView.setAdapter(frdAdapter);
                    friendsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {

                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                            if (firstVisibleItem != topVisiblePosition) {
//                                topVisiblePosition = firstVisibleItem;
//                                final String header = listdata.get(firstVisibleItem).substring(0,1);
//                                topHeader.setText(header);
                            }
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return false;
        }
    });




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);


        topHeader = (TextView) findViewById(R.id.addFriendHeader);

        friendsListView = (ListView) findViewById(R.id.friendsList);

        searchUser = (Button) findViewById(R.id.searchUser);
        searchQR = (Button) findViewById(R.id.searchQR);
        searchContact = (Button) findViewById(R.id.searchContact);

        searchText = (EditText) findViewById(R.id.searchText);
        searchText.setSingleLine();


        mGestureDetector = new GestureDetector(this);


        searchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchText.getText() != null) {
                    userId = searchText.getText().toString();
                    Log.d(TAG, userId);

                    if (!userId.equals(getLoggedInUserId())) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String response = Util.findUsers(userId);
                                Log.d(TAG, response);
                                Message message = new Message();
                                message.what = MESSAGE_RETRIEVED;
                                message.obj = response;
                                handler.sendMessage(message);
                            }
                        }).start();
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(AddFriend.this,"You can't add yourself as a friend",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    hideKeyboard(v);
                }
            }
        });



        searchContact.setOnClickListener(new View.OnClickListener() {
            String response = "";
            JSONArray jsonUsersArray;
            JSONObject jsonUsersObject;

            @Override
            public void onClick(View v) {

                final ArrayList<String> phones;
                jsonUsersArray = new JSONArray();
                jsonUsersObject = new JSONObject();

                phones = getNumbers(getApplicationContext());


                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            for (int i = 0; i < phones.size(); i++) {
                                String phone = phones.get(i).replace("(","").replace(")", "")
                                        .replace("-", "").replace(" ", "");
                                Log.d(TAG, phone);
                                response = Util.findUserByPhone(phone);
                                Log.d("Response", response);
                                String newResponse = response.substring(10, response.length()-5);
                                Log.d("Modified Response", newResponse.toString());
                                jsonUsersArray.put(newResponse);
                                Log.d("Test", jsonUsersArray.toString());
                            }

                            try {
                                jsonUsersObject.put("user", jsonUsersArray);
                                Log.d("Final", jsonUsersObject.toString());
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Message message = new Message();
                            message.what = MESSAGE_RETRIEVED;
                            message.obj = jsonUsersObject;
                            handler.sendMessage(message);
                        }
                    }).start();

            }
        });



        searchQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AddFriend.this, QRScanner.class);
                startActivity(intent);
                }

        });




        friendsListView.setOnTouchListener(new OnSwipeTouchListener(AddFriend.this) {

            public void onSwipeRight() {
//                Toast.makeText(CameraView.this, "right", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AddFriend.this, CameraView.class);
                startActivity(i);
            }

        });


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

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {

        }
        else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
            Intent intent = new Intent();
            intent.setClass(AddFriend.this, CameraView.class);
            intent.putExtra("backToCamera", "True");
            startActivity(intent);
            overridePendingTransition(R.anim.from_left, R.anim.to_right);
            AddFriend.this.finish();

        }

        return false;
    }



    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent().getExtras() != null) {

            scannedUserId = getIntent().getStringExtra("username");

            if (!scannedUserId.equals(getLoggedInUserId())) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String response = Util.findUsers(scannedUserId);
                        Log.d(TAG, response);
                        Message message = new Message();
                        message.what = MESSAGE_RETRIEVED;
                        message.obj = response;
                        handler.sendMessage(message);
                    }
                }).start();
            }
            else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(AddFriend.this,"You can't add yourself as a friend",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }




    private String getLoggedInUserId () {
        String loggedInUser = "";

        if (Login.getLoggedinUserId() == "") {
            FileInputStream fis = null;

            try {
                fis = openFileInput("user");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                loggedInUser = bufferedReader.readLine();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            loggedInUser = Login.getLoggedinUserId();
        }


        return loggedInUser;

    }



    private void hideKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }




    public ArrayList<String> getNumbers(Context context) {
        ContentResolver cr = getContentResolver();
        ArrayList<String> alContacts = new ArrayList<String>();

        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {

            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        alContacts.add(contactNumber);
                        break;
                    }
                    pCur.close();
                }

            } while (cursor.moveToNext());
        }

        return alContacts;
    }


}
