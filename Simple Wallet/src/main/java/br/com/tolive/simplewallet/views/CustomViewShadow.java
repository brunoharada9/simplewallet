package br.com.tolive.simplewallet.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import br.com.tolive.simplewallet.app.R;

public class CustomViewShadow extends LinearLayout {
    private Paint mButtonPaint;

    public CustomViewShadow(Context context) {
        this(context, null);
    }

    public CustomViewShadow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setColor(int color){
        mButtonPaint.setColor(color);
        invalidate();
    }

    private void init(Context context) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mButtonPaint.setColor(context.getResources().getColor(R.color.accent_green));
        mButtonPaint.setStyle(Paint.Style.FILL);
        mButtonPaint.setShadowLayer(5.0f, 0.0f, 2.0f, Color.argb(100, 0, 0, 0));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight() - 5.0f, mButtonPaint);
    }
}
