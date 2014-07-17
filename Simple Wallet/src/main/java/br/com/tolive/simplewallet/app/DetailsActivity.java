package br.com.tolive.simplewallet.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import br.com.tolive.simplewallet.constants.Constantes;
import br.com.tolive.simplewallet.model.Entry;


public class DetailsActivity extends Activity {
    private Entry entry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        boolean removeAd = sharedPreferences.getBoolean(Constantes.SP_KEY_REMOVE_AD, Constantes.SP_REMOVE_AD_DEFAULT);
        if(!removeAd) {
            AdRequest request = new AdRequest.Builder()
                    .addTestDevice("E6E54B90007CAC7A62F9EC7857F3A989")
                    .build();
            AdView adView = (AdView) findViewById(R.id.ad_details);
            adView.loadAd(request);
        } else{
            AdView adView = (AdView) findViewById(R.id.ad_details);
            adView.setVisibility(View.GONE);
        }

        Intent intent = getIntent();
        this.entry = (Entry) intent.getSerializableExtra(EntriesListFragmentFragment.EXTRA_KEY_ENTRY_DETAILS);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.activity_details_container);

        if(entry.getType() == Entry.TYPE_GAIN){
            container.setBackgroundColor(getResources().getColor(R.color.green));
        } else{
            container.setBackgroundColor(getResources().getColor(R.color.red));
        }

        Typeface tf = Typeface.createFromAsset(getAssets(), Constantes.FONT_PATH_ROBOTO_CONDENSED_BOLD);

        TextView txtDescription = (TextView) findViewById(R.id.activity_details_text_description);
        txtDescription.setTypeface(tf);
        txtDescription.setText(entry.getDescription());

        TextView txtValue = (TextView) findViewById(R.id.activity_details_text_value);
        txtValue.setTypeface(tf);
        txtValue.setText(String.format("%.2f", entry.getValue()));

        TextView txtDate = (TextView) findViewById(R.id.activity_details_text_date);
        txtDate.setTypeface(tf);
        txtDate.setText(entry.getDate());

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.ic_back);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
