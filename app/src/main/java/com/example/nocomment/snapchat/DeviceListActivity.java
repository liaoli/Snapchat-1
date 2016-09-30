package com.example.nocomment.snapchat;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Set;

/**
 * Created by guomingsun on 14/09/2016.
 */
public class DeviceListActivity extends Activity {

    private static final String TAG = "DeviceListActivity";

    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    private BluetoothAdapter mBluethAdapter;

    // new discovered devices
    private ArrayAdapter<String> mNewDevicesArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setup the window
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_device_list);

        // set result CANCELED in case the user backs out
        setResult(Activity.RESULT_CANCELED);

        // initialize the button to perform device discovery
        Button scanButton = (Button) findViewById(R.id.buttonScan);
        scanButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                doDiscovery();
                v.setVisibility(View.GONE);
            }
        });

        // initialize array adapters
        // paired devices and new discovered device each
        ArrayAdapter<String> pairedDeviceArrayAdapter =
                new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        // find and set up the listview for paired devices
        ListView pairedListView = (ListView) findViewById(R.id.pairedDevice);
        pairedListView.setAdapter(pairedDeviceArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // find and set up the ListView for newly discovered devices
        ListView newDeviceListView = (ListView) findViewById(R.id.newDevices);
        newDeviceListView.setAdapter(mNewDevicesArrayAdapter);
        newDeviceListView.setOnItemClickListener(mDeviceClickListener);

        // register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // register for  broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        // get the local bluetooth adapter
        mBluethAdapter = BluetoothAdapter.getDefaultAdapter();

        // get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBluethAdapter.getBondedDevices();

        // if there are paired devices, add each one to the array adapter
        if(pairedDevices.size()>0){
            findViewById(R.id.titlePairedDevice).setVisibility(View.VISIBLE);
            for(BluetoothDevice device : pairedDevices){
                pairedDeviceArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else{
            String noDevices = getResources().getText(R.string.none_paired).toString();
            pairedDeviceArrayAdapter.add(noDevices);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // make sure we're not doing discovery anymore
        if(mBluethAdapter != null){
            mBluethAdapter.cancelDiscovery();
        }

        // unregister broadcast listener
        this.unregisterReceiver(mReceiver);
    }


    private void doDiscovery(){
        Log.d(TAG, "doDiscovery()");

        // indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);

        // turn on subtitle for new devices
        findViewById(R.id.titleNewDevice).setVisibility(View.VISIBLE);

        if(mBluethAdapter.isDiscovering()){
            mBluethAdapter.cancelDiscovery();
        }

        // request discover from bluetooth adapter
        mBluethAdapter.startDiscovery();
    }

    // the on-click listener for all devices in the ListView
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener(){

        public void onItemClick(AdapterView<?> av, View view, int arg2, long arg3){
            mBluethAdapter.cancelDiscovery();

            // get the device mac address, which is the last 17 chars in the view
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);

            // create the result intent and include the mac address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            // set result and finish this activity
            setResult(Activity.RESULT_OK, intent);

        }
    };

    // the broadcastreceiver listens for discovered devices and changes the title when
    // discovery is finished
    public final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // when discovery finds a device
            if(BluetoothDevice.ACTION_FOUND.equals(action)){

                // get the bluetooth deviceobject from the internet
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // if it's already paired, skip it
                if(device.getBondState() != BluetoothDevice.BOND_BONDED){
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_device);
                if(mNewDevicesArrayAdapter.getCount() == 0){
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };


}
