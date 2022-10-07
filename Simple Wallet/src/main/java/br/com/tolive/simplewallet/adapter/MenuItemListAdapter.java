package br.com.tolive.simplewallet.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import br.com.tolive.simplewallet.app.R;
import br.com.tolive.simplewallet.constants.Constantes;
import br.com.tolive.simplewallet.model.MenuItem;
import br.com.tolive.simplewallet.utils.ThemeChanger;

/**
 * Created by bruno.carvalho on 27/06/2014.
 */

public class MenuItemListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MenuItem> menuItems;

    public MenuItemListAdapter(Context context, ArrayList<MenuItem> menuItems){
        this.context = context;
        this.menuItems = menuItems;
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        if(position == 0){
            convertView.setBackgroundColor(context.getResources().getColor(R.color.clicked));
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        ImageView imgArrow = (ImageView) convertView.findViewById(R.id.arrow);

        Typeface tf = Typeface.createFromAsset(context.getAssets(), Constantes.FONT_PATH_ROBOTO_CONDENSED_BOLD);
        txtTitle.setTypeface(tf);

        //imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
        txtTitle.setText(menuItems.get(position).getTitle());

        ThemeChanger themeChanger = new ThemeChanger((AppCompatActivity) context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int color = themeChanger.setThemeColor(sharedPreferences.getInt(Constantes.SP_KEY_MONTH, Constantes.SP_MONTH_DEFAULT), null);
        Resources resources = context.getResources();
        TypedArray navMenuIcons = null;
        if (color == resources.getColor(R.color.primary_red)) {
            navMenuIcons = context.getResources().obtainTypedArray(R.array.nav_drawer_icons_red);
            imgArrow.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_hardware_keyboard_arrow_right_red));
        } else if (color == resources.getColor(R.color.primary_yellow)) {
            navMenuIcons = context.getResources().obtainTypedArray(R.array.nav_drawer_icons_yellow);
            imgArrow.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_hardware_keyboard_arrow_right_yellow));
        } else if (color == resources.getColor(R.color.primary_green)) {
            navMenuIcons = context.getResources().obtainTypedArray(R.array.nav_drawer_icons_green);
            imgArrow.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_hardware_keyboard_arrow_right_green));
        }
        imgIcon.setImageDrawable(navMenuIcons.getDrawable(position));
        txtTitle.setTextColor(color);

        return convertView;
    }
}
