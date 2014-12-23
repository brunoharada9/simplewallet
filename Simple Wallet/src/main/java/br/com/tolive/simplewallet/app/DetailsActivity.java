package br.com.tolive.simplewallet.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import br.com.tolive.simplewallet.constants.Constantes;
import br.com.tolive.simplewallet.model.Entry;
import br.com.tolive.simplewallet.utils.ThemeChanger;
import br.com.tolive.simplewallet.views.CustomTextView;


public class DetailsActivity extends ActionBarActivity {
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
            adView.setVisibility(View.INVISIBLE);
        }

        Intent intent = getIntent();
        this.entry = (Entry) intent.getSerializableExtra(EntriesListFragmentFragment.EXTRA_KEY_ENTRY_DETAILS);

        if(entry.getType() == Entry.TYPE_GAIN){
            ThemeChanger.setThemeColor(this, ThemeChanger.THEME_GREEN);
        } else{
            ThemeChanger.setThemeColor(this, ThemeChanger.THEME_RED);
        }

        CustomTextView txtDescription = (CustomTextView) findViewById(R.id.activity_details_text_description);
        txtDescription.setText(entry.getDescription());

        CustomTextView txtValue = (CustomTextView) findViewById(R.id.activity_details_text_value);
        txtValue.setText(String.format("%.2f", entry.getValue()));

        CustomTextView txtDate = (CustomTextView) findViewById(R.id.activity_details_text_date);
        txtDate.setText(entry.getDate());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setLogo(R.drawable.ic_back);
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
