package com.example.nocomment.snapchat;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.Surface;
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
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Sina on 10/12/2016.
 * The main camera class which takes care of a preview of the camera both front and back side,
 * providing options to a user to take a picture, modify it by adding movable text and pictures
 * to it, hand draw with different color on it and save the picture. Flash and autofocus option
 * for cameras supporting flash and autofocus are also provided.
 */

public class CameraView extends AppCompatActivity implements SurfaceHolder.Callback {

    Button btnTakePhoto, btnSwitchCamera;
    Button btnSavePhoto, btnDelete;
    Button btnFlash, btnFlashOff;
    Button btnSend, btnAddFriend;
    Button btnDraw, btnErase, btnSmiley, btnText;

    Button btnRed, btnBlack, btnWhite, btnBlue, btnYellow, btnGreen;

    Button imageSmiling, imageLaughing, imageSad, imageAngry, imageTeasing, imageInLove,
            imageCry, imageLaughCry, imageShit, imageShocked, imageKiss, imageCool;

    ImageView imageSmilingView, imageLaughingView, imageSadView, imageAngryView,
            imageTeasingView, imageInLoveView, imageCryView, imageLaughCryView,
            imageShitView, imageShockedView, imageKissView, imageCoolView;

    private static boolean isBackCamera = false;

    SurfaceHolder surfaceHolder;
    SurfaceView surfaceView;
    FrameLayout imageLayout, drawingPad, allColors, allEmojis;
    ImageView capturedImageView;
    EditText text;

    Camera camera;
    Camera.Parameters parameters;

    Bitmap photo, bitmap;

    HandDrawing drawing;



    // getting the camera instance if already created and creating a new one if not exists
    public static Camera getCameraInstance() {
        Camera cameraInstance = null;
        try {
            cameraInstance = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return cameraInstance; // returns null if camera is unavailable
    }




    // a method that handles view of the layout and all the available options like the buttons etc.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        drawing = new HandDrawing(getApplicationContext());

        surfaceView = (SurfaceView) findViewById(R.id.camera);
        imageLayout = (FrameLayout) findViewById(R.id.imageLayout);
        capturedImageView = (ImageView) findViewById(R.id.imageView);
        drawingPad = (FrameLayout) findViewById(R.id.drawingPad);
        allColors = (FrameLayout) findViewById(R.id.colors);
        text = (EditText) findViewById(R.id.text);
        text.setSingleLine();
        allEmojis = (FrameLayout) findViewById(R.id.emojis);

        imageSmilingView = new ImageView(getApplicationContext());
        imageLaughingView = new ImageView(getApplicationContext());
        imageSadView = new ImageView(getApplicationContext());
        imageAngryView = new ImageView(getApplicationContext());
        imageTeasingView = new ImageView(getApplicationContext());
        imageInLoveView = new ImageView(getApplicationContext());
        imageShockedView = new ImageView(getApplicationContext());
        imageKissView = new ImageView(getApplicationContext());
        imageShitView = new ImageView(getApplicationContext());
        imageCoolView = new ImageView(getApplicationContext());
        imageLaughCryView = new ImageView(getApplicationContext());
        imageCryView = new ImageView(getApplicationContext());



        // a method to change current activity when swiping the camera surface view to left or right
        surfaceView.setOnTouchListener(new OnSwipeTouchListener(CameraView.this) {

            public void onSwipeRight() {
                Intent i = new Intent(CameraView.this, ChatList.class);
                startActivity(i);
            }
            public void onSwipeLeft() {
                Intent i = new Intent(CameraView.this, Stories.class);
                startActivity(i);
            }

        });


        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        initialize();
    }




    // a method that initialized all the existing buttons and available functions on camera page
    // and handles their clicks
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

                imageLayout.setDrawingCacheEnabled(true);
                Bitmap tempImage = Bitmap.createBitmap(imageLayout.getDrawingCache());
                imageLayout.setDrawingCacheEnabled(false);


