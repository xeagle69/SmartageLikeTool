package com.example.smartageliketool.data.util;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * @author rajesh
 *
 */
public class MyWebView extends WebView {


    private GestureDetector gestureDetector;

    /**
     * @param context
     */
    public MyWebView(Context context) {
        super(context);
        gestureDetector = new GestureDetector(context, new GestureListener());

    }

    /**
     * @param context
     * @param attrs
     */
    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, new GestureListener());

    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        gestureDetector = new GestureDetector(context, new GestureListener());

    }


    public void simulateDoubleTap()
    {

        simulateDoubleTapEvent(0);
        simulateDoubleTapEvent(2);
        simulateDoubleTapEvent(2);
        simulateDoubleTapEvent(1);
        simulateDoubleTapEvent(0);
    }

    private void simulateDoubleTapEvent(int action)
    {
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        float x = 0.0f;
        float y = 0.0f;
        // List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
        int metaState = 0;
        MotionEvent me = MotionEvent.obtain(
                downTime,
                eventTime,
                action,
                x,
                y,
                metaState
        );
        dispatchTouchEvent(me);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return gestureDetector.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();

            Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");

            return true;
        }
    }

}

