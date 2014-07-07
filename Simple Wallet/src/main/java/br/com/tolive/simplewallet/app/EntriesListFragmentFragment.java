package br.com.tolive.simplewallet.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;

import br.com.tolive.simplewallet.adapter.EntriesListAdapter;
import br.com.tolive.simplewallet.constants.Constantes;
import br.com.tolive.simplewallet.db.EntryDAO;
import br.com.tolive.simplewallet.model.Entry;
<<<<<<< HEAD
import br.com.tolive.simplewallet.utils.DialogAddEntryMaker;

public class EntriesListFragmentFragment extends Fragment implements MenuActivity.OnFiltroApplyListener{
    private static final int FIRST_ELEMENT = 0;
    private static final int EMPTY_BACKSTACK = 0;
    private static final int DATE_YEAR = 2;
=======

public class EntriesListFragmentFragment extends Fragment implements MenuActivity.OnFiltroApplyListener{
    private static final String EMPTY = "";
    private static final int FIRST_ELEMENT = 0;
    private static final int EMPTY_BACKSTACK = 0;
    private static final int DATE_YEAR = 2;
    private static final int DATE_MONTH = 1;
    private static final int DATE_DAY = 0;
>>>>>>> 1ed1f099f6b39cf259cf77eed17b012c02a76bf3
    private static final int NO_ROWS_AFFECTED = 0;

    ArrayList<Entry> entries;
    Entry entry;
    EntryDAO dao;
    LinearLayout containerBalance;
    ListView entriesList;
    TextView textBalanceNumber;
    int month;

<<<<<<< HEAD
    int prevMonth;
    int prevYear;

=======
>>>>>>> 1ed1f099f6b39cf259cf77eed17b012c02a76bf3
    public EntriesListFragmentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dao = EntryDAO.getInstance(getActivity());

        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);

        entries = dao.getEntry(month);

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        TextView textBalance = (TextView) view.findViewById(R.id.fragment_list_text_balance);
        textBalanceNumber = (TextView) view.findViewById(R.id.fragment_list_text_balance_number);
        entriesList = (ListView) view.findViewById(R.id.fragment_list_list_entries);
        containerBalance = (LinearLayout) view.findViewById(R.id.fragment_list_container_balance);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), Constantes.FONT_PATH_ROBOTO_CONDENSED_BOLD);
        textBalance.setTypeface(tf);
        textBalanceNumber.setTypeface(tf);

        registerForContextMenu(entriesList);

        refreshList(entries);

        entriesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                entry = entries.get(position);
                return false;
            }
        });

        entriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Fragment fragment = new DetailsFragment(entries.get(position));
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                ft.replace(R.id.frame_container, fragment);
                ft.addToBackStack("teste");
                ft.commit();
            }
        });

        return view;
    }

<<<<<<< HEAD
=======
    //@Override
    //public void onListItemClick(ListView l, View v, int position, long id) {
    //    super.onListItemClick(l, v, position, id);
//
    //    Fragment fragment = new DetailsFragment(entries.get(position));
     //   FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//
      //  ft.replace(R.id.frame_container, fragment);
     //   ft.addToBackStack("teste");
      //  ft.commit();
    //}

   // @Override
    //public void onActivityCreated(Bundle savedInstanceState) {
    //    super.onActivityCreated(savedInstanceState);
    //    registerForContextMenu(getListView());
    //    setLongClick();
    //}

>>>>>>> 1ed1f099f6b39cf259cf77eed17b012c02a76bf3
    @Override
    public void onFiltroApply(ArrayList<Entry> entries) {
        this.entries = entries;
        if(entries != null){
            month = entries.get(FIRST_ELEMENT).getMonth();
        }
        FragmentManager fm = getActivity().getSupportFragmentManager();
        int backStackNumber = fm.getBackStackEntryCount();
        if(backStackNumber > EMPTY_BACKSTACK){
            fm.popBackStack();
        }
        refreshList(entries);
    }

    private void refreshList(ArrayList<Entry> entries) {
        EntriesListAdapter adapter = new EntriesListAdapter(entries, getActivity());
        entriesList.setAdapter(adapter);
        Float gain = dao.getGain(month);
        Float expense = dao.getExpense(month);

        textBalanceNumber.setText(String.format("%.2f", (gain - expense)));
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
        containerBalance.setBackgroundColor(color);
    }

