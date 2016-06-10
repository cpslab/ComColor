package com.elzup.comcolor.views.fragments;

/**
 * Created by ren on 2016/06/08.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.elzup.comcolor.R;
import com.elzup.comcolor.models.ColorLogObject;


class WaveAnimationSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    public WaveAnimationSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    private Paint paint;
    private Rect rect, rect2;
    private Thread thread;
    private float w, h;
    private boolean mIsAttached;
    private Bitmap bmp = null, abmp = null;
    private int color, mergedColor, preColor;

    public void surfaceCreated(SurfaceHolder holder) {
        mIsAttached = true;
        thread = new Thread(this);

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        w = width;
        h = height;
        thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        while (thread != null && thread.isAlive()) ;
        mIsAttached = false;
        thread = null;
    }

    public void setColor(ColorLogObject mergedColor,ColorLogObject preColor) {
        this.color = mergedColor.getColor();
        this.mergedColor = mergedColor.getMergedColor();
        this.preColor = preColor.getColor();
    }

    public void run() {
        SurfaceHolder holder = getHolder();
        int t = 0;
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.wave);
        abmp = BitmapFactory.decodeResource(getResources(), R.drawable.wave);
        while (mIsAttached) {
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(preColor);

            if (t == 0) {
                canvas.drawColor(Color.WHITE);
            }

            //speed
            t -= 200;

            int ch = t + (int) h;
            if (ch < -4 * bmp.getHeight()) {
                ch = -4 * bmp.getHeight();
                mIsAttached = false;
            }
            //pre-combination wave
            for (int i = 0; i < w; i += bmp.getWidth()) {
                bmp = this.setColor(bmp, mergedColor);
                canvas.drawBitmap(bmp, i, ch, paint);
            }
            paint = new Paint();
            rect = new Rect(0, ch + bmp.getHeight(), (int) w, this.getHeight());
            paint.setColor(mergedColor);
            canvas.drawRect(rect, paint);

            //combination wave
            for (int i = 0; i < w; i += bmp.getWidth()) {
                abmp = this.setColor(abmp, color);
                canvas.drawBitmap(abmp, i, ch + 2 * bmp.getHeight(), paint);
            }
            rect2 = new Rect(0, ch + 3 * bmp.getHeight(), (int) w, this.getHeight() - t);
            paint.setColor(color);
            canvas.drawRect(rect2, paint);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private Bitmap setColor(Bitmap bitmap, int color) {
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        bitmap.recycle();
        Canvas myCanvas = new Canvas(mutableBitmap);
        int myColor = mutableBitmap.getPixel(0, 0);
        ColorFilter filter = new LightingColorFilter(myColor, color);
        Paint pnt = new Paint();
        pnt.setColorFilter(filter);
        myCanvas.drawBitmap(mutableBitmap, 0, 0, pnt);
        return mutableBitmap;
    }
}
