package com.example.nocomment.snapchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Sina on 10/10/2016.
 * a QR scanner class that handles the creation of a QR code based on a string and also reading
 * a QR code and converting it back to a string
 */

public class QRScanner extends Activity implements ZXingScannerView.ResultHandler {

    String TAG = "QRScanner";
    private ZXingScannerView scannerView;
    private ArrayList<BarcodeFormat> formats = new ArrayList<>();
    private float x1,x2,y1,y2;
    static final int MIN_DISTANCE = 70;

    @Override
    public void onCreate(Bundle state) {

        super.onCreate(state);
        scannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        formats.add(BarcodeFormat.QR_CODE);
        scannerView.setFormats(formats);
        setContentView(scannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        scannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();           // Stop camera on pause
    }


    @Override
    public void handleResult(Result rawResult) {

        Intent intent = new Intent();
        intent.setClass(QRScanner.this, AddFriend.class);
        intent.putExtra("username", rawResult.getText());
        startActivity(intent);
        QRScanner.this.finish();

    }


    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchevent.getX();
                y2 = touchevent.getY();
                float deltaX = x2 - x1;
                float deltaY = y2 - y1;
                if (deltaX > MIN_DISTANCE)
                {
                    Intent intent = new Intent(QRScanner.this, AddFriend.class);
                    startActivity(intent);
                    QRScanner.this.finish();
                }
                else if( Math.abs(deltaX) > MIN_DISTANCE)
                {

                }
                else if(deltaY > MIN_DISTANCE){

                }
                else if( Math.abs(deltaY) > MIN_DISTANCE){

                }

                break;
        }

        return false;
    }
}
