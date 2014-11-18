package br.com.tolive.simplewallet.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import br.com.tolive.simplewallet.app.R;

public class CircleView extends CustomTextView {
	private ShapeDrawable mDrawable;
	//private Context mContext;
	
    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setBackgroundColor(context.getResources().getColor(android.R.color.white));

        mDrawable = new ShapeDrawable(new OvalShape());
		setColor(context.getResources().getColor(R.color.snow));
		//setBounds(10, 10, 300, 50);
    }
	
	/**
	 * Set CircleView color
	 * 
	 * @param color
	 * 		Pass resolved color instead of resource id here: getResources().getColor(android.R.color.black)
	 */
	public void setColor(int color) {
		mDrawable.getPaint().setColor(/*0xff74AC23*/color);
		invalidate();
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if(changed){
			Log.d("TAG", "left: " + left + " top: " + top + " right: " + right + " bottom: " + bottom);
			setBounds(0 + 10, 0 + 10, (right - left) - 10, (bottom - top) - 10);
		}
	}

//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		// TODO Auto-generated method stub
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		setBounds(0 + 10, 0 + 10, widthMeasureSpec - 10, heightMeasureSpec - 10);
//	}
	
	/**
	 * Set CircleView Bounds. This is where the drawable will draw when its draw() method is called.
	 * 
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void setBounds(int left, int top, int right, int bottom){
		mDrawable.setBounds(left, top, right, bottom);
	}
	
	protected void onDraw(Canvas canvas) {
		mDrawable.draw(canvas);
		super.onDraw(canvas);
	}
}
