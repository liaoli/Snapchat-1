package com.example.nocomment.snapchat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Sina on 10/10/2016.
 */

public class QRScanner extends Activity implements ZXingScannerView.ResultHandler {

    String TAG = "QRScanner";
    private ZXingScannerView scannerView;
    private ArrayList<BarcodeFormat> formats = new ArrayList<>();


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
        Toast.makeText(this, "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scannerView.resumeCameraPreview(QRScanner.this);
            }
        }, 2000);
    }
}
