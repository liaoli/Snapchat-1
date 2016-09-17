package com.example.nocomment.snapchat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


/**
 * Created by guomingsun on 9/09/2016.
 */
public class BluetoothService {

    private static final String TAG = "BluetoothChatService";

    private static final String NAME_SECURE = "BluetoothChatSecure";
    private static final String NAME_INSECURE = "BluetoothChatInsecure";

    // member field
    private final BluetoothAdapter mAdapter;
    private int mState;
    private final Handler mHandler;
    private ConnectThread mConnectThread;

    // constants that indicate the current connection state
    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;





    public BluetoothService(Context context, Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mHandler = handler;
    }

    private synchronized void setState(int state){
        Log.d(TAG, "setState()" + mState + " -> " + state);
        mState = state;

        mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    public synchronized int getState(){
        return mState;
    }

    // stop all threads
    public synchronized void stop(){
        Log.d(TAG, "stop");

        if(mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }
    }

    public synchronized void start(){
        Log.d(TAG, "start");

        // cancel any thread attempting to make a connection
        if(mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }

    }

    public synchronized void connect(BluetoothDevice device, boolean secure){
        Log.d(TAG, "connect to:" + device);

        // cancel any thread attempting to make a connection
        if(mConnectThread != null){
            mConnectThread.cancel();
        }
    }

    // Indicate that the connection was lost and notify the UI activity
    private void connectionFailed(){
        // send a failure message back to the activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        // start the service over to restart listening mode
        BluetoothService.this.start();
    }

    private class AcceptThread extends Thread{

        // the local server socket
        private final BluetoothServerSocket mmServerSocker;
        private String mSocketType;

        public AcceptThread(boolean secure){
            BluetoothServerSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";

            try{
                if(secure){
                    tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, MY_UUID_SECURE);
                }
            }

        }
    }

    private class ConnectThread extends Thread{
        
    }


}
