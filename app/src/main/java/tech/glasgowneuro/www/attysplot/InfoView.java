package tech.glasgowneuro.www.attysplot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by bp1 on 07/09/16.
 */
public class InfoView extends SurfaceView implements SurfaceHolder.Callback {

    static private String TAG = "InfoView";

    static private SurfaceHolder holder = null;
    static private Canvas canvas = null;
    static private Paint paintLarge = new Paint();
    static private Paint paintSmall = new Paint();

    static private int textHeight = 0;

    public InfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public InfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InfoView(Context context) {
        super(context);
        init();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(false);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    protected void onDraw(Canvas canvas) {
    }

    private void init() {
        holder = getHolder();
        holder.setFormat(PixelFormat.TRANSLUCENT);
        paintLarge.setColor(Color.argb(128, 0, 255, 0));
        paintSmall.setColor(Color.argb(128, 0, 255, 0));
    }

    public void removeText() {
        canvas = holder.lockCanvas();
        if (canvas != null) {
            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPaint(paint);
        }
        holder.unlockCanvasAndPost(canvas);
    }

    public int getInfoHeight() {
        return textHeight;
    }

    public synchronized void drawText(String largeText, String smallText) {
        if (canvas != null) return;
        Surface surface = holder.getSurface();
        int width = getWidth();
        int yLarge = 0;
        int xLarge = 0;
        if (surface.isValid()) {
            if (largeText != null) {
                Rect bounds = new Rect();
                int txtDiv = 7;
                do {
                    paintLarge.setTextSize(getHeight() / txtDiv);
                    paintLarge.getTextBounds(largeText, 0, largeText.length(), bounds);
                    xLarge = width - (bounds.width() * 10 / 9);
                    txtDiv++;
                } while (xLarge < 0);
                String dummyText = "1.2424Vpp";
                paintLarge.getTextBounds(dummyText, 0, dummyText.length(), bounds);
                yLarge = bounds.height();
            }
            int txtDiv = 25;
            Rect bounds = new Rect();
            do {
                paintSmall.setTextSize(getHeight() / txtDiv);
                paintSmall.getTextBounds(smallText, 0, smallText.length(), bounds);
                txtDiv++;
            } while ((width - (bounds.width() * 10 / 9)) < 0);
            int y2 = bounds.height();
            canvas = holder.lockCanvas();
            if (canvas != null) {
                Paint paint = new Paint();
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                canvas.drawPaint(paint);
                if (largeText != null) {
                    canvas.drawText(largeText, xLarge, yLarge + y2 * 10 / 9, paintLarge);
                }
                canvas.drawText(smallText, getWidth() / 100, y2, paintSmall);
            } else {
                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    Log.d(TAG, "Canvas==null");
                }
            }
            holder.unlockCanvasAndPost(canvas);
            canvas = null;
            textHeight = y2 + yLarge;
        }
    }
}
