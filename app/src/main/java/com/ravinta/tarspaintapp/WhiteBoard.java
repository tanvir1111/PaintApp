package com.ravinta.tarspaintapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class WhiteBoard extends View {
    public static float LARGE_BRUSH=16f;
    public static float MEDIUM_BRUSH=8f;
    public static float SMALL_BRUSH=4f;
    private Paint paint,canvasPaint;
    private Path path;
    private Canvas canvas;
    private Bitmap bitmap;
    private float brushSize;

    public WhiteBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialSetUp();
    }

    private void initialSetUp() {
        path=new Path();
        paint =new Paint();
        paint.setColor(0xFF000000);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(SMALL_BRUSH);
        canvasPaint = new Paint(Paint.DITHER_FLAG);

        paint.setStrokeJoin(Paint.Join.ROUND);
        setBrushSize(SMALL_BRUSH);

    }

    public void setBrushSize(float newBrushSize) {
        brushSize= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,newBrushSize,getResources().getDisplayMetrics());
        paint.setStrokeWidth(brushSize);

    }

    public void setErase(boolean mode){
        if(mode){
            canvasPaint.setColor(Color.WHITE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        else {

            paint.setXfermode(null);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        bitmap= Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        canvas=new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        canvas.drawBitmap(bitmap,0,0,canvasPaint);
        canvas.drawPath(path,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x=event.getX();
        float y=event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:

                path.moveTo(x,y);
                break;
                case MotionEvent.ACTION_MOVE:
                    path.lineTo(x,y);
                    break;
                case MotionEvent.ACTION_UP:
                    canvas.drawPath(path,paint);
                    path.reset();
                    break;
            default:
                return false;
        }
        invalidate();
        return  true;
    }

    public void setBrushColor(String color) {
        invalidate();
        setErase(false);

        paint.setColor(Color.parseColor(color));
    }

    public void reset() {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }
}
