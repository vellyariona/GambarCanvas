package com.example.gambarcanvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

public class MyCanvasView extends View {

//    membuat properties
    private Paint mPaint;
    private Path mPath;
    private int mDrawColor;
    private int mBackgroundColor;
    private Canvas mExtraCanvas;
    private Bitmap mExtraBitmap;
    private Rect mFrame;

//    menyimpan koordinat terakhir
    private float mX, mY;
//    mendetect perubahan minimal ada berapa
    private static final float TOUCH_TOLERANCE = 4;

    public MyCanvasView(Context context) {
//        mengganti return supaya otomatis memannggil constructor kedua
        this(context, null);

//        mendefine variabel
        mBackgroundColor = ResourcesCompat.getColor(
                getResources(), R.color.opaque_orange, null);
        mDrawColor = ResourcesCompat.getColor(
                getResources(), R.color.opaque_yellow, null);
        mPath = new Path();
        mPaint = new Paint();

        mPaint.setColor(mDrawColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
//        ujung
        mPaint.setStrokeCap(Paint.Cap.ROUND);
//        besar kecil kuas
        mPaint.setStrokeWidth(12);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mExtraBitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        mExtraCanvas = new Canvas(mExtraBitmap);
        mExtraCanvas.drawColor(mBackgroundColor);

        int inset = 40;
        mFrame = new Rect(inset, inset,w-inset,h-inset);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mExtraBitmap, 0, 0, null);
        canvas.drawRect(mFrame, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                touchUp();
                break;
            default:
        }
        return true;
    }

    private void touchStart(float x, float y){
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y){
    float dx = Math.abs(x - mX);
    float dy = Math.abs(y - mY);

    if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){
        mPath.quadTo(mX, mY, (x+mX)/2, (y+mY)/2);
        mX = x;
        mY = y;

        mExtraCanvas.drawPath(mPath, mPaint);
        }
    }

    private void touchUp(){
        mPath.reset();
    }

    public MyCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
