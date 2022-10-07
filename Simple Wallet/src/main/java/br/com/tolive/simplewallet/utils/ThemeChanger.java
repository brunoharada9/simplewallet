package br.com.tolive.simplewallet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import br.com.tolive.simplewallet.adapter.MenuItemListAdapter;
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

    private AppCompatActivity context;
    private Resources resources;

    private Drawable icon_green;
    private Drawable icon_yellow;
    private Drawable icon_red;

    public ThemeChanger(AppCompatActivity context){
        this.context = context;
        this.resources = context.getResources();

        this.icon_green = resources.getDrawable(R.drawable.ic_action_editor_attach_money_green);
        this.icon_yellow = resources.getDrawable(R.drawable.ic_action_editor_attach_money_yellow);
        this.icon_red = resources.getDrawable(R.drawable.ic_action_editor_attach_money_red);
    }

    public static int setDialogTheme(Context context, View view, int theme){
        int color;
        switch (theme){
            case THEME_GREEN:
                color = context.getResources().getColor(R.color.primary_green);
                break;
            case THEME_YELLOW:
                color = context.getResources().getColor(R.color.primary_yellow);
                break;
            default:
                color = context.getResources().getColor(R.color.primary_red);
                break;
        }
        try {
            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    setDialogTheme(context, child, theme);
                }
            } else if (view instanceof CustomTextView || view instanceof TextView) {
                if(view.getId() == R.id.dialog_add_text_cancel || view.getId() == R.id.dialog_add_text_ok
                        || view.getId() == R.id.dialog_details_text_edit || view.getId() == R.id.dialog_details_text_cancel
                        || view.getId() == R.id.dialog_recovery_text_cancel || view.getId() == R.id.dialog_recovery_text_ok){
                    view.setBackgroundColor(color);
                } else if( view.getId() == R.id.dialog_details_text_description || view.getId() == R.id.dialog_details_value || view.getId() == R.id.dialog_details_date
                        || view.getId() == R.id.dialog_add_text_choose || view.getId() == R.id.dialog_add_edittext_value || view.getId() == R.id.dialog_add_edittext_description){
                    ((TextView) view).setTextColor(color);
                } else if(view.getId() == R.id.dialog_details_description || view.getId() == R.id.dialog_details_text_value || view.getId() == R.id.dialog_details_text_date){
                    ((TextView) view).setTextColor(color);
                }
            } else if (view instanceof CustomView) {
                if(view.getId() == R.id.dialog_details_divisor_1 || view.getId() == R.id.dialog_details_divisor_2
                        || view.getId() == R.id.dialog_details_divisor_3 || view.getId() == R.id.dialog_details_divisor_4
                        || view.getId() == R.id.dialog_add_divisor_choose_date_1 || view.getId() == R.id.dialog_add_divisor_choose_date_2) {
                     view.setBackgroundColor(color);
                }
            } else if (view instanceof ImageView){
                ImageView image = (ImageView) view;
                switch (theme) {
                    case THEME_GREEN:
                        image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_hardware_keyboard_arrow_down_green));
                        break;
                    case THEME_YELLOW:
                        image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_hardware_keyboard_arrow_down_yellow));
                        break;
                    default:
                        image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_hardware_keyboard_arrow_down_red));
                        break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return color;
    }

    public static int getThemeColor(Context context, int month){
        EntryDAO dao = EntryDAO.getInstance(context);
        Float gain = dao.getGain(month);
        Float expense = dao.getExpense(month);

        Resources resources = context.getResources();

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        float yellow = sharedPreferences.getFloat(Constantes.SP_KEY_YELLOW, Constantes.SP_YELLOW_DEFAULT);
        float red = sharedPreferences.getFloat(Constantes.SP_KEY_RED, Constantes.SP_RED_DEFAULT);

        if((gain - expense) < red){
            return resources.getColor(R.color.primary_red);
        } else if((gain - expense) < yellow){
            return resources.getColor(R.color.primary_yellow);
        } else{
            return resources.getColor(R.color.primary_green);
        }
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
            color = resources.getColor(R.color.primary_red);
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
            color = resources.getColor(R.color.primary_yellow);
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
            color = resources.getColor(R.color.primary_green);
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

    public void setAllViewsColor(View parent, int color){
        try {
            if (parent instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) parent;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    setAllViewsColor(child, color);
                }
            } else if (parent instanceof CustomTextView || parent instanceof TextView) {
                ((TextView) parent).setTextColor(color);
                /*if(parent.getId() == android.R.id.text1){
                    ((Spinner) parent.getParent()).getAdapter().getView();
                    ((TextView) parent).setTextColor(resources.getColor(R.color.snow));
                }*/
            } else if(parent instanceof CustomView){
                parent.setBackgroundColor(color);
            } else if (parent instanceof ImageView){
                if(parent.getId() == R.id.fragment_recory_image_sdcard || parent.getId() == R.id.fragment_store_image_sdcard){
                    if(color == resources.getColor(R.color.primary_red)) {
                        ((ImageView) parent).setImageDrawable(resources.getDrawable(R.drawable.ic_action_device_sd_storage_red));
                    } else if(color == resources.getColor(R.color.primary_yellow)) {
                        ((ImageView) parent).setImageDrawable(resources.getDrawable(R.drawable.ic_action_device_sd_storage_yellow));
                    } else if(color == resources.getColor(R.color.primary_green)) {
                        ((ImageView) parent).setImageDrawable(resources.getDrawable(R.drawable.ic_action_device_sd_storage_green));
                    }
                } else if(parent.getId() == R.id.fragment_store_image_email){
                    if(color == resources.getColor(R.color.primary_red)) {
                        ((ImageView) parent).setImageDrawable(resources.getDrawable(R.drawable.ic_action_content_mail_red));
                    } else if(color == resources.getColor(R.color.primary_yellow)) {
                        ((ImageView) parent).setImageDrawable(resources.getDrawable(R.drawable.ic_action_content_mail_yellow));
                    } else if(color == resources.getColor(R.color.primary_green)) {
                        ((ImageView) parent).setImageDrawable(resources.getDrawable(R.drawable.ic_action_content_mail_green));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setMenuColor(View parent, int color){
        ImageView imageContent = (ImageView) ((ViewGroup)parent).getChildAt(0);
        ListView menuList = (ListView) ((ViewGroup)parent).getChildAt(1);

        TypedArray navMenuIcons = null;
        Drawable imageArrow = null;
        if (color == resources.getColor(R.color.primary_red)) {
            navMenuIcons = context.getResources().obtainTypedArray(R.array.nav_drawer_icons_red);
            imageContent.setBackgroundColor(context.getResources().getColor(R.color.accent_red));
            imageArrow = context.getResources().getDrawable(R.drawable.ic_action_hardware_keyboard_arrow_right_red);
        } else if (color == resources.getColor(R.color.primary_yellow)) {
            navMenuIcons = context.getResources().obtainTypedArray(R.array.nav_drawer_icons_yellow);
            imageContent.setBackgroundColor(context.getResources().getColor(R.color.accent_yellow));
            imageArrow = context.getResources().getDrawable(R.drawable.ic_action_hardware_keyboard_arrow_right_yellow);
        } else if (color == resources.getColor(R.color.primary_green)) {
            navMenuIcons = context.getResources().obtainTypedArray(R.array.nav_drawer_icons_green);
            imageContent.setBackgroundColor(context.getResources().getColor(R.color.accent_green));
            imageArrow = context.getResources().getDrawable(R.drawable.ic_action_hardware_keyboard_arrow_right_green);
        }

        MenuItemListAdapter adapter = (MenuItemListAdapter) menuList.getAdapter();
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            RelativeLayout layout = (RelativeLayout) menuList.getChildAt(i);
            if(layout != null) {
                for (int j = 0; j < layout.getChildCount(); j++) {
                    View child = layout.getChildAt(j);
                    if (j % 3 == 0) {
                        ((ImageView) child).setImageDrawable(navMenuIcons.getDrawable(i));
                    } else if (j % 3 == 1){
                        ((TextView) child).setTextColor(color);
                    } else {
                        ((ImageView) child).setImageDrawable(imageArrow);
                    }
                }
            }
        }

        navMenuIcons.recycle();
    }

    public void setConfigColor(View parent, int color){
        try {
            if(parent.getId() == R.id.fragment_settings_container_set_warning_green ||
               parent.getId() == R.id.fragment_settings_container_set_warning_yellow ||
               parent.getId() == R.id.fragment_settings_container_set_warning_red){
                return ;
            }
            if (parent instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) parent;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    setConfigColor(child, color);
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

    public void setAboutColor(View parent, int color){
        try {
            if(parent.getId() == R.id.fragment_about_tolivehealthy ||
                    parent.getId() == R.id.fragment_about_gastossimplespro ||
                    parent.getId() == R.id.fragment_about_fb_fanpage ||
                    parent.getId() == R.id.fragment_about_container_remove_ad){
                return ;
            }
            if (parent instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) parent;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    setAboutColor(child, color);
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
