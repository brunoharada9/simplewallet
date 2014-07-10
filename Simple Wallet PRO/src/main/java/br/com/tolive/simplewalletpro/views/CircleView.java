package br.com.tolive.simplewalletpro.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import br.com.tolive.simplewalletpro.R;

public class CircleView extends View {

    private Paint red;
    private Paint yellow;
    private Paint green;
    RectF rectRed;
    RectF rectYellow;
    RectF rectGreen;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(context.getResources().getColor(R.color.snow));

        red = new Paint();
        red.setColor(getResources().getColor(R.color.red));

        yellow = new Paint();
        yellow.setColor(getResources().getColor(R.color.yellow));

        green = new Paint();
        green.setColor(getResources().getColor(R.color.green));

        //rectRed = new RectF(0, 0, getLayoutParams().width, getLayoutParams().height);
        //rectYellow = new RectF(0, 0, getLayoutParams().width, getLayoutParams().height);
        //rectGreen = new RectF(0, 0, getLayoutParams().width, getLayoutParams().height);

        setFocusable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        rectRed = new RectF(0, 0, widthMeasureSpec, heightMeasureSpec);
        rectYellow = new RectF(0, 0, widthMeasureSpec, heightMeasureSpec);
        rectGreen = new RectF(0, 0, widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(rectRed, 0, 90, true, red);
        canvas.drawArc(rectYellow, 90, 90, true, yellow);
        canvas.drawArc(rectGreen, 180, 180, true, green);
        //canvas.drawCircle(100, 110, 20, red);
    }
}