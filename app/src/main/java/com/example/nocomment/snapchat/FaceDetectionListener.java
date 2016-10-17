package com.example.nocomment.snapchat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Camera;
import android.util.Log;
import android.view.View;



/**
 * Created by Sina on 10/17/2016.
 * A class to handle camera face recongition by listening if a face was recognized and drawing a
 * bounding box around it.
 */

public class FaceDetectionListener extends View implements Camera.FaceDetectionListener {

    private Paint mPaint;
    private Camera.Face[] detectedFaces;
    private int mOrientation;
    private int mDisplayOrientation;



    public FaceDetectionListener(Context context) {
        super(context);
        initialize();
    }


    // creating a green bounding box around the faces
    private void initialize() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setAlpha(100);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
    }


    // put the detected faces into detectedFaces array
    public void setFaces(Camera.Face[] faces) {
        detectedFaces = faces;
        invalidate();
    }


    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {

        if (faces.length > 0) {

            for (int i=0; i < faces.length; i++) {
                setFaces(faces);
            }
        }
    }

    // drawing the faces on canvas
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (detectedFaces != null && detectedFaces.length > 0) {
            Matrix matrix = new Matrix();
            Util.prepareMatrix(matrix, false, mDisplayOrientation, getWidth(), getHeight());
            canvas.save();
            matrix.postRotate(mOrientation);
            canvas.rotate(-mOrientation);
            RectF rectF = new RectF();
            for (Camera.Face face : detectedFaces) {
                rectF.set(face.rect);
                matrix.mapRect(rectF);
                canvas.drawRect(rectF, mPaint);
            }
            canvas.restore();
        }
    }

}
