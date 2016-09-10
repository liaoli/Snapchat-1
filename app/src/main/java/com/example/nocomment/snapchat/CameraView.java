package com.example.nocomment.snapchat;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class CameraView extends AppCompatActivity implements SurfaceHolder.Callback {

    Button btnTakePhoto;
    Button btnSwitchCamera;
    Button btnSavePhoto;
    Button btnDelete;
    Button btnFlash;
    Button btnFlashOff;
    Button btnSend;
    private boolean isBackCamera = false;




    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera camera;
    Camera.Parameters parameters;

    Bitmap photo, bitmap;



    public static Camera getCameraInstance(){
        Camera cameraInstance = null;
        try {
            cameraInstance = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return cameraInstance; // returns null if camera is unavailable
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        surfaceView = (SurfaceView) findViewById(R.id.camera);

        surfaceView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                camera = getCameraInstance();
                parameters = camera.getParameters();

                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

                parameters.setFlashMode("OFF");

//                parameters.setPreviewSize(surfaceView.getMeasuredWidth(), surfaceView.getMeasuredHeight());

                camera.setParameters(parameters);

                if (parameters.getMaxNumDetectedFaces()>0){
                    camera.startFaceDetection();
                    Toast.makeText(getApplicationContext(),"Face detection started",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        initialize();
    }



    public void initialize() {

        btnTakePhoto = (Button) findViewById(R.id.btnTakePhoto);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });



        btnSwitchCamera = (Button) findViewById(R.id.btnSwitch);
        btnSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchCamera();
            }
        });



        btnFlash = (Button) findViewById(R.id.btnFlash);
        btnFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFlash();
            }
        });


        btnFlashOff = (Button) findViewById(R.id.btnFlashOff);
        btnFlashOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFlash();
            }
        });


        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartCamera();
            }
        });



        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




        btnSavePhoto = (Button) findViewById(R.id.btnSave);
        btnSavePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImagePublic();
            }
        });

    }

    public void takePhoto() {
        camera.takePicture(new Camera.ShutterCallback() {
            @Override
            public void onShutter() {

            }
        }, null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                camera.stopPreview();
                int orientation;

                if (isBackCamera) {

                    orientation = 90;

                }
                else {

                    orientation = 270;

                }

                photo = rotateImage(bitmap, orientation);


                btnSavePhoto.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
                btnSend.setVisibility(View.VISIBLE);
                btnFlashOff.setVisibility(View.GONE);
                btnFlash.setVisibility(View.GONE);
                btnSwitchCamera.setVisibility(View.GONE);

            }
        });
    }



    public static Bitmap rotateImage(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }




    public void switchCamera() {

        int cameraFacing = 0;

        camera.stopPreview();
        isBackCamera = (!isBackCamera);

        if (camera!=null){
            camera.release();
            camera = null;
        }

        if(isBackCamera){
            cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
        }

        else {
            cameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }

        camera = Camera.open(cameraFacing);
        camera.setDisplayOrientation(90);

        try {
            camera.setPreviewDisplay(surfaceHolder);
        }

        catch (IOException e) {
                e.printStackTrace();
            }

        camera.startPreview();

    }


    public void switchFlash() {

        parameters = camera.getParameters();


        if (parameters.getFlashMode() == "OFF") {
//            parameters.setFlashMode("ON");
//            camera.setParameters(parameters);
            btnFlashOff.setVisibility(View.GONE);
            btnFlash.setVisibility(View.VISIBLE);
        }

        else {
//            parameters.setFlashMode("OFF");
//            camera.setParameters(parameters);
            btnFlashOff.setVisibility(View.VISIBLE);
            btnFlash.setVisibility(View.GONE);
        }


    }



    public void downloadImagePublic() {
        File dir = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES),"noComment.SnapChat");
        if (!dir.exists()){
            dir.mkdir();
        }
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(dir.getPath()+File.separator+"IMG_"+timestamp+".jpg");
        FileOutputStream outputStream =null;
        try {
            outputStream = new FileOutputStream(mediaFile);
            photo.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (outputStream!=null){
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mediaFile)));
        Toast.makeText(getApplicationContext(),"Successfully saved ",Toast.LENGTH_LONG).show();
    }



    private void restartCamera() {

        btnSavePhoto.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);
        btnSend.setVisibility(View.GONE);
        btnFlashOff.setVisibility(View.VISIBLE);
        btnFlash.setVisibility(View.GONE);
        btnSwitchCamera.setVisibility(View.VISIBLE);

        camera.startPreview();


    }




    @Override
    protected void onPause() {
        super.onPause();
        camera.release();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera  = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        camera.setFaceDetectionListener(new Camera.FaceDetectionListener() {
            @Override
            public void onFaceDetection(Camera.Face[] faces, Camera camera) {
                if (faces.length>0){
                    System.out.println("@ Location X "+faces[0].rect.centerX()+ "Location Y: "+faces[0].rect.centerY());
                }
            }
        });
        parameters = camera.getParameters();
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

//

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {


    }



}
