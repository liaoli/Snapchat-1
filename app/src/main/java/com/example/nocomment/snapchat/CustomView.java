package com.example.nocomment.snapchat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by Sina on 9/24/2016.
 */

public class CustomView extends SurfaceView implements SurfaceHolder.Callback {


    SurfaceHolder mHolder;
    public Camera camera;
    CustomView(Context context) {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    public void surfaceCreated(final SurfaceHolder holder) {
        camera = Camera.open();
        try {
            camera.setPreviewDisplay(holder);
            camera.setPreviewCallback(new Camera.PreviewCallback() {
                public void onPreviewFrame(byte[] data, Camera arg1) {
                    invalidar();
                }
            });
        } catch (IOException e) {}
    }
    public void invalidar(){
        invalidate();
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(w, h);
        camera.setParameters(parameters);
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // nothing gets drawn :(
        Paint p = new Paint(Color.RED);
        canvas.drawText("PREVIEW", canvas.getWidth() / 2,
                canvas.getHeight() / 2, p);
    }
}


}
