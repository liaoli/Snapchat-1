package com.example.nocomment.snapchat;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.graphics.Bitmap.createBitmap;

/**
 * Created by guomingsun on 16/10/16.
 */

public class ColorLayer implements Transformation {
    private final int destinationColor;
    public ColorLayer(int destinationColor) {
        this.destinationColor = destinationColor;
    }
    @Override public Bitmap transform(Bitmap source) {



        Canvas canvas = new Canvas(source);

        int dr = Color.red(destinationColor);
        int dg = Color.green(destinationColor);
        int db = Color.blue(destinationColor);
        canvas.drawColor(Color.argb(80, dr, dg, db));
        return source;
        // 20 vs #%02x
    }
    @Override public String key() {
        String hexColor = String.format("#%08x", destinationColor);
        return "color-layer(destinationColor=" + hexColor + ")";
    }
}