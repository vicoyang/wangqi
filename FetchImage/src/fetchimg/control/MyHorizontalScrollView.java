package fetchimg.control;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import fetchimg.adapter.HorizontalScrollViewAdapter;

public class MyHorizontalScrollView extends HorizontalScrollView implements
		OnClickListener {
	/**
	 * 图片滚动时的回调接口
	 * 
	 * @author zhy
	 * 
	 */
	public interface CurrentImageChangeListener {
		void onCurrentImgChanged(int position, View viewIndicator);
	}

	/**
	 * 条目点击时的回调
	 * 
	 * @author zhy
	 * 
	 */
	public interface OnItemClickListener {
		void onClick(View view, int pos);
	}

	private CurrentImageChangeListener mListener;

	private OnItemClickListener mOnClickListener;

	private static final String TAG = "MyHorizontalScrollView";

	private LinearLayout mContainer;

	private int mChildWidth;

	private int mChildHeight;

	private int mCurrentIndex;

	private int mFristIndex;

	private View mFirstView;

	private HorizontalScrollViewAdapter mAdapter;

	private int mCountOneScreen;

	private int mScreenWitdh;

	private Map<View, Integer> mViewPos = new HashMap<View, Integer>();

	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 获得屏幕宽度
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWitdh = outMetrics.widthPixels;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mContainer = (LinearLayout) getChildAt(0);
	}

	protected void loadNextImg() {
		if (mCurrentIndex == mAdapter.getCount() - 1) {
			return;
		}
		scrollTo(0, 0);
		mViewPos.remove(mContainer.getChildAt(0));
		mContainer.removeViewAt(0);
		View view = mAdapter.getView(++mCurrentIndex, null, mContainer);
		view.setOnClickListener(this);
		mContainer.addView(view);
		mViewPos.put(view, mCurrentIndex);
		mFristIndex++;
		if (mListener != null) {
			notifyCurrentImgChanged();
		}

	}

	protected void loadPreImg() {
		if (mFristIndex == 0)
			return;
		int index = mCurrentIndex - mCountOneScreen;
		if (index >= 0) {
			int oldViewPos = mContainer.getChildCount() - 1;
			mViewPos.remove(mContainer.getChildAt(oldViewPos));
			mContainer.removeViewAt(oldViewPos);
			View view = mAdapter.getView(index, null, mContainer);
			mViewPos.put(view, index);
			mContainer.addView(view, 0);
			view.setOnClickListener(this);
			scrollTo(mChildWidth, 0);
			mCurrentIndex--;
			mFristIndex--;
			if (mListener != null) {
				notifyCurrentImgChanged();

			}
		}
	}

	public void notifyCurrentImgChanged() {
		for (int i = 0; i < mContainer.getChildCount(); i++) {
			mContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
		}

		mListener.onCurrentImgChanged(mFristIndex, mContainer.getChildAt(0));

	}

	public void initDatas(HorizontalScrollViewAdapter mAdapter) {
		this.mAdapter = mAdapter;
		mContainer = (LinearLayout) getChildAt(0);
		final View view = mAdapter.getView(0, null, mContainer);
		mContainer.addView(view);
		if (mChildWidth == 0 && mChildHeight == 0) {
			int w = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			int h = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			view.measure(w, h);
			mChildHeight = view.getMeasuredHeight();
			mChildWidth = view.getMeasuredWidth();
			Log.e(TAG, view.getMeasuredWidth() + "," + view.getMeasuredHeight());
			mChildHeight = view.getMeasuredHeight();
			mCountOneScreen = mScreenWitdh / mChildWidth + 2;

			Log.e(TAG, "mCountOneScreen = " + mCountOneScreen
					+ " ,mChildWidth = " + mChildWidth);

		}
		initFirstScreenChildren(mCountOneScreen);
	}

	public void initFirstScreenChildren(int mCountOneScreen) {
		mContainer = (LinearLayout) getChildAt(0);
		mContainer.removeAllViews();
		mViewPos.clear();

		for (int i = 0; i < mCountOneScreen; i++) {
			View view = mAdapter.getView(i, null, mContainer);
			view.setOnClickListener(this);
			mContainer.addView(view);
			mViewPos.put(view, i);
			mCurrentIndex = i;
		}

		if (mListener != null) {
			notifyCurrentImgChanged();
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_MOVE:
			// Log.e(TAG, getScrollX() + "");

			int scrollX = getScrollX();
			// 如果当前scrollX为view的宽度，加载下一张，移除第一张
			if (scrollX >= mChildWidth) {
				loadNextImg();
			}
			// 如果当前scrollX = 0， 往前设置一张，移除最后一张
			if (scrollX == 0) {
				loadPreImg();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void onClick(View v) {
		if (mOnClickListener != null) {
			for (int i = 0; i < mContainer.getChildCount(); i++) {
				mContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
			}
			mOnClickListener.onClick(v, mViewPos.get(v));
		}
	}

	public void setOnItemClickListener(OnItemClickListener mOnClickListener) {
		this.mOnClickListener = mOnClickListener;
	}

	public void setCurrentImageChangeListener(
			CurrentImageChangeListener mListener) {
		this.mListener = mListener;
	}

}
