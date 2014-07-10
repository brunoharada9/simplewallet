package br.com.tolive.simplewalletpro.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.constants.Constantes;
import br.com.tolive.simplewalletpro.model.Entry;

/**
 * Created by bruno.carvalho on 27/06/2014.
 */
public class EntriesListAdapter extends BaseAdapter {
    private List<Entry> entries;
    private Context context;

    public EntriesListAdapter(ArrayList<Entry> entries, Context context){
        this.entries = entries;
        this.context = context;
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int position) {
        return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return entries.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.view_list_entries, null);
        }
        Entry entry = entries.get(position);

        TextView txtDescription = (TextView) convertView.findViewById(R.id.textView_list_description);
        TextView txtValue = (TextView) convertView.findViewById(R.id.textView_list_value);

        Typeface tf = Typeface.createFromAsset(context.getAssets(), Constantes.FONT_PATH_ROBOTO_CONDENSED_BOLD);
        txtDescription.setTypeface(tf);
        txtValue.setTypeface(tf);

        txtDescription.setText(entry.getDescription());
        txtValue.setText(String.format("%.2f", entry.getValue()));

        if(entry.getType() == Entry.TYPE_EXPENSE){
            convertView.setBackgroundColor(context.getResources().getColor(R.color.expense));
        } else if (entry.getType() == Entry.TYPE_GAIN){
            convertView.setBackgroundColor(context.getResources().getColor(R.color.gain));
        }

        return convertView;
    }
}
