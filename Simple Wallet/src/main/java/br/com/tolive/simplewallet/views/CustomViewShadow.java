package br.com.tolive.simplewallet.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import br.com.tolive.simplewallet.app.R;
import br.com.tolive.simplewallet.utils.LayoutHelper;

public class CustomViewShadow extends LinearLayout {
    private Paint mButtonPaint;
    private String shadowOrientation;

    public CustomViewShadow(Context context) {
        this(context, null);
    }

    public CustomViewShadow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void setColor(int color){
        mButtonPaint.setColor(color);
        invalidate();
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomShadowView);
            shadowOrientation = a.getString(R.styleable.CustomShadowView_orientationShadow);

            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            mButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            if("vertical".equals(shadowOrientation)){
                mButtonPaint.setColor(context.getResources().getColor(R.color.snow));
                mButtonPaint.setStyle(Paint.Style.FILL);
                mButtonPaint.setShadowLayer(LayoutHelper.dpToPixel(getContext(), 2), 0.0f, LayoutHelper.dpToPixel(getContext(), 1), Color.argb(170, 120, 144, 156));
            } else {
                mButtonPaint.setColor(context.getResources().getColor(R.color.snow));
                mButtonPaint.setStyle(Paint.Style.FILL);
                mButtonPaint.setShadowLayer(LayoutHelper.dpToPixel(getContext(), 2), LayoutHelper.dpToPixel(getContext(), 1), 0.0f, Color.argb(170, 120, 144, 156));
            }
            a.recycle();
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if("vertical".equals(shadowOrientation)) {
            canvas.drawRect(0, 0, getWidth(), getHeight() - LayoutHelper.dpToPixel(getContext(), 2), mButtonPaint);
        } else {
            canvas.drawRect(0, 0, getWidth() - LayoutHelper.dpToPixel(getContext(), 2), getHeight(), mButtonPaint);
        }
    }
}
