package br.com.tolive.simplewallet.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

import br.com.tolive.simplewallet.adapter.EntriesListAdapter;
import br.com.tolive.simplewallet.constants.Constantes;
import br.com.tolive.simplewallet.db.EntryDAO;
import br.com.tolive.simplewallet.model.Entry;
import br.com.tolive.simplewallet.utils.DialogAddEntryMaker;
import br.com.tolive.simplewallet.utils.DialogDetailsMaker;
import br.com.tolive.simplewallet.utils.ThemeChanger;
import br.com.tolive.simplewallet.views.CustomTextView;
import br.com.tolive.simplewallet.views.CustomViewShadow;
import br.com.tolive.simplewallet.views.FloatingActionButton;

public class EntriesListFragmentFragment extends Fragment implements MenuActivity.OnFiltroApplyListener, View.OnClickListener {
    public static final int EMPTY = -1;
    private static final int FIRST_ELEMENT = 0;
    private static final int DATE_YEAR = 2;
    private static final int NO_ROWS_AFFECTED = 0;
    public static final String EXTRA_KEY_ENTRY_DETAILS = "entry_details";

    private ArrayList<Entry> entries;
    private Entry entry;
    private EntryDAO dao;
    private CustomViewShadow containerBalance;
    private ListView entriesList;
    private CustomTextView textBalanceNumber;
    private CustomTextView textGainNumber;
    private CustomTextView textExpenseNumber;
    private FloatingActionButton mFabButton;
    private AlertDialog dialog;
    private int month;

    private int prevMonth;
    private int prevYear;

    private ThemeChanger themeChanger;

    public EntriesListFragmentFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        dao = EntryDAO.getInstance(getActivity());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        month = sharedPreferences.getInt(Constantes.SP_KEY_MONTH, Constantes.SP_MONTH_DEFAULT);

        entries = dao.getEntry(month);

        themeChanger = new ThemeChanger((ActionBarActivity) getActivity());

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        textBalanceNumber = (CustomTextView) view.findViewById(R.id.fragment_list_text_balance_number);
        textGainNumber = (CustomTextView) view.findViewById(R.id.fragment_list_text_gain_number);
        textExpenseNumber = (CustomTextView) view.findViewById(R.id.fragment_list_text_expense_number);
        entriesList = (ListView) view.findViewById(R.id.fragment_list_list_entries);
        containerBalance = (CustomViewShadow) view.findViewById(R.id.fragment_list_container_balance);
        //container = (LinearLayout) view.findViewById(R.id.fragment_list_container);

        containerBalance.post(new Runnable() {
            @Override
            public void run() {
                // safe to get height and width here
                mFabButton = new FloatingActionButton.Builder(getActivity())
                        .withDrawable(
                                getResources().getDrawable(R.drawable.ic_action_content_create))
                        .withButtonColor(getResources().getColor(R.color.primary_green), getResources().getColor(R.color.bar_green))
                        .withGravity(Gravity.TOP | Gravity.END)
                        .withMarginsInPixels(0, containerBalance.getBottom(),
                                convertToPixels(16), 0).create(container);
                mFabButton.setOnClickListener(EntriesListFragmentFragment.this);
                refreshList(entries);
            }

        });

        registerForContextMenu(entriesList);

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
                DialogDetailsMaker dialogDetailsMaker = new DialogDetailsMaker(getActivity());
                dialogDetailsMaker.setOnClickEditListener(new DialogDetailsMaker.OnClickEditListener() {
                    @Override
                    public void onClickEdit(Entry entry) {
                        createEditDialog(entry);
                    }
                });
                AlertDialog dialog = dialogDetailsMaker.makeDetailsDialog(entries.get(position));
                dialog.show();
            }
        });

        return view;
    }

    @Override
    public void onFiltroApply(ArrayList<Entry> entries, int month) {
        this.entries = entries;
        this.month = month;
        refreshList(entries);
    }

    private void refreshList(ArrayList<Entry> entries) {
        EntriesListAdapter adapter = new EntriesListAdapter(entries, getActivity());
        entriesList.setAdapter(adapter);
        int color = themeChanger.setThemeColor(month, mFabButton);
        themeChanger.setMenuColor(getActivity().findViewById(R.id.list_slidermenu), color);
        containerBalance.setBackgroundColor(getActivity().getResources().getColor(R.color.snow));
        if(color == getResources().getColor(R.color.primary_red)) {
            containerBalance.setColor(getResources().getColor(R.color.bar_red));
        } else if(color == getResources().getColor(R.color.primary_yellow)) {
            containerBalance.setColor(getResources().getColor(R.color.bar_yellow));
        } else if(color == getResources().getColor(R.color.primary_green)) {
            containerBalance.setColor(getResources().getColor(R.color.bar_green));
        }
        EntryDAO dao = EntryDAO.getInstance(getActivity());
        Float gain = dao.getGain(month);
        Float expense = dao.getExpense(month);
        String formatted = NumberFormat.getCurrencyInstance().format((gain-expense));
        textBalanceNumber.setText(String.valueOf(formatted));
        formatted = NumberFormat.getCurrencyInstance().format((gain));
        textGainNumber.setText(String.valueOf(formatted));
        formatted = NumberFormat.getCurrencyInstance().format((expense));
        textExpenseNumber.setText(String.valueOf(formatted));
        saveTheme();
    }

    private void saveTheme() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constantes.SP_KEY_MONTH, month);
        editor.commit();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        final MenuItem itemEdit = menu.add(getActivity().getResources().getString(R.string.fragment_list_contextmenu_item_edit));
        itemEdit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                createEditDialog(entry);
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

    private void createEditDialog(Entry entry) {
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
    }

    @Override
    public void onClick(View v) {
        DialogAddEntryMaker dialogAddEntryMaker = new DialogAddEntryMaker(getActivity());
        dialogAddEntryMaker.setOnClickOkListener(new DialogAddEntryMaker.OnClickOkListener() {
            @Override
            public void onClickOk(Entry entry) {
                if (dao.insert(entry) != -1) {
                    Toast.makeText(getActivity(), R.string.dialog_add_sucess, Toast.LENGTH_SHORT).show();
                    entries = dao.getEntry(month);
                    refreshList(entries);
                } else {
                    Toast.makeText(getActivity(), R.string.dialog_add_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog = dialogAddEntryMaker.makeAddDialog();

        dialog.show();
    }

    // The calculation (value * scale + 0.5f) is a widely used to convert to dps
    // to pixel units
    // based on density scale
    // see developer.android.com (Supporting Multiple Screen Sizes)
    private int convertToPixels(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mFabButton != null) {
            mFabButton.hideFloatingActionButton();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mFabButton != null) {
            mFabButton.showFloatingActionButton();
        }
    }
}
