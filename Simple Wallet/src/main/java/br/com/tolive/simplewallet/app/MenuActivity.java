package br.com.tolive.simplewallet.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import br.com.tolive.simplewallet.adapter.NavDrawerListAdapter;
import br.com.tolive.simplewallet.constants.Constantes;
import br.com.tolive.simplewallet.model.Entry;
import br.com.tolive.simplewallet.model.NavDrawerItem;


public class MenuActivity extends ActionBarActivity {
    private static final int ICON_SETTINGS = 0;
    private static final int ICON_FILTRO = 1;
    private static final int ICON_NONE = 2;
    private static final int REQUEST_SETTINGS = 0;
    private static final int REQUEST_FILTRO = 1;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    int actionBarIcon = ICON_SETTINGS;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    private OnFiltroApplyListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        AdView adView = (AdView) findViewById(R.id.ad);
        adView.loadAd(request);

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Add
        navDrawerItems.add(new NavDrawerItem(Constantes.NAV_DRAWER_ITEMS[0], Constantes.NAV_DRAWER_ICONS[0]));
        // List
        navDrawerItems.add(new NavDrawerItem(Constantes.NAV_DRAWER_ITEMS[1], Constantes.NAV_DRAWER_ICONS[1]));
        // Graphs
        //navDrawerItems.add(new NavDrawerItem(Constantes.NAV_DRAWER_ITEMS[2], Constantes.NAV_DRAWER_ICONS[2]));
        // About
        navDrawerItems.add(new NavDrawerItem(Constantes.NAV_DRAWER_ITEMS[2], Constantes.NAV_DRAWER_ICONS[2]));


        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new AddFragment();
                actionBarIcon = ICON_SETTINGS;
                break;
            case 1:
                fragment = new EntriesListFragmentFragment();
                mListener = (OnFiltroApplyListener) fragment;
                actionBarIcon = ICON_FILTRO;
                break;
            case 2:
                fragment = new AboutFragment();
                actionBarIcon = ICON_NONE;
                break;
            //case 3:
                //fragment = new GraphFragment();
             //   Toast.makeText(this, "fragment 4", Toast.LENGTH_SHORT).show();
             //   break;

            default:
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(Constantes.NAV_DRAWER_ITEMS[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            //Log.e("", "Error in creating fragment");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_filtro:
                openFiltro();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSettings() {
        Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
        startActivityForResult(intent, REQUEST_SETTINGS);
    }

    private void openFiltro() {
        Intent intent = new Intent(MenuActivity.this, FiltroActivity.class);
        startActivityForResult(intent, REQUEST_FILTRO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_SETTINGS){
            if(resultCode == RESULT_OK){
                //TODO
            }
        } else if(requestCode == REQUEST_FILTRO){
            if(resultCode == RESULT_OK){
                if (mListener != null) {
                    mListener.onFiltroApply((ArrayList<Entry>) data.getSerializableExtra(FiltroActivity.EXTRA_KEY_FILTRO_ENTRIES));
                }
            }
        }
    }

    public interface OnFiltroApplyListener {
        public void onFiltroApply(ArrayList<Entry> entries);
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        switch (actionBarIcon){
            case ICON_SETTINGS:
                menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
                menu.findItem(R.id.action_filtro).setVisible(false);
                break;
            case ICON_FILTRO:
                menu.findItem(R.id.action_settings).setVisible(false);
                menu.findItem(R.id.action_filtro).setVisible(!drawerOpen);
                break;
            case ICON_NONE:
                menu.findItem(R.id.action_settings).setVisible(false);
                menu.findItem(R.id.action_filtro).setVisible(false);
                break;
            default:
                menu.findItem(R.id.action_settings).setVisible(false);
                menu.findItem(R.id.action_filtro).setVisible(false);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
