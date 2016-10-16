package com.example.nocomment.snapchat;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import com.squareup.picasso.Transformation;

/**
 * Created by guomingsun on 16/10/16.
 */

public class ColorLayerImmutable implements Transformation {


    private final int destinationColor;
    public ColorLayerImmutable(int destinationColor) {
        this.destinationColor = destinationColor;
    }
    @Override public Bitmap transform(Bitmap source) {

        Bitmap drawableBitmap = source.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(drawableBitmap);

        int dr = Color.red(destinationColor);
        int dg = Color.green(destinationColor);
        int db = Color.blue(destinationColor);
        canvas.drawColor(Color.argb(80, dr, dg, db));
        source.recycle();
        return drawableBitmap;
        // 20 vs #%02x
    }
    @Override public String key() {
        String hexColor = String.format("#%08x", destinationColor);
        return "color-layer(destinationColor=" + hexColor + ")";
    }
}
