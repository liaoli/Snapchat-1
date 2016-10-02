package com.example.nocomment.snapchat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Sina on 10/1/2016.
 */

public class HandDrawing extends View {



    Paint paint;
    Bitmap bitmap;
    Canvas canvas;
    Path path;
    Paint bitmapPaint;
    boolean draw = false;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;



    public HandDrawing(Context context) {
        super(context);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(0xFFFF0000);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(10);

        path = new Path();
        bitmapPaint = new Paint();
        bitmapPaint.setColor(Color.RED);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
        canvas.drawPath(path, paint);
    }


    private void touch_start(float x, float y) {
        path.moveTo(x, y);
        mX = x;
        mY = y;
    }


    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }


    private void touch_up() {
        path.lineTo(mX, mY);
        canvas.drawPath(path, paint);
        path.reset();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }


    public void setColor(int newColor) {
        paint.setColor(newColor);
        invalidate();
    }


    public void reset() {
        this.bitmap.eraseColor(Color.TRANSPARENT);
        this.path.reset();
        draw = false;
        invalidate();
    }


}
