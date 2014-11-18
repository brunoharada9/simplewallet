package br.com.tolive.simplewallet.views;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import br.com.tolive.simplewallet.app.R;

public class FloatingActionButton extends View {

    final static OvershootInterpolator overshootInterpolator = new OvershootInterpolator();
    final static AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();

    final static Boolean PRESSED = true;
    final static Boolean NOT_PRESSED = false;

    Context context;
    Paint mButtonPaint;
    int mFloatingActionButtonColor;
    int mFloatingActionButtonColorPressed;
    Paint mDrawablePaint;
    Bitmap mBitmap;
    boolean mHidden = false;

    public FloatingActionButton(Context context) {
        super(context);
        this.context = context;
        init(context.getResources().getColor(R.color.primary_green));
    }

    public void setFloatingActionButtonColor(Boolean pressed){
        if(pressed){
            init(this.mFloatingActionButtonColorPressed);
        } else{
            init(mFloatingActionButtonColor);
        }
    }

    public void setFloatingActionButtonColor(int FloatingActionButtonColor, int FloatingActionButtonColorPressed) {
        this.mFloatingActionButtonColor = FloatingActionButtonColor;
        this.mFloatingActionButtonColorPressed = FloatingActionButtonColorPressed;
        init(FloatingActionButtonColor);
    }

    public void setFloatingActionButtonDrawable(Drawable FloatingActionButtonDrawable) {
        mBitmap = ((BitmapDrawable) FloatingActionButtonDrawable).getBitmap();
        invalidate();
    }

    public void init(int FloatingActionButtonColor) {
        setWillNotDraw(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mButtonPaint.setColor(FloatingActionButtonColor);
        mButtonPaint.setStyle(Paint.Style.FILL);
        mButtonPaint.setShadowLayer(5.0f, 0.0f, 2.5f, Color.argb(100, 0, 0, 0));
        mDrawablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setClickable(true);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (float) (getWidth() / 2.6), mButtonPaint);
        canvas.drawBitmap(mBitmap, (getWidth() - mBitmap.getWidth()) / 2,
                (getHeight() - mBitmap.getHeight()) / 2, mDrawablePaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //setAlpha(1.0f);
            setFloatingActionButtonColor(NOT_PRESSED);
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //setAlpha(0.6f);
            setFloatingActionButtonColor(PRESSED);
        }
        return super.onTouchEvent(event);
    }

    public void hideFloatingActionButton() {
        if (!mHidden) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1, 0);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1, 0);
            AnimatorSet animSetXY = new AnimatorSet();
            animSetXY.playTogether(scaleX, scaleY);
            animSetXY.setInterpolator(accelerateInterpolator);
            animSetXY.setDuration(100);
            animSetXY.start();
            mHidden = true;
        }
    }

    public void showFloatingActionButton() {
        if (mHidden) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", 0, 1);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", 0, 1);
            AnimatorSet animSetXY = new AnimatorSet();
            animSetXY.playTogether(scaleX, scaleY);
            animSetXY.setInterpolator(overshootInterpolator);
            animSetXY.setDuration(200);
            animSetXY.start();
            mHidden = false;
        }
    }

    public boolean isHidden() {
        return mHidden;
    }

    static public class Builder {
        private FrameLayout.LayoutParams params;
        private final Activity activity;
        int gravity = Gravity.BOTTOM | Gravity.END; // default bottom right
        Drawable drawable;
        int color = Color.WHITE;
        int colorPressed = Color.WHITE;
        int size = 0;
        float scale = 0;

        public Builder(Activity context) {
            scale = context.getResources().getDisplayMetrics().density;
            size = convertToPixels(72, scale); // default size is 72dp by 72dp
            params = new FrameLayout.LayoutParams(size, size);
            params.gravity = gravity;

            this.activity = context;
        }

        /**
         * Sets the gravity for the FAB
         */
        public Builder withGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
         * Sets the margins for the FAB in dp
         */
        public Builder withMargins(int left, int top, int right, int bottom) {
            params.setMargins(
                    convertToPixels(left, scale),
                    convertToPixels(top, scale),
                    convertToPixels(right, scale),
                    convertToPixels(bottom, scale));
            return this;
        }

        /**
         * Sets the margins for the FAB in pixels
         */
        public Builder withMarginsInPixels(int left, int top, int right, int bottom) {
            params.setMargins(
                    left,
                    top - size/2,
                    right,
                    bottom);
            return this;
        }

        /**
         * Sets the FAB drawable
         */
        public Builder withDrawable(final Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        /**
         * Sets the FAB color
         */
        public Builder withButtonColor(final int color, final int colorPressed) {
            this.color = color;
            this.colorPressed = colorPressed;
            return this;
        }

        /**
         * Sets the FAB size in dp
         */
        public Builder withButtonSize(int size) {
            size = convertToPixels(size, scale);
            params = new FrameLayout.LayoutParams(size, size);
            return this;
        }

        public FloatingActionButton create() {
            return create(null);
        }

        public FloatingActionButton create(ViewGroup parent) {
            final FloatingActionButton button = new FloatingActionButton(activity);
            button.setFloatingActionButtonColor(this.color, this.colorPressed);
            button.setFloatingActionButtonDrawable(this.drawable);
            params.gravity = this.gravity;
            ViewGroup root;
            if(parent == null){
                root = (ViewGroup) activity.findViewById(android.R.id.content);
            } else {
                root = parent;
            }
            root.addView(button, params);
            return button;
        }

        // The calculation (value * scale + 0.5f) is a widely used to convert to dps to pixel units
        // based on density scale
        // see developer.android.com (Supporting Multiple Screen Sizes)
        private int convertToPixels(int dp, float scale){
            return (int) (dp * scale + 0.5f) ;
        }
    }
}
