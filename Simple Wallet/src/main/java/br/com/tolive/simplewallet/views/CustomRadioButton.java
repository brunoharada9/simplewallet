package br.com.tolive.simplewallet.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatRadioButton;

import br.com.tolive.simplewallet.app.R;

/**
 * Created by Bruno on 05/08/2014.
 */
public class CustomRadioButton extends AppCompatRadioButton {
    public CustomRadioButton(Context context) {
        super(context);
        init(null);
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
            String font = a.getString(R.styleable.CustomTextView_fonttest);
            if (font!=null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + font);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }
}
