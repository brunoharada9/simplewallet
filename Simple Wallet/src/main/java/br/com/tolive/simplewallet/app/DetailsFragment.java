package br.com.tolive.simplewallet.app;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.tolive.simplewallet.constants.Constantes;
import br.com.tolive.simplewallet.model.Entry;


public class DetailsFragment extends Fragment {
    private Entry entry;

    public DetailsFragment(Entry entry) {
        this.entry = entry;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        if(entry.getType() == Entry.TYPE_GAIN){
            view.setBackgroundColor(getActivity().getResources().getColor(R.color.green));
        } else{
            view.setBackgroundColor(getActivity().getResources().getColor(R.color.red));
        }

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), Constantes.FONT_PATH_ROBOTO_CONDENSED_BOLD);

        TextView txtDescription = (TextView) view.findViewById(R.id.fragment_details_text_description);
        txtDescription.setTypeface(tf);
        txtDescription.setText(entry.getDescription());

        TextView txtValue = (TextView) view.findViewById(R.id.fragment_details_text_value);
        txtValue.setTypeface(tf);
        txtValue.setText(String.format("%.2f", entry.getValue()));

        TextView txtDate = (TextView) view.findViewById(R.id.fragment_details_text_date);
        txtDate.setTypeface(tf);
        txtDate.setText(entry.getDate());

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
