package com.one.daybook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by one on 11/13/2015.
 */
public class notes_DrawingView extends View {

    private Path drawPath;
    //drawing and canvas notes_paint
    static Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0x000000, paintAlpha = 255;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
  //  private Bitmap canvasBitmap;

    public static Bitmap canvasBitmap;
    //brush sizes
    private float brushSize;
    private float lastBrushSize;
    //erase flag
    private boolean erase=false;


    public notes_DrawingView(Context context) {
        super(context);
        setupDrawing();
    }


    private void setupDrawing() {
        float stuartSize = 2;
        brushSize = getResources().getInteger(R.integer.medium_size);
       // lastBrushSize = brushSize;
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(stuartSize);

        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    //draw the view - will be called after touch event
    @Override
    protected void onDraw(Canvas canvas) {

           canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
            canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        //respond to down, move and up events
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);

                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);

                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX, touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath = new Path();
                break;
            default:
                return false;
        }
        //redraw
       invalidate();
        return true;

    }

    public void setColor(String newColor){
        invalidate();

            paintColor = Color.parseColor(newColor);
            drawPaint.setColor(paintColor);
            drawPaint.setShader(null);
    }


    public void setBrushSize(float newSize){
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    //get and set last brush size
    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }
    public float getLastBrushSize(){
        return lastBrushSize;
    }

    //set erase true or false
    public void setErase(boolean isErase){
        erase=isErase;
        if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);
        invalidate();
    }

    //start new drawing
    public void startNew() {

        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    //return current alpha


    //set alpha
    public void setPaintAlpha(int newAlpha){
        paintAlpha= Math.round((float)newAlpha/100*255);
        drawPaint.setColor(paintColor);
        drawPaint.setAlpha(paintAlpha);
    }

}


/*  case Mathsin notes_MainActivity.ajva
{

                drawView.setDrawingCacheEnabled(true);

                Bitmap bitmap= drawView.getDrawingCache();

                String path = Environment.getExternalStorageDirectory().toString();

                OutputStream fOut = null;
                File file = new File(path, "/Maths/math_"+UUID.randomUUID().toString()+".png");
                try {
                    fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 10, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                drawView.destroyDrawingCache();
                //drawView.startNew();
                return true;
            }*/