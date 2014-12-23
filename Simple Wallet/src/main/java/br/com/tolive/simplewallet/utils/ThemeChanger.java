package br.com.tolive.simplewallet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import br.com.tolive.simplewallet.app.R;
import br.com.tolive.simplewallet.constants.Constantes;
import br.com.tolive.simplewallet.db.EntryDAO;
import br.com.tolive.simplewallet.views.FloatingActionButton;

/**
 * Created by bruno.carvalho on 23/12/2014.
 */
public class ThemeChanger {
    private static final String TAG = "ThemeChanger";
    public static final int THEME_RED = 0;
    public static final int THEME_YELLOW = 1;
    public static final int THEME_GREEN = 2;

    public static void setThemeColor(ActionBarActivity context, int color){
        ActionBar actionBar = context.getSupportActionBar();
        if(color == THEME_RED){
            //actionBar.setIcon(R.drawable.ic_title_red);
            actionBar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.actionbar_background_red));
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                context.getWindow().setStatusBarColor(context.getResources().getColor(R.color.primary_dark_red));
                context.getWindow().setNavigationBarColor(context.getResources().getColor(R.color.primary_red));
            }
        } else if(color == THEME_YELLOW){
            //actionBar.setIcon(R.drawable.ic_title_yellow);
            actionBar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.actionbar_background_yellow));
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                context.getWindow().setStatusBarColor(context.getResources().getColor(R.color.primary_dark_yellow));
                context.getWindow().setNavigationBarColor(context.getResources().getColor(R.color.primary_yellow));
            }
        } else {
            //actionBar.setIcon(R.drawable.ic_title_green);
            actionBar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.actionbar_background_green));
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                context.getWindow().setStatusBarColor(context.getResources().getColor(R.color.primary_dark_green));
                context.getWindow().setNavigationBarColor(context.getResources().getColor(R.color.primary_green));
            }
        }
    }

    public static int setThemeColor(ActionBarActivity context, int month, FloatingActionButton mFabButton){
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
            actionBar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.actionbar_background_red));
            color = context.getResources().getColor(R.color.bar_red);
            if(mFabButton != null) {
                mFabButton.setFloatingActionButtonColor(context.getResources().getColor(R.color.primary_red), context.getResources().getColor(R.color.bar_red));
            }
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                context.getWindow().setStatusBarColor(context.getResources().getColor(R.color.primary_dark_red));
                context.getWindow().setNavigationBarColor(context.getResources().getColor(R.color.primary_red));
            }
        } else if((gain - expense) < yellow){
            //actionBar.setIcon(R.drawable.ic_title_yellow);
            actionBar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.actionbar_background_yellow));
            color = context.getResources().getColor(R.color.bar_yellow);
            if(mFabButton != null) {
                mFabButton.setFloatingActionButtonColor(context.getResources().getColor(R.color.primary_yellow), context.getResources().getColor(R.color.bar_yellow));
            }
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                context.getWindow().setStatusBarColor(context.getResources().getColor(R.color.primary_dark_yellow));
                context.getWindow().setNavigationBarColor(context.getResources().getColor(R.color.primary_yellow));
            }
        } else{
            //actionBar.setIcon(R.drawable.ic_title_green);
            actionBar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.actionbar_background_green));
            color = context.getResources().getColor(R.color.bar_green);
            if(mFabButton != null) {
                mFabButton.setFloatingActionButtonColor(context.getResources().getColor(R.color.primary_green), context.getResources().getColor(R.color.bar_green));
            }
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                context.getWindow().setStatusBarColor(context.getResources().getColor(R.color.primary_dark_green));
                context.getWindow().setNavigationBarColor(context.getResources().getColor(R.color.primary_green));
            }
        }
        return color;
    }

    //public static int setThemeColor(ActionBarActivity context, int month){
    //    return setThemeColor(context, month, null);
    //}
}
