package br.com.tolive.simplewallet.app;



import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import br.com.tolive.simplewallet.constants.Constantes;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class AboutFragment extends Fragment {


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        TextView textMoreApps = (TextView) view.findViewById(R.id.fragment_about_text_more_apps);
        TextView textToLiveHealthy = (TextView) view.findViewById(R.id.fragment_about_tolivehealthy_text);
        LinearLayout containerToLiveHealthy = (LinearLayout) view.findViewById(R.id.fragment_about_tolivehealthy);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), Constantes.FONT_PATH_ROBOTO_CONDENSED_BOLD);
        textMoreApps.setTypeface(tf);
        textToLiveHealthy.setTypeface(tf);

        containerToLiveHealthy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Levar para Loja", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


}
