package br.com.tolive.simplewallet.adapter;

import br.com.tolive.simplewallet.app.R;
import br.com.tolive.simplewallet.model.Entry;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class MyRecyclerViewAdapter extends
		Adapter<MyRecyclerViewAdapter.ViewHolder> {
	private List<Entry> entries;
	private Context mContext;

	// Provide a reference to the views for each data item
	// Complex data items may need more than one view per item, and
	// you provide access to all the views for a data item in a view holder
	public static class ViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		//public CircleView mCircle;
		public TextView txtValue;
		public TextView txtDescription;

		public ViewHolder(View v) {
			super(v);
			txtValue = (TextView) v.findViewById(R.id.textView_list_value);
			txtDescription = (TextView) v.findViewById(R.id.textView_list_description);
			//mCircle = (CircleView) v.findViewById(R.id.button);
		}
	}

	// Provide a suitable constructor (depends on the kind of dataset)
	public MyRecyclerViewAdapter(List<Entry> entries, Context context) {
        entries = entries;
		mContext = context;
	}

	// Create new views (invoked by the layout manager)
	@Override
	public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(
			ViewGroup parent, int viewType) {
		// create a new view
		View v = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.view_list_entries, parent, false);
		// set the view's size, margins, paddings and layout parameters
		// ...
		ViewHolder vh = new ViewHolder(v);
		return vh;
	}

	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		// - get element from your dataset at this position
		// - replace the contents of the view with that element
        Entry entry = entries.get(position);

        holder.txtDescription.setText(entry.getDescription());
        holder.txtValue.setText(String.format("%.2f", entry.getValue()));

        if(entry.getType() == Entry.TYPE_EXPENSE){
            //setBackgroundDrawable
            if(Build.VERSION.SDK_INT < 16){
                holder.txtValue.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.txt_value_red));
            } else {
                holder.txtValue.setBackground(mContext.getResources().getDrawable(R.drawable.txt_value_red));
            }
        } else if (entry.getType() == Entry.TYPE_GAIN){
            if(Build.VERSION.SDK_INT < 16){
                holder.txtValue.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.txt_value_green));
            } else {
                holder.txtValue.setBackground(mContext.getResources().getDrawable(R.drawable.txt_value_green));
            }
        }
	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return entries.size();
	}

}
