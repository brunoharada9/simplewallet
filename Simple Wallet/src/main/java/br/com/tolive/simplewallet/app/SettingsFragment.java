package br.com.tolive.simplewallet.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import br.com.tolive.simplewallet.constants.Constantes;
import br.com.tolive.simplewallet.utils.ThemeChanger;
import br.com.tolive.simplewallet.views.CustomRadioButton;


public class SettingsFragment extends Fragment {
    //private static final String MSG_ERROR_INTERVAL = "Os valores devem estar entre 0 e 99";
    private static final String MSG_ERROR = "ERRO - O valor amarelo deve ser maior que o vermelho";
    private static final String MSG_SUCESS = "Configurações salvas com sucesso";
    private static final String EMPTY = "";
    private static final String ERROR_INVALID_INPUT = "Valor inválido";
    private EditText editYellow;
    private EditText editRed;
    private TextView textPercentGreen;
    private RadioGroup radioGroupBalanceType;
    private CustomRadioButton radioButtonBalanceTypeTotal;
    private CustomRadioButton radioButtonBalanceTypeMonth;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();


        ThemeChanger themeChanger = new ThemeChanger((AppCompatActivity) getActivity());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int color = themeChanger.setThemeColor(sharedPreferences.getInt(Constantes.SP_KEY_MONTH, Constantes.SP_MONTH_DEFAULT), null);
        themeChanger.setMenuColor(getActivity().findViewById(R.id.fragment_menu), color);
        themeChanger.setConfigColor(getView(), color);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_settings, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        editYellow = (EditText) view.findViewById(R.id.fragment_settings_edit_yellow);
        editRed = (EditText) view.findViewById(R.id.fragment_settings_edit_red);
        textPercentGreen = (TextView) view.findViewById(R.id.fragment_settings_text_color_set_percent_green_number);

        radioGroupBalanceType = (RadioGroup) view.findViewById(R.id.fragment_settings_radio_group_balance_type);
        radioButtonBalanceTypeTotal = (CustomRadioButton) view.findViewById(R.id.fragment_settings_radio_balance_type_total);
        radioButtonBalanceTypeMonth = (CustomRadioButton) view.findViewById(R.id.fragment_settings_radio_balance_type_month);
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

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.ic_action_navigation_menu);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save_settings) {
            if(editYellow.getText().toString().equals(EMPTY) || editRed.getText().toString().equals(EMPTY)){
                Toast.makeText(getActivity(), ERROR_INVALID_INPUT, Toast.LENGTH_SHORT).show();
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
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constantes.SHARED_PREFERENCES, Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putInt(Constantes.SP_KEY_BALANCE_TYPE, balanceType);

                    editor.putFloat(Constantes.SP_KEY_YELLOW, yellow);
                    editor.putFloat(Constantes.SP_KEY_RED, red);

                    editor.apply();

                    Toast.makeText(getActivity(), MSG_SUCESS, Toast.LENGTH_SHORT).show();

                    return true;
                } else {
                    Toast.makeText(getActivity(), MSG_ERROR, Toast.LENGTH_SHORT).show();
                }
               // } else {
                    //Toast.makeText(this, MSG_ERROR_INTERVAL, Toast.LENGTH_SHORT).show();
               // }
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
