package br.com.tolive.simplewallet.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
<<<<<<< HEAD
import android.util.TypedValue;
=======
>>>>>>> 1ed1f099f6b39cf259cf77eed17b012c02a76bf3
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
<<<<<<< HEAD
import android.widget.LinearLayout;
=======
>>>>>>> 1ed1f099f6b39cf259cf77eed17b012c02a76bf3
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import br.com.tolive.simplewallet.constants.Constantes;
import br.com.tolive.simplewallet.db.EntryDAO;
import br.com.tolive.simplewallet.model.Entry;
<<<<<<< HEAD
import br.com.tolive.simplewallet.utils.DialogAddEntryMaker;

public class AddFragment extends Fragment {
    public static final String EMPTY = "";
    public static final int DIALOG_TITLE_SIZE = 25;
=======

public class AddFragment extends Fragment {
    public static final String EMPTY = "";
>>>>>>> 1ed1f099f6b39cf259cf77eed17b012c02a76bf3
    TextView buttonAdd;
    TextView textBalance;
    TextView textGain;
    TextView textExpense;
    RelativeLayout background;
    EntryDAO dao;
<<<<<<< HEAD
    AlertDialog dialog;
=======
>>>>>>> 1ed1f099f6b39cf259cf77eed17b012c02a76bf3

    public AddFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add, container, false);

        dao = EntryDAO.getInstance(getActivity());

        buttonAdd = (TextView) rootView.findViewById(R.id.fragment_add_button_add);
        textBalance = (TextView) rootView.findViewById(R.id.fragment_add_text_balance);
        textGain = (TextView) rootView.findViewById(R.id.fragment_add_text_gain);
        textExpense = (TextView) rootView.findViewById(R.id.fragment_add_text_expense);
        background = (RelativeLayout) rootView.findViewById(R.id.fragment_add_background);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), Constantes.FONT_PATH_ROBOTO_CONDENSED_BOLD);
        textBalance.setTypeface(tf);
        textGain.setTypeface(tf);
        textExpense.setTypeface(tf);

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);

        refreshBalanceText(month);
        refreshBackGround(month);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
<<<<<<< HEAD
                DialogAddEntryMaker dialogAddEntryMaker = new DialogAddEntryMaker(getActivity());
                dialogAddEntryMaker.setOnClickOkListener(new DialogAddEntryMaker.OnClickOkListener() {
                    @Override
                    public void onClickOk(Entry entry) {
                        if (dao.insert(entry) != -1) {
                            Toast.makeText(getActivity(), R.string.dialog_add_sucess, Toast.LENGTH_SHORT).show();
                            refreshBackGround(entry.getMonth());
                            refreshBalanceText(entry.getMonth());
                        } else {
                            Toast.makeText(getActivity(), R.string.dialog_add_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog = dialogAddEntryMaker.makeAddDialog();
=======
                AlertDialog dialog = makeAddDialog();
>>>>>>> 1ed1f099f6b39cf259cf77eed17b012c02a76bf3
                dialog.show();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        refreshBalanceText(month);
        refreshBackGround(month);
    }

<<<<<<< HEAD
=======
    private AlertDialog makeAddDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater)   getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_add, null);

        dialog
                .setTitle(R.string.dialog_add_title)
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText editTextDescription = (EditText) view.findViewById(R.id.dialog_add_edittext_description);
                        EditText editTextValue = (EditText) view.findViewById(R.id.dialog_add_edittext_value);
                        RadioGroup radioGroupType = (RadioGroup) view.findViewById(R.id.dialog_add_radiogroup_type);
                        DatePicker datePicker = (DatePicker) view.findViewById(R.id.dialog_add_datepicker);

                        if (editTextDescription.getText().toString().equals(EMPTY))
                            editTextDescription.setText(R.string.dialog_add_no_descripition);

                        String description = editTextDescription.getText().toString();
                        Float value = Float.parseFloat(editTextValue.getText().toString());

                        int typeRadioButtonId = radioGroupType.getCheckedRadioButtonId();
                        int type = typeRadioButtonId == R.id.dialog_add_radiobutton_expense ? Entry.TYPE_EXPENSE : Entry.TYPE_GAIN;

                        String category = "default";

                        int month = datePicker.getMonth();

                        String date = datePicker.getDayOfMonth() + "/" + (month + 1) + "/" + datePicker.getYear();

                        if (value.equals(EMPTY)) {
                            Toast.makeText(getActivity(), R.string.dialog_add_invalid_value, Toast.LENGTH_SHORT).show();
                        } else {
                            Entry entry = new Entry();
                            entry.setDescription(description);
                            entry.setValue(value);
                            entry.setType(type);
                            entry.setCategory(category);
                            entry.setDate(date);
                            entry.setMonth(month);

                            if (dao.insert(entry) != -1) {
                                Toast.makeText(getActivity(), R.string.dialog_add_sucess, Toast.LENGTH_SHORT).show();
                                refreshBalanceText(month);
                                refreshBackGround(month);
                            } else {
                                Toast.makeText(getActivity(), R.string.dialog_add_error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        return dialog.create();
    }

>>>>>>> 1ed1f099f6b39cf259cf77eed17b012c02a76bf3
    private void refreshBalanceText(int month) {
        Float balance = new Float(dao.getMonthBalance(month));
        Float gain = dao.getGain(month);
        Float expense = dao.getExpense(month);
        textBalance.setText("Saldo: " + String.format("%.2f", balance));
        textGain.setText("Ganho: " + String.format("%.2f", gain));
        textExpense.setText("Despesa: " + String.format("%.2f", expense));
    }

    private void refreshBackGround(int month){

        Float gain = dao.getGain(month);
        Float expense = dao.getExpense(month);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        float yellow = sharedPreferences.getFloat(Constantes.SP_KEY_YELLOW, Constantes.SP_YELLOW_DEFAULT);
        float red = sharedPreferences.getFloat(Constantes.SP_KEY_RED, Constantes.SP_RED_DEFAULT);

        int color;
        if((gain - expense) < red){
            color = getActivity().getResources().getColor(R.color.red);
        } else if((gain - expense) < yellow){
            color = getActivity().getResources().getColor(R.color.yellow);
        } else{
            color = getActivity().getResources().getColor(R.color.green);
        }

        background.setBackgroundColor(color);
        buttonAdd.setBackgroundColor(color);
    }
<<<<<<< HEAD

=======
>>>>>>> 1ed1f099f6b39cf259cf77eed17b012c02a76bf3
}