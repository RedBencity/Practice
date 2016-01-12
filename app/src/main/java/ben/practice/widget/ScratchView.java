package ben.practice.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import ben.practice.R;

/**
 * Created by Administrator on 2016/1/11 0011.
 */
public class ScratchView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private Paint paint;
    private Path path;
    private Canvas mCanvas;
    private Bitmap bgBitmap;
    private SurfaceHolder surfaceHolder;
    private boolean isDrawing;

    public ScratchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        path = new Path();
        paint.setColor(getResources().getColor(R.color.blank));
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
//        paint.setAlpha(0);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        paint.setStyle(Paint.Style.STROKE);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);

        setZOrderOnTop(true);
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);


    }

    public ScratchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScratchView(Context context) {
        super(context);
    }

    float x1 = 0;
    float y1 = 0;
    Path path1 = new Path();

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
//                System.out.println(x1 + " " + y1);
                path.reset();
                path.moveTo(event.getX(), event.getY());

                break;
            case MotionEvent.ACTION_MOVE:
//                System.out.println(x1 + " " + y1);
                final float x2 = (event.getX() + x1) / 2;
                final float y2 = (event.getY() + y1) / 2;
                path.quadTo(x1, y1, x2, y2);
//                path.quadTo(x2, y2, event.getX(),event.getY());
                x1 = event.getX();
                y1 = event.getY();
                break;
        }
        path1.addPath(path);
        invalidate();
        return true;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        while (isDrawing) {
            draw();
        }
        long end = System.currentTimeMillis();
        if (end - start < 100) {
            try {
                Thread.sleep(100 - (end - start));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void draw() {
        try {
            mCanvas = surfaceHolder.lockCanvas();
            mCanvas.drawPath(path, paint);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                surfaceHolder.unlockCanvasAndPost(mCanvas);

            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
    }
}
