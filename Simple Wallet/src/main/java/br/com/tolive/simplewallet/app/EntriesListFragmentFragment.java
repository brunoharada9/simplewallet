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
import br.com.tolive.simplewallet.utils.DialogAddEntryMaker;

public class EntriesListFragmentFragment extends Fragment implements MenuActivity.OnFiltroApplyListener{
    private static final int FIRST_ELEMENT = 0;
    private static final int EMPTY_BACKSTACK = 0;
    private static final int DATE_YEAR = 2;
    private static final int NO_ROWS_AFFECTED = 0;

    ArrayList<Entry> entries;
    Entry entry;
    EntryDAO dao;
    LinearLayout containerBalance;
    ListView entriesList;
    TextView textBalanceNumber;
    int month;

    int prevMonth;
    int prevYear;

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        final MenuItem itemEdit = menu.add(getActivity().getResources().getString(R.string.fragment_list_contextmenu_item_edit));
        itemEdit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
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
