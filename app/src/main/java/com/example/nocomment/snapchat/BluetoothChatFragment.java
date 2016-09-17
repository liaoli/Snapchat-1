package com.example.nocomment.snapchat;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.KeyEventCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by guomingsun on 11/09/2016.
 */
public class BluetoothChatFragment extends Fragment{


    // layout view
    private EditText msgET;
    private ListView msgContainer;
    private Button sendButton;

    private TextView chat;
    private ImageView backToChatList;
    private ImageView bluetoothBtn;

    private static final String TAG = "BluetoothChatFragment";

    // intent request code
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // name of connected device
    private String mConnectedDeviceName = null;

    // array adapter for the conversation thread
    private ArrayAdapter<String> chatAdapter;

    // String buffer for outgoing message
    private StringBuffer mOutStringBuffer;

    // local bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;

    // member object for the chat services
    private BluetoothService mChatService = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // get local bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // notify user if bluetooth is not supported
        if(mBluetoothAdapter == null){
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_chat_screen, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        sendButton = (Button) view.findViewById(R.id.sendBtn);
        msgET = (EditText) view.findViewById(R.id.msg);
        msgContainer = (ListView) view.findViewById(R.id.msgContainer);

    }

    @Override
    public void onStart() {
        super.onStart();

        // If BT is not on, request to enable
        if(!mBluetoothAdapter.isEnabled()){
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else if(mChatService == null) {
            setupChat();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mChatService.getState() == BluetoothService.STATE_NONE){
            // start the bluetooth chat service
            mChatService.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mChatService != null)
            mChatService.stop();
    }

    public void setupChat(){
        Log.d(TAG, "setupChat()");

        // initialize the array adapter for the conversation thread
        chatAdapter = new ArrayAdapter<String>(getActivity(), R.layout.msg_right);

        msgContainer.setAdapter(chatAdapter);

        // initialize the compose filed with a listener for the return key
        msgET.setOnEditorActionListener(mWriteListener);

        //msgET.setOnEditorActionListener(mWriteListener);
        sendButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                View view = getView();
                if(null != view){
                    TextView textView = (TextView) view.findViewById(R.id.msg);
                    String message = textView.getText().toString();
                    sendMessage(message);
                }
            }
        });

        // initialize the bluetooth service to perform bluetooth connection
        mChatService = new BluetoothService(getActivity(), mHandler);

        // initialize the buffer for outgoing msg
        mOutStringBuffer = new StringBuffer("");
    }

    private void sendMessage(String message){
        // check that we are connected
        if(mChatService.getState() != BluetoothService.STATE_CONNECTED){
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();

            if(message.length() > 0){
                // get the message bytes and tell the bluetooth service to write
                byte[] send = message.getBytes();
                mChatService.write(send);

                // reset out string buffer to zero
                // clear edit text
                mOutStringBuffer.setLength(0);
                msgET.setText(mOutStringBuffer);
            }
        }
    }

    private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener(){
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event){
            if(actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP){
                String message = view.getText().toString();
                sendMessage(message);
            }
            return true;
        }
    };

    // update the status on the action bar
    private void setStatus(CharSequence subTitle){
        FragmentActivity activity = getActivity();

        if(null == activity)
            return;

        final ActionBar actionBar = activity.getActionBar();
        if(null == actionBar)
            return;

        actionBar.setSubtitle(subTitle);
    }

    private void setStatus(int resId){
        FragmentActivity activity = getActivity();
        if(null == activity){
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if(null == actionBar){
            return;
        }

        actionBar.setSubtitle(resId);
    }

    // handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            FragmentActivity activity = getActivity();
            switch (msg.what){
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1){
                        case BluetoothService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected, mConnectedDeviceName));
                            chatAdapter.clear();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothService.STATE_LISTEN:
                            break;
                        case BluetoothService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMsg = new String (writeBuf);
                    chatAdapter.add(mConnectedDeviceName + ": " + writeMsg);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMsg = new String(readBuf, 0, msg.arg1);
                    chatAdapter.add(mConnectedDeviceName + ": " + readMsg);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if(null != activity){
                        Toast.makeText(activity, "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if(null != activity){
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case REQUEST_CONNECT_DEVICE_SECURE:
                if(requestCode == Activity.RESULT_OK){
                    connectDevice(data,true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                if (requestCode == Activity.RESULT_OK){
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                if(resultCode == Activity.RESULT_OK){
                    setupChat();
                } else {
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), ,Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    }

    // establish connection with other device
    private void connectDevice(Intent data, boolean secure){
        // get the device mac address
        String macAddress = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

        // get the bluetooth object
        BluetoothDevice bluethDevice = mBluetoothAdapter.getRemoteDevice(macAddress);

        // attemp to connect to the device
        mChatService.connect(bluethDevice, secure);
    }

    @Override
    public void onCreateOptionMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.bluetooth_chat_menu, menu);

    }

    @Override
    public boolean onOptionItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.secure_connect_scan:{
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.secure_insecure_scan:{
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }
            case R.id.discoverable:{
                ensureDiscoverable();
                return true;
            }
        }
        return false;
    }

    // confirm device is discoverable
    private void ensureDiscoverable(){
        if(mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }
}
