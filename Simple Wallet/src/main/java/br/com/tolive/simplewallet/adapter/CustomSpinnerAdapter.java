package br.com.tolive.simplewallet.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.tolive.simplewallet.app.R;
import br.com.tolive.simplewallet.constants.Constantes;

/**
 * Created by bruno.carvalho on 02/07/2014.
 */
public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private Context context;
    private String[] data;
    private int color;
    private LayoutInflater inflater;

    /*************  CustomAdapter Constructor *****************/
    public CustomSpinnerAdapter( Context context, int textViewResourceId, String[] objects, int color) {
        super(context, textViewResourceId, objects);
        /********** Take passed values **********/
        this.context = context;
        this.data     = objects;
        this.color = color;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        View view = inflater.inflate(R.layout.simple_spinner_item, parent, false);
        view.setBackgroundColor(color);

        TextView label        = (TextView) view.findViewById(android.R.id.text1);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), Constantes.FONT_PATH_ROBOTO_CONDENSED_BOLD);
        label.setTypeface(tf);

        label.setText(data[position]);

        return view;
    }
}
