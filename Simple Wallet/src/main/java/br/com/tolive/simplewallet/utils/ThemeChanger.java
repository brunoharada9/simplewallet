package br.com.tolive.simplewallet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import br.com.tolive.simplewallet.app.R;
import br.com.tolive.simplewallet.constants.Constantes;
import br.com.tolive.simplewallet.db.EntryDAO;
import br.com.tolive.simplewallet.views.CustomTextView;
import br.com.tolive.simplewallet.views.CustomView;
import br.com.tolive.simplewallet.views.FloatingActionButton;

/**
 * Created by bruno.carvalho on 23/12/2014.
 */
public class ThemeChanger {
    private static final String TAG = "ThemeChanger";
    public static final int THEME_RED = 0;
    public static final int THEME_YELLOW = 1;
    public static final int THEME_GREEN = 2;

    public ActionBarActivity context;
    public Resources resources;

    public ThemeChanger(ActionBarActivity context){
        this.context = context;
        this.resources = context.getResources();
    }

    public int setThemeColor(int color){
        ActionBar actionBar = context.getSupportActionBar();
        if(color == THEME_RED){
            //actionBar.setIcon(R.drawable.ic_title_red);
            color = resources.getColor(R.color.bar_red);
            actionBar.setBackgroundDrawable(resources.getDrawable(R.drawable.actionbar_background_red));
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                context.getWindow().setStatusBarColor(resources.getColor(R.color.primary_dark_red));
                context.getWindow().setNavigationBarColor(resources.getColor(R.color.primary_red));
            }
        } else if(color == THEME_YELLOW){
            //actionBar.setIcon(R.drawable.ic_title_yellow);
            color = resources.getColor(R.color.bar_yellow);
            actionBar.setBackgroundDrawable(resources.getDrawable(R.drawable.actionbar_background_yellow));
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                context.getWindow().setStatusBarColor(resources.getColor(R.color.primary_dark_yellow));
                context.getWindow().setNavigationBarColor(resources.getColor(R.color.primary_yellow));
            }
        } else {
            //actionBar.setIcon(R.drawable.ic_title_green);
            color = resources.getColor(R.color.bar_green);
            actionBar.setBackgroundDrawable(resources.getDrawable(R.drawable.actionbar_background_green));
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                context.getWindow().setStatusBarColor(resources.getColor(R.color.primary_dark_green));
                context.getWindow().setNavigationBarColor(resources.getColor(R.color.primary_green));
            }
        }
        return color;
    }

    public int setThemeColor(int month, FloatingActionButton mFabButton){
        EntryDAO dao = EntryDAO.getInstance(context);
        Float gain = dao.getGain(month);
        Float expense = dao.getExpense(month);

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        float yellow = sharedPreferences.getFloat(Constantes.SP_KEY_YELLOW, Constantes.SP_YELLOW_DEFAULT);
        float red = sharedPreferences.getFloat(Constantes.SP_KEY_RED, Constantes.SP_RED_DEFAULT);

        ActionBar actionBar = context.getSupportActionBar();
        int color;
        if((gain - expense) < red){
            //actionBar.setIcon(R.drawable.ic_title_red);
            actionBar.setBackgroundDrawable(resources.getDrawable(R.drawable.actionbar_background_red));
            color = resources.getColor(R.color.bar_red);
            if(mFabButton != null) {
                mFabButton.setFloatingActionButtonColor(resources.getColor(R.color.primary_red), resources.getColor(R.color.bar_red));
            }
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                context.getWindow().setStatusBarColor(resources.getColor(R.color.primary_dark_red));
                context.getWindow().setNavigationBarColor(resources.getColor(R.color.primary_red));
            }
        } else if((gain - expense) < yellow){
            //actionBar.setIcon(R.drawable.ic_title_yellow);
            actionBar.setBackgroundDrawable(resources.getDrawable(R.drawable.actionbar_background_yellow));
            color = resources.getColor(R.color.bar_yellow);
            if(mFabButton != null) {
                mFabButton.setFloatingActionButtonColor(resources.getColor(R.color.primary_yellow), resources.getColor(R.color.bar_yellow));
            }
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                context.getWindow().setStatusBarColor(resources.getColor(R.color.primary_dark_yellow));
                context.getWindow().setNavigationBarColor(resources.getColor(R.color.primary_yellow));
            }
        } else{
            //actionBar.setIcon(R.drawable.ic_title_green);
            actionBar.setBackgroundDrawable(resources.getDrawable(R.drawable.actionbar_background_green));
            color = resources.getColor(R.color.bar_green);
            if(mFabButton != null) {
                mFabButton.setFloatingActionButtonColor(resources.getColor(R.color.primary_green), resources.getColor(R.color.bar_green));
            }
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                context.getWindow().setStatusBarColor(resources.getColor(R.color.primary_dark_green));
                context.getWindow().setNavigationBarColor(resources.getColor(R.color.primary_green));
            }
        }
        return color;
    }

    public void setAllTextViewColor(View parent, int color){
        try {
            if (parent instanceof ViewGroup) {
                if(parent.getId() == R.id.drawer_list_item){
                    if(color == resources.getColor(R.color.bar_red)) {
                        ((ImageView) ((ViewGroup) parent).getChildAt(0)).setImageDrawable(resources.getDrawable(R.drawable.ic_title_red));
                    } else if(color == resources.getColor(R.color.bar_yellow)) {
                        ((ImageView) ((ViewGroup) parent).getChildAt(0)).setImageDrawable(resources.getDrawable(R.drawable.ic_title_yellow));
                    } else if(color == resources.getColor(R.color.bar_green)) {
                        ((ImageView) ((ViewGroup) parent).getChildAt(0)).setImageDrawable(resources.getDrawable(R.drawable.ic_title_green));
                    }
                    setAllTextViewColor(((ViewGroup) parent).getChildAt(1), color);
                }
                ViewGroup vg = (ViewGroup) parent;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    setAllTextViewColor(child, color);
                }
            } else if (parent instanceof CustomTextView || parent instanceof TextView) {
                ((TextView) parent).setTextColor(color);
            } else if(parent instanceof CustomView){
                parent.setBackgroundColor(color);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //public static int setThemeColor(ActionBarActivity context, int month){
    //    return setThemeColor(context, month, null);
    //}
}