                FragmentManager fm = getFragmentManager();
                ShareImageDialog imageDialog = new ShareImageDialog(tempImage);
                imageDialog.show(fm, "Share Image");


            }
        });


        btnAddFriend = (Button) findViewById(R.id.btnAddFriend);
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                AddFriendDialog addFriendDialog = new AddFriendDialog();
                addFriendDialog.show(fm, "Add Friend");

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


        btnErase = (Button) findViewById(R.id.btnErase);
        btnErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (drawing.draw) {
                    drawingPad.removeView(drawing);
                    drawing.reset();
                    allColors.setVisibility(View.GONE);
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



        imageSmiling = (Button) findViewById(R.id.imageSmiling);
        imageSmiling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageLayout.indexOfChild(imageSmilingView) == -1) {
                    imageSmilingView.setImageResource(R.drawable.smiling);
                    imageSmilingView.setLayoutParams(new ViewGroup.LayoutParams(100,100));
                    imageSmilingView.setX(100);
                    imageSmilingView.setY(100);
                    imageLayout.addView(imageSmilingView);
                }

                else {
                    imageSmilingView.setImageBitmap(null);
                    imageSmilingView.invalidate();
                    imageLayout.removeView(imageSmilingView);
                }
            }
        });


        imageSmilingView.setOnTouchListener(new View.OnTouchListener() {
            float xPosition, yPosition;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xPosition = view.getX() - event.getRawX();
                        yPosition = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        imageSmilingView.setX(event.getRawX() + xPosition);
                        imageSmilingView.setY(event.getRawY() + yPosition);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });




        imageLaughing = (Button) findViewById(R.id.imageLaughing);
        imageLaughing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageLayout.indexOfChild(imageLaughingView) == -1) {
                    imageLaughingView.setImageResource(R.drawable.laughing);
                    imageLaughingView.setLayoutParams(new ViewGroup.LayoutParams(100,100));
                    imageLaughingView.setX(200);
                    imageLaughingView.setY(100);
                    imageLayout.addView(imageLaughingView);
                }

                else {
                    imageLaughingView.setImageBitmap(null);
                    imageLaughingView.invalidate();
                    imageLayout.removeView(imageLaughingView);
                }
            }
        });


        imageLaughingView.setOnTouchListener(new View.OnTouchListener() {
            float xPosition, yPosition;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xPosition = view.getX() - event.getRawX();
                        yPosition = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        imageLaughingView.setX(event.getRawX() + xPosition);
                        imageLaughingView.setY(event.getRawY() + yPosition);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });




        imageSad = (Button) findViewById(R.id.imageSad);
        imageSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageLayout.indexOfChild(imageSadView) == -1) {
                    imageSadView.setImageResource(R.drawable.sad);
                    imageSadView.setLayoutParams(new ViewGroup.LayoutParams(100,100));
                    imageSadView.setX(300);
                    imageSadView.setY(100);
                    imageLayout.addView(imageSadView);
                }

                else {
                    imageSadView.setImageBitmap(null);
                    imageSadView.invalidate();
                    imageLayout.removeView(imageSadView);
                }
            }
        });


        imageSadView.setOnTouchListener(new View.OnTouchListener() {
            float xPosition, yPosition;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xPosition = view.getX() - event.getRawX();
                        yPosition = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        imageSadView.setX(event.getRawX() + xPosition);
                        imageSadView.setY(event.getRawY() + yPosition);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });



        imageAngry = (Button) findViewById(R.id.imageAngry);
        imageAngry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageLayout.indexOfChild(imageAngryView) == -1) {
                    imageAngryView.setImageResource(R.drawable.angry);
                    imageAngryView.setLayoutParams(new ViewGroup.LayoutParams(100,100));
                    imageAngryView.setX(100);
                    imageAngryView.setY(200);
                    imageLayout.addView(imageAngryView);
                }

                else {
                    imageAngryView.setImageBitmap(null);
                    imageAngryView.invalidate();
                    imageLayout.removeView(imageAngryView);
                }
            }
        });


        imageAngryView.setOnTouchListener(new View.OnTouchListener() {
            float xPosition, yPosition;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xPosition = view.getX() - event.getRawX();
                        yPosition = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        imageAngryView.setX(event.getRawX() + xPosition);
                        imageAngryView.setY(event.getRawY() + yPosition);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });



        imageTeasing = (Button) findViewById(R.id.imageTeasing);
        imageTeasing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageLayout.indexOfChild(imageTeasingView) == -1) {
                    imageTeasingView.setImageResource(R.drawable.teasing);
                    imageTeasingView.setLayoutParams(new ViewGroup.LayoutParams(100,100));
                    imageTeasingView.setX(200);
                    imageTeasingView.setY(200);
                    imageLayout.addView(imageTeasingView);
                }

                else {
                    imageTeasingView.setImageBitmap(null);
                    imageTeasingView.invalidate();
                    imageLayout.removeView(imageTeasingView);
                }
            }
        });


        imageTeasingView.setOnTouchListener(new View.OnTouchListener() {
            float xPosition, yPosition;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xPosition = view.getX() - event.getRawX();
                        yPosition = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        imageTeasingView.setX(event.getRawX() + xPosition);
                        imageTeasingView.setY(event.getRawY() + yPosition);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });



        imageInLove = (Button) findViewById(R.id.imageInLove);
        imageInLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageLayout.indexOfChild(imageInLoveView) == -1) {
                    imageInLoveView.setImageResource(R.drawable.inlove);
                    imageInLoveView.setLayoutParams(new ViewGroup.LayoutParams(100,100));
                    imageInLoveView.setX(300);
                    imageInLoveView.setY(200);
                    imageLayout.addView(imageInLoveView);
                }

                else {
                    imageInLoveView.setImageBitmap(null);
                    imageInLoveView.invalidate();
                    imageLayout.removeView(imageInLoveView);
                }


            }
        });


        imageInLoveView.setOnTouchListener(new View.OnTouchListener() {
            float xPosition, yPosition;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xPosition = view.getX() - event.getRawX();
                        yPosition = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        imageInLoveView.setX(event.getRawX() + xPosition);
                        imageInLoveView.setY(event.getRawY() + yPosition);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });




        imageCry = (Button) findViewById(R.id.imageCry);
        imageCry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageLayout.indexOfChild(imageCryView) == -1) {
                    imageCryView.setImageResource(R.drawable.cry);
                    imageCryView.setLayoutParams(new ViewGroup.LayoutParams(100,100));
                    imageCryView.setX(100);
                    imageCryView.setY(300);
                    imageLayout.addView(imageCryView);
                }

                else {
                    imageCryView.setImageBitmap(null);
                    imageCryView.invalidate();
                    imageLayout.removeView(imageCryView);
                }


            }
        });


        imageCryView.setOnTouchListener(new View.OnTouchListener() {
            float xPosition, yPosition;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xPosition = view.getX() - event.getRawX();
                        yPosition = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        imageCryView.setX(event.getRawX() + xPosition);
                        imageCryView.setY(event.getRawY() + yPosition);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });


        imageLaughCry = (Button) findViewById(R.id.imageLaughCry);
        imageLaughCry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageLayout.indexOfChild(imageLaughCryView) == -1) {
                    imageLaughCryView.setImageResource(R.drawable.laugh_cry);
                    imageLaughCryView.setLayoutParams(new ViewGroup.LayoutParams(100,100));
                    imageLaughCryView.setX(200);
                    imageLaughCryView.setY(300);
                    imageLayout.addView(imageLaughCryView);
                }

                else {
                    imageLaughCryView.setImageBitmap(null);
                    imageLaughCryView.invalidate();
                    imageLayout.removeView(imageLaughCryView);
                }


            }
        });


        imageLaughCryView.setOnTouchListener(new View.OnTouchListener() {
            float xPosition, yPosition;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xPosition = view.getX() - event.getRawX();
                        yPosition = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        imageLaughCryView.setX(event.getRawX() + xPosition);
                        imageLaughCryView.setY(event.getRawY() + yPosition);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });




        imageCool = (Button) findViewById(R.id.imageCool);
        imageCool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageLayout.indexOfChild(imageCoolView) == -1) {
                    imageCoolView.setImageResource(R.drawable.cool);
                    imageCoolView.setLayoutParams(new ViewGroup.LayoutParams(100,100));
                    imageCoolView.setX(300);
                    imageCoolView.setY(300);
                    imageLayout.addView(imageCoolView);
                }

                else {
                    imageCoolView.setImageBitmap(null);
                    imageCoolView.invalidate();
                    imageLayout.removeView(imageCoolView);
                }


            }
        });


        imageCoolView.setOnTouchListener(new View.OnTouchListener() {
            float xPosition, yPosition;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xPosition = view.getX() - event.getRawX();
                        yPosition = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        imageCoolView.setX(event.getRawX() + xPosition);
                        imageCoolView.setY(event.getRawY() + yPosition);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });



        imageKiss = (Button) findViewById(R.id.imageKiss);
        imageKiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageLayout.indexOfChild(imageKissView) == -1) {
                    imageKissView.setImageResource(R.drawable.kiss);
                    imageKissView.setLayoutParams(new ViewGroup.LayoutParams(100,100));
                    imageKissView.setX(100);
                    imageKissView.setY(400);
                    imageLayout.addView(imageKissView);
                }

                else {
                    imageKissView.setImageBitmap(null);
                    imageKissView.invalidate();
                    imageLayout.removeView(imageKissView);
                }


            }
        });


        imageKissView.setOnTouchListener(new View.OnTouchListener() {
            float xPosition, yPosition;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xPosition = view.getX() - event.getRawX();
                        yPosition = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        imageKissView.setX(event.getRawX() + xPosition);
                        imageKissView.setY(event.getRawY() + yPosition);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });




        imageShocked = (Button) findViewById(R.id.imageShocked);
        imageShocked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageLayout.indexOfChild(imageShockedView) == -1) {
                    imageShockedView.setImageResource(R.drawable.shocked);
                    imageShockedView.setLayoutParams(new ViewGroup.LayoutParams(100,100));
                    imageShockedView.setX(200);
                    imageShockedView.setY(400);
                    imageLayout.addView(imageShockedView);
                }

                else {
                    imageShockedView.setImageBitmap(null);
                    imageShockedView.invalidate();
                    imageLayout.removeView(imageShockedView);
                }


            }
        });


        imageShockedView.setOnTouchListener(new View.OnTouchListener() {
            float xPosition, yPosition;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xPosition = view.getX() - event.getRawX();
                        yPosition = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        imageShockedView.setX(event.getRawX() + xPosition);
                        imageShockedView.setY(event.getRawY() + yPosition);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });




        imageShit = (Button) findViewById(R.id.imageShit);
        imageShit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageLayout.indexOfChild(imageShitView) == -1) {
                    imageShitView.setImageResource(R.drawable.shit);
                    imageShitView.setLayoutParams(new ViewGroup.LayoutParams(100,100));
                    imageShitView.setX(300);
                    imageShitView.setY(400);
                    imageLayout.addView(imageShitView);
                }

                else {
                    imageShitView.setImageBitmap(null);
                    imageShitView.invalidate();
                    imageLayout.removeView(imageShitView);
                }


            }
        });


        imageShitView.setOnTouchListener(new View.OnTouchListener() {
            float xPosition, yPosition;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xPosition = view.getX() - event.getRawX();
                        yPosition = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        imageShitView.setX(event.getRawX() + xPosition);
                        imageShitView.setY(event.getRawY() + yPosition);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

    }






    // a method to handle taking a photo by camera
    public void takePhoto() {
        camera.takePicture(new Camera.ShutterCallback() {
            @Override
            public void onShutter() {

            }
        }, null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                int orientation = 0;

                int screenOrientation = getResources().getConfiguration().orientation;

                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                camera.stopPreview();

                if (isBackCamera) {
                    if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
                        orientation = 90;
                    }
                    else {
                        orientation = 180;
                    }

                }
                else if (!isBackCamera){
                    if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
                        orientation = 270;
                    }
                    else {
                        orientation = 180;
                    }
                }


                photo = rotateImage(bitmap, orientation);
                capturedImageView.setImageBitmap(photo);
                takePhotoOptions();

            }
        });
    }



    // a method that saves the images(from camera's surface view) to local storage
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
            finalImage.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
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




    // a method to handles switching camera to front or back facing camera
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
            btnFlashOff.setVisibility(View.VISIBLE);
        }
        else {
            cameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;
            btnFlashOff.setVisibility(View.GONE);
            btnFlash.setVisibility(View.GONE);
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



    // a method to handle turning the camera flash on or off if supported on the camera
    public void switchFlash() {

        parameters = camera.getParameters();

        if (parameters.getSupportedFlashModes() != null) {
            if (parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                camera.setParameters(parameters);
                btnFlashOff.setVisibility(View.GONE);
                btnFlash.setVisibility(View.VISIBLE);
            }
            else {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                btnFlash.setVisibility(View.GONE);
                btnFlashOff.setVisibility(View.VISIBLE);
            }
        }

    }



    // restarting the camera(resuming the preview) after the preview has stopped
    private void restartCamera() {

        restartCameraOptions();
        removeSmileys();
        camera.startPreview();

    }




    // a method that handles screen rotation and matches it to the camera's view(rotates the camera
    // view according to the screen's rotation)
    public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degree) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degree + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }



    // a method to hide the soft keyboard on screen when required
    private void hideKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    // a method that rotates the bitmap to an arbitrary degree
    private static Bitmap rotateImage(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }



    // removing the pictograms(smileys) from the surface after the camera is restarted
    private void removeSmileys() {
        imageSmilingView.setImageBitmap(null);
        imageSmilingView.invalidate();
        imageAngryView.setImageBitmap(null);
        imageAngryView.invalidate();
        imageSadView.setImageBitmap(null);
        imageSadView.invalidate();
        imageLaughingView.setImageBitmap(null);
        imageLaughingView.invalidate();
        imageInLoveView.setImageBitmap(null);
        imageInLoveView.invalidate();
        imageTeasingView.setImageBitmap(null);
        imageTeasingView.invalidate();
        imageCryView.setImageBitmap(null);
        imageCryView.invalidate();
        imageLaughCryView.setImageBitmap(null);
        imageLaughCryView.invalidate();
        imageShitView.setImageBitmap(null);
        imageShitView.invalidate();
        imageShockedView.setImageBitmap(null);
        imageShockedView.invalidate();
        imageCoolView.setImageBitmap(null);
        imageCoolView.invalidate();
        imageKissView.setImageBitmap(null);
        imageKissView.invalidate();
        imageLayout.removeView(imageSmilingView);
        imageLayout.removeView(imageAngryView);
        imageLayout.removeView(imageSadView);
        imageLayout.removeView(imageInLoveView);
        imageLayout.removeView(imageLaughingView);
        imageLayout.removeView(imageTeasingView);
        imageLayout.removeView(imageKissView);
        imageLayout.removeView(imageShockedView);
        imageLayout.removeView(imageCoolView);
        imageLayout.removeView(imageShitView);
        imageLayout.removeView(imageLaughCryView);
        imageLayout.removeView(imageCryView);
    }


    // a method to handle what options need to be shown on screen when a photo is taken
    private void takePhotoOptions() {
        btnTakePhoto.setVisibility(View.GONE);
        btnAddFriend.setVisibility(View.GONE);
        btnSavePhoto.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.VISIBLE);
        btnFlashOff.setVisibility(View.GONE);
        btnFlash.setVisibility(View.GONE);
        btnSwitchCamera.setVisibility(View.GONE);
        btnDraw.setVisibility(View.VISIBLE);
        btnErase.setVisibility(View.VISIBLE);
        btnText.setVisibility(View.VISIBLE);
        btnSmiley.setVisibility(View.VISIBLE);
        surfaceView.setVisibility(View.GONE);
        drawingPad.setVisibility(View.VISIBLE);
    }


    // a method to handle what options need to be shown on screen when the camera is restarted
    private void restartCameraOptions () {
        btnSavePhoto.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);
        btnSend.setVisibility(View.GONE);
        btnFlashOff.setVisibility(View.VISIBLE);
        btnFlash.setVisibility(View.GONE);
        btnSwitchCamera.setVisibility(View.VISIBLE);
        btnTakePhoto.setVisibility(View.VISIBLE);
        btnAddFriend.setVisibility(View.VISIBLE);
        btnDraw.setVisibility(View.GONE);
        btnErase.setVisibility(View.GONE);
        btnText.setVisibility(View.GONE);
        btnSmiley.setVisibility(View.GONE);
        allColors.setVisibility(View.GONE);
        allEmojis.setVisibility(View.GONE);

        surfaceView.setVisibility(View.VISIBLE);
        capturedImageView.setImageBitmap(null);
        capturedImageView.invalidate();

        if (drawing.draw) {
            drawing.reset();
        }

        drawingPad.removeView(drawing);
        drawingPad.setVisibility(View.GONE);
        text.getText().clear();
        text.setVisibility(View.GONE);
    }



    // a method to handle releasing the camera on activity pause
    @Override
    protected void onPause() {
        super.onPause();
        camera.release();
    }


    // a method that handles what needs to be shown if activity is resumed(or started) from specific
    // activity(in this instance from friendslist page)
    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent().getExtras() != null) {
            FragmentManager fm = getFragmentManager();
            AddFriendDialog addFriendDialog = new AddFriendDialog();
            addFriendDialog.show(fm, "Add Friend");
        }
    }


    // a method that initializes the camera surface view when its created(setting camera parameters)
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        if (camera != null) {
            camera.release();
            camera = null;
        }

        int facing = 0;

        if (isBackCamera) {
            facing = Camera.CameraInfo.CAMERA_FACING_BACK;
            camera = Camera.open(facing);
            parameters = camera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            btnFlashOff.setVisibility(View.VISIBLE);

        } else {
            facing = Camera.CameraInfo.CAMERA_FACING_FRONT;
            camera = Camera.open(facing);
            parameters = camera.getParameters();
            btnFlashOff.setVisibility(View.GONE);
        }




        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        setCameraDisplayOrientation(this, facing, camera);
        camera.startPreview();

    }



    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }


    // handling releasing the camera when the surface is destroyed
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        camera.release();
    }

}