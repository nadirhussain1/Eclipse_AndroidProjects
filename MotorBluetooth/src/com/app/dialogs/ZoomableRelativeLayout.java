package com.app.dialogs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewParent;
import android.widget.RelativeLayout;

import com.app.utilities.SharedData;

public class ZoomableRelativeLayout extends RelativeLayout {

	String TAG = "ZoomableRelativeLayout";

	static float mScaleFactor = 1.0f;

	// Maximum and Minimum Zoom
	private static float MIN_ZOOM = 1.0f;
	private static float MAX_ZOOM = 3.0f;

	// Track the Bound of the Image after zoom to calculate the offset
	static Rect mClipBound;

	// mDetector to detect the scaleGesture for the pinch Zoom
	private ScaleGestureDetector mScaleDetector;

	private static final int INVALID_POINTER_ID = -1;

	// The ‘active pointer’ is the one currently moving our object.
	private int mActivePointerId = INVALID_POINTER_ID;

	private float mPosX;
	private float mPosY;

	private float mLastTouchX;
	private float mLastTouchY;

	// Pivot point for Scaling
	static float gx = 0, gy = 0;

	public ZoomableRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode()) {
			setWillNotDraw(false);
			mClipBound = new Rect();
			// Intialize ScaleGestureDetector
			mScaleDetector = new ScaleGestureDetector(getContext(),
					new ZoomListener());
		}

	}

	public ZoomableRelativeLayout(Context context) {
		super(context);
		if (!isInEditMode()) {
			setWillNotDraw(false);
			mClipBound = new Rect();
			// Intialize ScaleGestureDetector
			mScaleDetector = new ScaleGestureDetector(getContext(),
					new ZoomListener());
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		mScaleDetector.onTouchEvent(ev);
		// Handles all type of motion-events possible
		final int action = ev.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {
			final float x = ev.getX();
			final float y = ev.getY();

			mLastTouchX = x;
			mLastTouchY = y;
			mActivePointerId = ev.getPointerId(0);
			break;
		}

		case MotionEvent.ACTION_MOVE: {
			if (mScaleFactor == 1.0f)
				break;
			final int pointerIndex = ev.findPointerIndex(mActivePointerId);
			final float x = ev.getX(pointerIndex);
			final float y = ev.getY(pointerIndex);

			// Only move if the ScaleGestureDetector isn't processing a gesture.
			if (!mScaleDetector.isInProgress()) {

				final float dx = x - mLastTouchX;
				final float dy = y - mLastTouchY;

				mPosX += dx;
				mPosY += dy;

				if (mScaleFactor == 1.0f) {
					SharedData.isPagerEnabled = true;
					break;
				} else {
					SharedData.isPagerEnabled = false;
				}

				if (mScaleFactor != 1.0f)
					invalidate();
			}

			mLastTouchX = x;
			mLastTouchY = y;

			break;
		}

		case MotionEvent.ACTION_CANCEL: {
			mActivePointerId = INVALID_POINTER_ID;
			break;
		}

		case MotionEvent.ACTION_UP:
			mActivePointerId = INVALID_POINTER_ID;
			break;

		case MotionEvent.ACTION_POINTER_UP: {
			final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
			final int pointerId = ev.getPointerId(pointerIndex);
			if (pointerId == mActivePointerId) {
				// This was our active pointer going up. Choose a new
				// active pointer and adjust accordingly.
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				mLastTouchX = ev.getX(newPointerIndex);
				mLastTouchY = ev.getY(newPointerIndex);
				mActivePointerId = ev.getPointerId(newPointerIndex);
			}
			break;
		}
		}

		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			onTouchEvent(ev);
			return super.onInterceptTouchEvent(ev);

		} catch (Exception e) {
			Log.i("ZoomableRelLayout-exception", "" + e.getMessage());
		}
		return false;
	}

	@Override
	public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
		return super.invalidateChildInParent(location, dirty);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			if (child.getVisibility() != GONE) {
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) child
						.getLayoutParams();
				child.layout((int) (params.leftMargin),
						(int) (params.topMargin),
						(int) (params.leftMargin + child.getMeasuredWidth()),
						(int) (params.topMargin + child.getMeasuredHeight()));
			}
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		// Save the canvas to set the scaling factor returned from detector
		canvas.save();

		if (mScaleFactor == 1.0f) {
			SharedData.isPagerEnabled = true;
		} else {
			SharedData.isPagerEnabled = false;
		}

		if (mScaleFactor == 1.0f) {
			mPosX = 0.0f;
			mPosY = 0.0f;
		}

		canvas.translate(mPosX, mPosY);

		canvas.scale(mScaleFactor, mScaleFactor, gx, gy);
		super.dispatchDraw(canvas);

		mClipBound = canvas.getClipBounds();
		canvas.restore();
	}

	private class ZoomListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			// getting the scaleFactor from the detector
			mScaleFactor *= detector.getScaleFactor();
			gx = detector.getFocusX();
			gy = detector.getFocusY();
			// Limit the scale factor in the MIN and MAX bound
			mScaleFactor = Math.max(Math.min(mScaleFactor, MAX_ZOOM), MIN_ZOOM);
			// Here we are only zooming so invalidate has to be done
			invalidate();
			requestLayout();
			return true;
		}
	}

}