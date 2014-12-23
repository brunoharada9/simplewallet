package br.com.tolive.simplewallet.app;

import android.os.Build;
import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.internal.en;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.tolive.simplewallet.adapter.EntriesListAdapter;
import br.com.tolive.simplewallet.constants.Constantes;
import br.com.tolive.simplewallet.db.EntryDAO;
import br.com.tolive.simplewallet.model.Entry;
import br.com.tolive.simplewallet.utils.DialogAddEntryMaker;
import br.com.tolive.simplewallet.utils.ThemeChanger;
import br.com.tolive.simplewallet.views.CustomTextView;
import br.com.tolive.simplewallet.views.CustomViewShadow;
import br.com.tolive.simplewallet.views.FloatingActionButton;

public class EntriesListFragmentFragment extends Fragment implements MenuActivity.OnFiltroApplyListener, View.OnClickListener {
    private static final int FIRST_ELEMENT = 0;
    private static final int EMPTY_BACKSTACK = 0;
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

    public EntriesListFragmentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        dao = EntryDAO.getInstance(getActivity());

        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);

        entries = dao.getEntry(month);

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
                                getResources().getDrawable(R.drawable.ic_create_white_36dp))
                        .withButtonColor(getResources().getColor(R.color.primary_green), getResources().getColor(R.color.bar_green))
                        .withGravity(Gravity.TOP | Gravity.END)
                        .withMarginsInPixels(0, containerBalance.getBottom(),
                                convertToPixels(6), 0).create(container);
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
                Intent detailsIntent = new Intent(getActivity(), DetailsActivity.class);
                detailsIntent.putExtra(EXTRA_KEY_ENTRY_DETAILS, entries.get(position));

                startActivity(detailsIntent);
            }
        });

        return view;
    }

    @Override
    public void onFiltroApply(ArrayList<Entry> entries) {
        this.entries = entries;
        if(entries.size() > 0){
            month = entries.get(FIRST_ELEMENT).getMonth();
        }
        refreshList(entries);
    }

    private void refreshList(ArrayList<Entry> entries) {
        EntriesListAdapter adapter = new EntriesListAdapter(entries, getActivity());
        entriesList.setAdapter(adapter);
        int color = ThemeChanger.setThemeColor(((ActionBarActivity) getActivity()), month, mFabButton);
        containerBalance.setBackgroundColor(getActivity().getResources().getColor(R.color.snow));
        containerBalance.setColor(color);
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