<<<<<<< HEAD
=======
    private AlertDialog makeAddDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater)   getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add, null);

        final EditText editTextDescription = (EditText) view.findViewById(R.id.dialog_add_edittext_description);
        final EditText editTextValue = (EditText) view.findViewById(R.id.dialog_add_edittext_value);
        final RadioGroup radioGroupType = (RadioGroup) view.findViewById(R.id.dialog_add_radiogroup_type);
        final RadioButton radioGain = (RadioButton) view.findViewById(R.id.dialog_add_radiobutton_gain);
        final RadioButton radioExpense = (RadioButton) view.findViewById(R.id.dialog_add_radiobutton_expense);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.dialog_add_datepicker);

        editTextDescription.setText(entry.getDescription());
        editTextValue.setText(String.format("%.0f", entry.getValue()));
        if (entry.getType() == Entry.TYPE_GAIN) {
            radioGain.setSelected(true);
            Log.d("teste", "g: " + entry.getType());
            //radioExpense.setSelected(false);
        } else {
            Log.d("teste", "e: " + entry.getType());
            //radioGain.setSelected(false);
            radioExpense.setSelected(true);
        }
        String[] split = entry.getDate().split("/");
        final int prevMonth = Integer.valueOf(split[DATE_MONTH]) - 1;
        final int prevYear = Integer.valueOf(split[DATE_YEAR]);
        datePicker.updateDate(Integer.valueOf(split[DATE_YEAR]), Integer.valueOf(split[DATE_MONTH]) - 1, Integer.valueOf(split[DATE_DAY]));

        dialog
                .setTitle(R.string.dialog_add_title)
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

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
                            entry.setDescription(description);
                            entry.setValue(value);
                            entry.setType(type);
                            entry.setCategory(category);
                            entry.setDate(date);
                            entry.setMonth(month);

                            if (dao.update(entry) > NO_ROWS_AFFECTED) {
                                Toast.makeText(getActivity(), R.string.dialog_edit_sucess, Toast.LENGTH_SHORT).show();
                                if (prevMonth != month || prevYear != datePicker.getYear()) {
                                    entries.remove(entry);
                                }
                                refreshList(entries);
                            } else {
                                Toast.makeText(getActivity(), R.string.dialog_edit_error, Toast.LENGTH_SHORT).show();
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
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        final MenuItem itemEdit = menu.add(getActivity().getResources().getString(R.string.fragment_list_contextmenu_item_edit));
        itemEdit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
<<<<<<< HEAD
                prevMonth = entry.getMonth();
                prevYear = Integer.valueOf(entry.getDate().split("/")[DATE_YEAR]);
                DialogAddEntryMaker dialogAddEntryMaker = new DialogAddEntryMaker(getActivity());
                dialogAddEntryMaker.setOnClickOkListener(new DialogAddEntryMaker.OnClickOkListener() {
                    @Override
                    public void onClickOk(Entry entry) {
                        if (dao.update(entry) > NO_ROWS_AFFECTED) {
                            Toast.makeText(getActivity(), R.string.dialog_edit_sucess, Toast.LENGTH_SHORT).show();
                            if (prevMonth != entry.getMonth() || prevYear != Integer.valueOf(entry.getDate().split("/")[DATE_YEAR])) {
                                entries.remove(entry);
                            }
                            refreshList(entries);
                        } else {
                            Toast.makeText(getActivity(), R.string.dialog_edit_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AlertDialog dialog = dialogAddEntryMaker.makeAddDialog(entry);
=======
                AlertDialog dialog = makeAddDialog();
>>>>>>> 1ed1f099f6b39cf259cf77eed17b012c02a76bf3
                dialog.show();
                return false;
            }
        });

        final MenuItem itemDelete = menu.add(getActivity().getResources().getString(R.string.fragment_list_contextmenu_item_delete));
        itemDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dao.delete(entry.getId());
                entries.remove(entry);
                refreshList(entries);
                return false;
            }
        });
    }
}
