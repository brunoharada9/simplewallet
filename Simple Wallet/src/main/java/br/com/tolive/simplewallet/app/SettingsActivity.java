package br.com.tolive.simplewallet.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import br.com.tolive.simplewallet.constants.Constantes;
import br.com.tolive.simplewallet.views.CustomRadioButton;


public class SettingsActivity extends Activity {
    //private static final String MSG_ERROR_INTERVAL = "Os valores devem estar entre 0 e 99";
    private static final String MSG_ERROR = "O valor amarelo deve ser maior que o vermelho";
    private static final String EMPTY = "";
    private static final String ERROR_INVALID_INPUT = "Valor inválido";
    private EditText editYellow;
    private EditText editRed;
    private TextView textPercentGreen;
    private RadioGroup radioGroupBalanceType;
    private CustomRadioButton radioButtonBalanceTypeTotal;
    private CustomRadioButton radioButtonBalanceTypeMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        boolean removeAd = sharedPreferences.getBoolean(Constantes.SP_KEY_REMOVE_AD, Constantes.SP_REMOVE_AD_DEFAULT);
        if(!removeAd) {
            AdRequest request = new AdRequest.Builder()
                    .addTestDevice("E6E54B90007CAC7A62F9EC7857F3A989")
                    .build();
            AdView adView = (AdView) findViewById(R.id.ad_settings);
            adView.loadAd(request);
        } else{
            AdView adView = (AdView) findViewById(R.id.ad_settings);
            adView.setVisibility(View.GONE);
        }

        editYellow = (EditText) findViewById(R.id.fragment_settings_edit_yellow);
        editRed = (EditText) findViewById(R.id.fragment_settings_edit_red);
        textPercentGreen = (TextView) findViewById(R.id.fragment_settings_text_color_set_percent_green_number);

        radioGroupBalanceType = (RadioGroup) findViewById(R.id.fragment_settings_radio_group_balance_type);
        radioButtonBalanceTypeTotal = (CustomRadioButton) findViewById(R.id.fragment_settings_radio_balance_type_total);
        radioButtonBalanceTypeMonth = (CustomRadioButton) findViewById(R.id.fragment_settings_radio_balance_type_month);
        int balanceType = sharedPreferences.getInt(Constantes.SP_KEY_BALANCE_TYPE, Constantes.SP_BALANCE_TYPE_DEFAULT);
        if(balanceType == Constantes.BALANCE_TYPE_MONTH) {
            radioButtonBalanceTypeMonth.setChecked(true);
            radioButtonBalanceTypeTotal.setChecked(false);
        } else if (balanceType == Constantes.BALANCE_TYPE_TOTAL) {
            radioButtonBalanceTypeTotal.setChecked(true);
            radioButtonBalanceTypeMonth.setChecked(false);
        }

        float yellow = sharedPreferences.getFloat(Constantes.SP_KEY_YELLOW, Constantes.SP_YELLOW_DEFAULT);
        float red = sharedPreferences.getFloat(Constantes.SP_KEY_RED, Constantes.SP_RED_DEFAULT);

        textPercentGreen.setText(String.format("+%.0f",yellow));
        editYellow.setText(String.format("%.0f",yellow));
        editRed.setText(String.format("%.0f",red));

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.ic_back);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_confirm) {
            if(editYellow.getText().toString().equals(EMPTY) || editRed.getText().toString().equals(EMPTY)){
                Toast.makeText(this, ERROR_INVALID_INPUT, Toast.LENGTH_SHORT).show();
            } else {
                float yellow = Float.valueOf(editYellow.getText().toString());
                float red = Float.valueOf(editRed.getText().toString());
                int radioSelectedId = radioGroupBalanceType.getCheckedRadioButtonId();
                int balanceType = Constantes.SP_BALANCE_TYPE_DEFAULT;
                switch (radioSelectedId){
                    case R.id.fragment_settings_radio_balance_type_month:
                        balanceType = Constantes.BALANCE_TYPE_MONTH;
                        break;
                    case R.id.fragment_settings_radio_balance_type_total:
                        balanceType = Constantes.BALANCE_TYPE_TOTAL;
                        break;
                }
               // if ((yellow > 0 && yellow < 100) && (yellow > 0 && yellow < 100)) {
                if (yellow > red) {
                    SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putInt(Constantes.SP_KEY_BALANCE_TYPE, balanceType);

                    editor.putFloat(Constantes.SP_KEY_YELLOW, yellow);
                    editor.putFloat(Constantes.SP_KEY_RED, red);

                    editor.apply();

                    setResult(RESULT_OK);
                    finish();
                    return true;
                } else {
                    Toast.makeText(this, MSG_ERROR, Toast.LENGTH_SHORT).show();
                }
               // } else {
                    //Toast.makeText(this, MSG_ERROR_INTERVAL, Toast.LENGTH_SHORT).show();
               // }
            }
        } else if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
