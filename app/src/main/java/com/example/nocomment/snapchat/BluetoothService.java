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
import java.util.concurrent.ThreadFactory;


/**
 * Created by guomingsun on 9/09/2016.
 */
public class BluetoothService {

    private static final String TAG = "BluetoothChatService";

    private static final String NAME = "BluetoothChat";

    // unique UUID for this application
    // temp UUID, have not yet been tested
    private static final UUID MY_UUID =
            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

    // member field
    private final BluetoothAdapter mAdapter;
    private int mState;
    private final Handler mHandler;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private AcceptThread mAcceptThread;


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
        // update new state to handler so that UI can update
        mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    public synchronized int getState(){
        return mState;
    }

    public synchronized void start(){
        Log.d(TAG, "start");

        // cancel any thread attempting to make a connection
        if(mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // cancel any thread currently running a connection
        if(mConnectedThread != null){
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        setState(STATE_LISTEN);

        // start the thread to listen on a Bluetooth Server Socket
        if(mAcceptThread == null){
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }


    }

    // stop all threads
    public synchronized void stop(){
        Log.d(TAG, "stop");

        if(mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if(mConnectedThread != null){
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // cancel the accept thread because we only want to connect to one device
        if(mAcceptThread != null){
            mAcceptThread.cancel();
            mAcceptThread = null;
        }

        setState(STATE_NONE);
    }



    public synchronized void connect(BluetoothDevice device){
        Log.d(TAG, "connect to:" + device);

        if(mState == STATE_CONNECTING){
            if(mConnectThread != null){
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // cancel any thread currently running a connection
        if(mConnectedThread != null){
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    // start the connnected thread to begin managing a bluetooth connection
    public synchronized void connected(BluetoothSocket socket,
                                       BluetoothDevice device){
        // cancel the thread that completed the connection
        if(mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // cancel any thread that currently is running a connection
        if(mConnectedThread != null){
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // cancel the accept thread because we only want to connect to one device
        if(mAcceptThread != null){
            mAcceptThread.cancel();
            mAcceptThread = null;
        }

        // start the thread to manage the connection and perform transmission
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        // send the name of the connected device back to the UI activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        setState(STATE_CONNECTED);
    }

    public void write(byte[] out){

        // create temporary object
        ConnectedThread r;
        //synchronize a copy of the connected thread
        synchronized (this){
            if(mState != STATE_CONNECTED)
                return;
            r = mConnectedThread;
        }
        //perform the write unsynchronize
        r.write(out);
    }

    // Indicate that the connection was lost and notify the UI activity
    private void connectionFailed(){
        setState(STATE_LISTEN);

        // send a failure message back to the activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        // start the service over to restart listening mode
        BluetoothService.this.start();
    }

    // indicate that the connection was lost and notify the UI activity
    private void connectionLost(){

        // send the failure message back to the activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Device connection was lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        BluetoothService.this.start();
    }

    // this thread runs while listening for incoming connection
    private class AcceptThread extends Thread{

        // the local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread(){
            BluetoothServerSocket tmp = null;

            try{
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e){
                Log.e(TAG, "Listen() failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run(){
            setName("Accept Thread");

            BluetoothSocket socket = null;

            // listen to the server socket if we are not connected
            while(mState != STATE_CONNECTED){
                try{
                    // blocking call which will only return on a successful connection or an
                    // exception
                    socket = mmServerSocket.accept();
                } catch(IOException e){
                    Log.e(TAG, "accept() failed", e);
                    break;
                }

                // if the connection was accepted
                if(socket != null){
                    synchronized (BluetoothService.this){
                        switch (mState){
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                connected(socket, socket.getRemoteDevice());
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // either not ready or already connected
                                try{
                                    socket.close();
                                } catch (IOException e){
                                    Log.e(TAG, "Could not close unwanted socket", e);
                                }
                                break;
                        }
                    }
                }
            }
            Log.i(TAG, "End accept thread");
        }

        public void cancel(){
            Log.d(TAG, "cancel" + this);
            try{
                mmServerSocket.close();
            } catch(IOException e){
                Log.e(TAG, "close() of server failed", e);
            }
        }
    }

    private class ConnectThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device){
            mmDevice = device;
            BluetoothSocket tmp = null;

            try{
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch(IOException e){
                Log.e(TAG, "create() failed", e);
            }
            mmSocket = tmp;
        }

        public void run(){
            setName("ConnectThread");

            // always cancel discovery
            mAdapter.cancelDiscovery();

            // make a connection to bluetooth socket
            try{
                //blocking call
                mmSocket.connect();
            } catch(IOException e){

                try{
                    mmSocket.close();
                } catch (IOException e2){

                    Log.e(TAG, "unable to close() during connection failure", e2);
                }
                connectionFailed();
                return;
            }

            // reset the connect thread because we're done
            synchronized (BluetoothService.this){
                mConnectThread = null;
            }

            // start the connected thread
            connected(mmSocket, mmDevice);
        }
        public void cancel(){
            try{
                mmSocket.close();
            } catch (IOException e){
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }

    }

    private class ConnectedThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final InputStream mmInputStream;
        private final OutputStream mmOutputStream;

        public ConnectedThread(BluetoothSocket socket){
            Log.d(TAG, "create ConnnectedThread: ");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // get the bluetooth input and output stream
            try{
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e){
                Log.e(TAG, "temp socket not created", e);
            }

            mmInputStream = tmpIn;
            mmOutputStream = tmpOut;
        }

        public void run(){
            Log.i(TAG, "Begin mConnectedThread");
            byte[] buffer = new byte[1024];
//            byte[] imgBuffer = new byte[1024*1024];
            int bytes;
//            int pos = 0;

            // keep listening to the inputstream while connected
            while(mState == STATE_CONNECTED){
                try{
                    // read from the inputstream
                    bytes = mmInputStream.read(buffer);
//                    System.arraycopy(buffer, 0, imgBuffer, pos, bytes);
//                    pos += bytes;

                    // send the obtained bytes to the inputstream
                    mHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();

                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    BluetoothService.this.start();
                    break;
                }
            }
        }
        public void cancel(){
            try{
                mmSocket.close();
            } catch(IOException e){
                Log.e(TAG, "close() of connection socket failed" + e);
            }
        }

        public void write(byte[] out){
            try{
                mmOutputStream.write(out);
                // share the sent message back to the UI Activity
                mHandler.obtainMessage(Constants.MESSAGE_WRITE,  -1, -1, out).sendToTarget();
            } catch (IOException e){
                Log.e(TAG, "Exception during write", e);
            }
        }
    }



}
