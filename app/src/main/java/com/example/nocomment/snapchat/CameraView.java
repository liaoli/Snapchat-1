package com.example.nocomment.snapchat;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import io.github.rockerhieu.emojiconize.Emojiconize;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class CameraView extends AppCompatActivity implements SurfaceHolder.Callback {

    Button btnTakePhoto, btnSwitchCamera;
    Button btnSavePhoto, btnDelete;
    Button btnFlash, btnFlashOff;
    Button btnSend;
    Button btnDraw, btnSmiley, btnText;

    Button btnRed, btnBlack, btnWhite, btnBlue, btnYellow, btnGreen;

    Button imageSmiling, imageLaughing, imageSad, imageAngry, imageTeasing, imageInLove;

    private static boolean isBackCamera = false;

    SurfaceHolder surfaceHolder;
    SurfaceView surfaceView;
    FrameLayout imageLayout, drawingPad, allColors, allEmojis;
    ImageView imageView, smiley;
    EditText text;

    Camera camera;
    Camera.Parameters parameters;

    Bitmap photo, bitmap;

    HandDrawing drawing;



    public static Camera getCameraInstance() {
        Camera cameraInstance = null;
        try {
            cameraInstance = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return cameraInstance; // returns null if camera is unavailable
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Emojiconize.activity(this).go();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        drawing = new HandDrawing(getApplicationContext());

        surfaceView = (SurfaceView) findViewById(R.id.camera);
        imageLayout = (FrameLayout) findViewById(R.id.imageLayout);
        imageView = (ImageView) findViewById(R.id.imageView);
        drawingPad = (FrameLayout) findViewById(R.id.drawingPad);
        allColors = (FrameLayout) findViewById(R.id.colors);
        text = (EditText) findViewById(R.id.text);
        text.setSingleLine();
        smiley = (ImageView) findViewById(R.id.smiley);
        allEmojis = (FrameLayout) findViewById(R.id.emojis);




        surfaceView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                camera = getCameraInstance();
                parameters = camera.getParameters();

                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

                parameters.setFlashMode("OFF");

                camera.setParameters(parameters);

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
                saveImage();
            }
        });


        btnDraw = (Button) findViewById(R.id.btnDraw);
        btnDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!drawing.draw) {
                    drawingPad.addView(drawing);
                    drawing.draw = true;
                }

                if (allColors.getVisibility() == View.VISIBLE) {
                    allColors.setVisibility(View.GONE);
                }
                else {
                    allColors.setVisibility(View.VISIBLE);
                }

            }

        });


        btnText = (Button) findViewById(R.id.btnText);
        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (text.getVisibility() == View.VISIBLE) {
                    text.setVisibility(View.GONE);
                    text.getText().clear();
                }
                else {
                    if (text.hasFocus()) {
                        hideKeyboard(view);
                    }
                    text.setVisibility(View.VISIBLE);
                    text.requestFocus();

                    InputMethodManager inputManager =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(text, 0);
                    inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                            InputMethodManager.HIDE_IMPLICIT_ONLY);
                }

            }

        });

        text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        text.setOnTouchListener(new View.OnTouchListener() {
            float yPosition;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        yPosition = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        text.setY(event.getRawY() + yPosition);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });



        btnRed = (Button) findViewById(R.id.btnRed);
        btnRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawing.setColor(Color.RED);
            }
        });


        btnWhite = (Button) findViewById(R.id.btnWhite);
        btnWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawing.setColor(Color.WHITE);
            }
        });


        btnBlue = (Button) findViewById(R.id.btnBlue);
        btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawing.setColor(Color.BLUE);
            }
        });


        btnBlack = (Button) findViewById(R.id.btnBlack);
        btnBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawing.setColor(Color.BLACK);
            }
        });


        btnGreen = (Button) findViewById(R.id.btnGreen);
        btnGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawing.setColor(Color.GREEN);
            }
        });


        btnYellow = (Button) findViewById(R.id.btnYellow);
        btnYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawing.setColor(Color.YELLOW);
            }
        });




        btnSmiley = (Button) findViewById(R.id.btnSmiley);
        btnSmiley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (allEmojis.getVisibility() == View.GONE) {
                    allEmojis.setVisibility(View.VISIBLE);
                }
                else {
                    allEmojis.setVisibility(View.GONE);
                }
            }
        });


        smiley.setOnTouchListener(new View.OnTouchListener() {
            float xPosition, yPosition;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xPosition = view.getX() - event.getRawX();
                        yPosition = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        smiley.setX(event.getRawX() + xPosition);
                        smiley.setY(event.getRawY() + yPosition);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });



        imageSmiling = (Button) findViewById(R.id.imageSmiling);
        imageSmiling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                smiley.setImageResource(R.drawable.smiling);
                smiley.setLayoutParams(new FrameLayout.LayoutParams(150,150));
                smiley.setY(200);
            }
        });



        imageLaughing = (Button) findViewById(R.id.imageLaughing);
        imageLaughing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                smiley.setImageResource(R.drawable.laughing);
                smiley.setLayoutParams(new FrameLayout.LayoutParams(150,150));
                smiley.setY(200);
            }
        });


        imageSad = (Button) findViewById(R.id.imageSad);
        imageSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                smiley.setImageResource(R.drawable.sad);
                smiley.setLayoutParams(new FrameLayout.LayoutParams(150,150));
                smiley.setY(200);
            }
        });


        imageAngry = (Button) findViewById(R.id.imageAngry);
        imageAngry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                smiley.setImageResource(R.drawable.angry);
                smiley.setLayoutParams(new FrameLayout.LayoutParams(150,150));
                smiley.setY(200);
            }
        });


        imageTeasing = (Button) findViewById(R.id.imageTeasing);
        imageTeasing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                smiley.setImageResource(R.drawable.teasing);
                smiley.setLayoutParams(new FrameLayout.LayoutParams(150,150));
                smiley.setY(200);
            }
        });


        imageInLove = (Button) findViewById(R.id.imageInLove);
        imageInLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                smiley.setImageResource(R.drawable.inlove);
                smiley.setLayoutParams(new FrameLayout.LayoutParams(150,150));
                smiley.setY(200);
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
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                camera.stopPreview();


                int orientation;

                if (isBackCamera) {

                    orientation = 90;

                } else {

                    orientation = 270;

                }

                photo = rotateImage(bitmap, orientation);


                imageView.setImageBitmap(photo);


                btnTakePhoto.setVisibility(View.GONE);
                btnSavePhoto.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
                btnSend.setVisibility(View.VISIBLE);
                btnFlashOff.setVisibility(View.GONE);
                btnFlash.setVisibility(View.GONE);
                btnSwitchCamera.setVisibility(View.GONE);
                btnDraw.setVisibility(View.VISIBLE);

                btnText.setVisibility(View.VISIBLE);
                btnSmiley.setVisibility(View.VISIBLE);

                surfaceView.setVisibility(View.INVISIBLE);
                drawingPad.setVisibility(View.VISIBLE);

            }
        });
    }



    public void saveImage() {
        File dir = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES), "noComment.SnapChat");
        if (!dir.exists()) {
            dir.mkdir();
        }
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(dir.getPath() + File.separator + "IMG_" + timestamp + ".jpg");
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(mediaFile);
            imageLayout.setDrawingCacheEnabled(true);
            Bitmap finalImage = Bitmap.createBitmap(imageLayout.getDrawingCache());
            imageLayout.setDrawingCacheEnabled(false);
            finalImage.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mediaFile)));
        Toast.makeText(getApplicationContext(), "Image Saved to Galley", Toast.LENGTH_LONG).show();
    }



    public void switchCamera() {

        int cameraFacing = 0;

        camera.stopPreview();
        isBackCamera = (!isBackCamera);

        if (camera != null) {
            camera.release();
            camera = null;
        }

        if (isBackCamera) {
            cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
        } else {
            cameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }

        camera = Camera.open(cameraFacing);
        camera.setDisplayOrientation(90);

        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        camera.startPreview();

    }



    public void switchFlash() {

        parameters = camera.getParameters();


        if (parameters.getFlashMode() == "OFF") {
            parameters.setFlashMode("ON");
            camera.setParameters(parameters);
            btnFlashOff.setVisibility(View.GONE);
            btnFlash.setVisibility(View.VISIBLE);
        } else {
            parameters.setFlashMode("OFF");
            camera.setParameters(parameters);
            btnFlash.setVisibility(View.GONE);
            btnFlashOff.setVisibility(View.VISIBLE);
        }
    }



    private void restartCamera() {

        btnSavePhoto.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);
        btnSend.setVisibility(View.GONE);
        btnFlashOff.setVisibility(View.VISIBLE);
        btnFlash.setVisibility(View.GONE);
        btnSwitchCamera.setVisibility(View.VISIBLE);
        btnTakePhoto.setVisibility(View.VISIBLE);
        btnDraw.setVisibility(View.GONE);
        btnText.setVisibility(View.GONE);
        btnSmiley.setVisibility(View.GONE);

        allColors.setVisibility(View.GONE);
        allEmojis.setVisibility(View.GONE);


        surfaceView.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(null);
        imageView.invalidate();

        if (drawing.draw) {
            drawing.reset();
        }

        drawingPad.removeView(drawing);
        drawingPad.setVisibility(View.GONE);
        text.getText().clear();
        text.setVisibility(View.GONE);
        smiley.setImageBitmap(null);
        smiley.invalidate();


        camera.startPreview();

    }



    private void hideKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    private static Bitmap rotateImage(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }




    @Override
    protected void onPause() {
        super.onPause();
        camera.release();
    }



    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {


        if (isBackCamera) {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        } else {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        }


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

    }


    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        camera.release();
    }


}