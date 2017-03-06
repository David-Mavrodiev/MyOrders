package com.topoffers.topoffers.common.helpers;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListView;

/**
 * Created by Simeon on 28.2.2017 г..
 */

public class OnSwipeTouchListener implements OnTouchListener {
    ListView list;
    private Context context;
    private final GestureDetector gestureDetector = new GestureDetector(new GestureListener());

    public OnSwipeTouchListener(Context ctx, ListView list) {
        context = ctx;
        this.list = list;
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        private int getPosition(MotionEvent e1) {
            return list.pointToPosition((int) e1.getX(), (int) e1.getY());
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight(getPosition(e1));
                        } else {
                            onSwipeLeft(getPosition(e1));
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom(getPosition(e1));
                        } else {
                            onSwipeTop(getPosition(e1));
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight(int position) {}
    public void onSwipeLeft(int position) {}
    public void onSwipeTop(int position) {}
    public void onSwipeBottom(int position) {}
}
