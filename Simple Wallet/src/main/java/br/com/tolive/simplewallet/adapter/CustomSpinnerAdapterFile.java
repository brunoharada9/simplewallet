package br.com.tolive.simplewallet.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import br.com.tolive.simplewallet.app.R;

/**
 * Created by bruno.carvalho on 11/10/2014.
 */
public class CustomSpinnerAdapterFile extends ArrayAdapter<File> {
    private Activity context;
    private ArrayList<File> data;
    private LayoutInflater inflater;
    private int color;

    /*************  CustomAdapter Constructor *****************/
    public CustomSpinnerAdapterFile(Activity context, int textViewResourceId, ArrayList<File> objects, int color) {
        super(context, textViewResourceId, objects);
        /********** Take passed values **********/
        this.context = context;
        this.data     = objects;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.color = color;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.simple_spinner_item, parent, false);
        }
        convertView.setBackgroundColor(color);

        TextView label        = (TextView) convertView.findViewById(android.R.id.text1);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/" + context.getResources().getString(R.string.app_font));
        label.setTypeface(tf);
        label.setTextColor(context.getResources().getColor(R.color.snow));
        //label.setBackgroundColor(color);
        final DisplayMetrics metrics;
        metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        label.setTextSize(getDPI(8, metrics));

        String name;
        File file = data.get(position);
        if(file == null){
            name = context.getResources().getString(R.string.dialog_recovery_text);
            label.setText(name);
        } else {
            name = file.getName();
            label.setText(name.substring(0,name.lastIndexOf(".")));
        }
        return convertView;
    }

    public static int getDPI(int size, DisplayMetrics metrics){
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }
}
