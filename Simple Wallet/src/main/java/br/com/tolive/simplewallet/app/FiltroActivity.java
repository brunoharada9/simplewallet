package br.com.tolive.simplewallet.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.tolive.simplewallet.adapter.CustomSpinnerAdapter;
import br.com.tolive.simplewallet.constants.Constantes;
import br.com.tolive.simplewallet.db.EntryDAO;
import br.com.tolive.simplewallet.model.Entry;
import br.com.tolive.simplewallet.utils.ThemeChanger;


public class FiltroActivity extends ActionBarActivity {
    public static final String EXTRA_KEY_FILTRO_ENTRIES = "entries_filtro";
    public static final String EXTRA_KEY_FILTRO_MONTH = "month_filtro";
    Spinner spinnerMonth;
    Spinner spinnerYear;
    private String[] months;
    private String[] years;
    int month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        boolean removeAd = sharedPreferences.getBoolean(Constantes.SP_KEY_REMOVE_AD, Constantes.SP_REMOVE_AD_DEFAULT);
        if(!removeAd) {
            AdRequest request = new AdRequest.Builder()
                    .addTestDevice("E6E54B90007CAC7A62F9EC7857F3A989")
                    .build();
            AdView adView = (AdView) findViewById(R.id.ad_filtro);
            adView.loadAd(request);
        } else{
            AdView adView = (AdView) findViewById(R.id.ad_filtro);
            adView.setVisibility(View.GONE);
        }

        month = sharedPreferences.getInt(Constantes.SP_KEY_MONTH, Constantes.SP_MONTH_DEFAULT);

        months = getResources().getStringArray(R.array.spinner_months);
        years = getResources().getStringArray(R.array.spinner_years);

        spinnerMonth = (Spinner) findViewById(R.id.fragment_filtro_spinner_month);
        spinnerYear = (Spinner) findViewById(R.id.fragment_filtro_spinner_year);
        Calendar calendar = Calendar.getInstance();

        ThemeChanger themeChanger = new ThemeChanger(this);
        int color = themeChanger.setThemeColor(month, null);
        themeChanger.setAllViewsColor(findViewById(R.id.parent), color);
        themeChanger.setAllViewsColor(findViewById(R.id.fragment_menu_list), color);

        CustomSpinnerAdapter adapterMonth = new CustomSpinnerAdapter(this, R.layout.simple_spinner_item, months, color);
        adapterMonth.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapterMonth);
        spinnerMonth.setSelection(month);

        /*spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ThemeChanger themeChanger = new ThemeChanger(FiltroActivity.this);
                int color = themeChanger.setThemeColor(position, null);
                themeChanger.setAllViewsColor(findViewById(R.id.parent), color);
                themeChanger.setAllViewsColor(findViewById(R.id.list_slidermenu), color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        CustomSpinnerAdapter adapterYear = new CustomSpinnerAdapter(this, R.layout.simple_spinner_item, years, color);
        adapterYear.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapterYear);
        spinnerYear.setSelection(getYear(calendar.get(Calendar.YEAR), years));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setIcon(R.drawable.ic_action_hardware_keyboard_backspace);
    }

    private int getYear(int currentYear, String[] years) {
        for (int i = 0; i < years.length; i++){
            if(years[i].equals(String.valueOf(currentYear))){
                return i;
            }
        }
        return 0;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filtro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_filtrar) {
            EntryDAO dao = EntryDAO.getInstance(this);
            ArrayList<Entry> entries = dao.getEntry(String.valueOf(spinnerMonth.getSelectedItemPosition()+1), (String) spinnerYear.getSelectedItem());

            Intent returnIntent = new Intent();
            returnIntent.putExtra(EXTRA_KEY_FILTRO_ENTRIES, entries);
            returnIntent.putExtra(EXTRA_KEY_FILTRO_MONTH, spinnerMonth.getSelectedItemPosition());
            setResult(RESULT_OK, returnIntent);
            finish();
            return true;
        } else if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
