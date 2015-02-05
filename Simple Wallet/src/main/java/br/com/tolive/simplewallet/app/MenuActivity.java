package br.com.tolive.simplewallet.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.tolive.simplewallet.constants.Constantes;
import br.com.tolive.simplewallet.model.Entry;
import br.com.tolive.simplewallet.model.MenuItem;
import br.com.tolive.simplewallet.utils.MenuSlider;


public class MenuActivity extends ActionBarActivity {
    private static final int ICON_SETTINGS = 0;
    private static final int ICON_FILTRO = 1;
    private static final int ICON_NONE = 2;
    private static final int REQUEST_SETTINGS = 0;
    private static final int REQUEST_FILTRO = 1;
    public static final int NAV_LIST = 0;
    public static final int NAV_STORE = 1;
    public static final int NAV_RECOVERY = 2;
    public static final int NAV_SETTINGS = 3;
    public static final int NAV_ABOUT = 4;
    public static final int DEFAULT_VALUE = -1;

    //private ActionBar mActionBar;
    int actionBarIcon = ICON_SETTINGS;

    private MenuSlider menuSlider;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<MenuItem> menuItems;

    private OnFiltroApplyListener mFiltroListener;

    private AlertDialog promoDialog;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayPromo();

        sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putBoolean(Constantes.SP_KEY_REMOVE_AD, true);
        //editor.commit();
        boolean removeAd = sharedPreferences.getBoolean(Constantes.SP_KEY_REMOVE_AD, Constantes.SP_REMOVE_AD_DEFAULT);
        if(!removeAd) {
            AdRequest request = new AdRequest.Builder()
                    .addTestDevice("E6E54B90007CAC7A62F9EC7857F3A989")
                    .build();
            AdView adView = (AdView) findViewById(R.id.ad_main);
            adView.loadAd(request);
        } else{
            AdView adView = (AdView) findViewById(R.id.ad_main);
            adView.setVisibility(View.GONE);
        }

        Calendar calendar = Calendar.getInstance();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constantes.SP_KEY_MONTH, calendar.get(Calendar.MONTH));
        editor.commit();

        mTitle = getTitle();
        menuItems = new ArrayList<MenuItem>();
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons_red);
        // adding nav drawer items to array
        // add
        //navDrawerItems.add(new NavDrawerItem(navMenuTitles[NAV_ADD], navMenuIcons.getResourceId(NAV_ADD, DEFAULT_VALUE)));
        // list
        menuItems.add(new MenuItem(navMenuTitles[NAV_LIST], navMenuIcons.getResourceId(NAV_LIST, DEFAULT_VALUE)));
        // store
        menuItems.add(new MenuItem(navMenuTitles[NAV_STORE], navMenuIcons.getResourceId(NAV_STORE, DEFAULT_VALUE)));
        // recovery
        menuItems.add(new MenuItem(navMenuTitles[NAV_RECOVERY], navMenuIcons.getResourceId(NAV_RECOVERY, DEFAULT_VALUE)));
        // recovery
        menuItems.add(new MenuItem(navMenuTitles[NAV_SETTINGS], navMenuIcons.getResourceId(NAV_SETTINGS, DEFAULT_VALUE)));
        // about
        menuItems.add(new MenuItem(navMenuTitles[NAV_ABOUT], navMenuIcons.getResourceId(NAV_ABOUT, DEFAULT_VALUE)));
        menuSlider = new MenuSlider(MenuActivity.this);
        MenuFragment menuFragment = new MenuFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(MenuFragment.KEY_MENU_ITENS, menuItems);
        menuFragment.setArguments(arguments);
        menuFragment.setOnMenuItemClickListener(new MenuFragment.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, MenuItem menuItem) {
                menuSlider.hideMenu();
                setTitle(menuItem.getTitle());
                displayView(position);
            }
        });
        menuSlider.setMenuFragment(menuFragment);
        // Recycle the typed array
        navMenuIcons.recycle();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.ic_action_navigation_menu);
        // enabling action bar app icon and behaving it as toggle button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        if (savedInstanceState == null) {
            displayView(0);
        }
    }

    private void displayPromo() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            Log.d("TAG", "dysplayPromo: " + version);

            if(version.startsWith("1.3.")){
                Log.d("TAG", "version: " + version);
                //Promo: Gastos Simples PRO Release U$ 0.01 \o/
                SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                //SharedPreferences.Editor editor = sharedPreferences.edit();
                //editor.putBoolean(Constantes.SP_KEY_PROMO_DIALOG, true);
                //editor.apply();
                boolean showPromoDialog = sharedPreferences.getBoolean(Constantes.SP_KEY_PROMO_DIALOG, Constantes.SP_PROMO_DIALOG_DEFAULT);
                Log.d("TAG", "showPromoDialog: " + showPromoDialog);
                if(showPromoDialog) {
                    Log.d("TAG", "if: ");
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.dialog_promo, null);
                    TextView okButton = (TextView) view.findViewById(R.id.dialog_promo_text_ok);
                    TextView cancelButton = (TextView) view.findViewById(R.id.dialog_promo_text_cancel);
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View buttonOk) {
                            final String appPackageName = Constantes.PACKAGE_GASTOS_SIMPLES_PRO;
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                            promoDialog.cancel();
                        }
                    });
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View buttonCancel) {
                            promoDialog.cancel();
                        }
                    });
                    dialog.setView(view);
                    dialog.setCustomTitle(inflater.inflate(R.layout.dialog_promo_title, null));
                    promoDialog = dialog.create();
                    promoDialog.show();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(Constantes.SP_KEY_PROMO_DIALOG, false);
                    editor.apply();
                }
            }
        } catch (Exception e) {
            //
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
//            case NAV_ADD:
//                fragment = new AddFragment();
//                actionBarIcon = ICON_SETTINGS;
//                break;
            case NAV_LIST:
                fragment = new EntriesListFragment();
                mFiltroListener = (OnFiltroApplyListener) fragment;
                actionBarIcon = ICON_FILTRO;
                break;
            case NAV_STORE:
                fragment = new StoreFragment();
                actionBarIcon = ICON_NONE;
                break;
            case NAV_RECOVERY:
                fragment = new RecoveryFragment();
                actionBarIcon = ICON_NONE;
                break;
            case NAV_SETTINGS:
                fragment = new SettingsFragment();
                actionBarIcon = ICON_SETTINGS;
                break;
            case NAV_ABOUT:
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
        } else {
            // error in creating fragment
            Log.e("", "Error in creating fragment");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_save_settings:
                return false;
            case R.id.action_filtro:
                openFiltro();
                return true;
            case android.R.id.home:
                if(menuSlider.isMenuVisible()){
                    menuSlider.hideMenu();
                } else {
                    menuSlider.showMenu();
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        if(menuSlider.isMenuVisible()){
            menuSlider.hideMenu();
        } else {
            super.onBackPressed();
        }
    }

    private void openFiltro() {
        Intent intent = new Intent(MenuActivity.this, FiltroActivity.class);
        startActivityForResult(intent, REQUEST_FILTRO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_SETTINGS){

        } else if(requestCode == REQUEST_FILTRO){
            if(resultCode == RESULT_OK){
                if (mFiltroListener != null) {
                    mFiltroListener.onFiltroApply((ArrayList<Entry>) data.getSerializableExtra(FiltroActivity.EXTRA_KEY_FILTRO_ENTRIES), data.getIntExtra(FiltroActivity.EXTRA_KEY_FILTRO_MONTH, AdapterView.INVALID_POSITION));
                }
            }
        }
    }

    public interface OnFiltroApplyListener {
        public void onFiltroApply(ArrayList<Entry> entries, int month);
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items

        switch (actionBarIcon){
            case ICON_SETTINGS:
//                menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
                menu.findItem(R.id.action_filtro).setVisible(false);
                menu.findItem(R.id.action_save_settings).setVisible(true);
                break;
            case ICON_FILTRO:
//                menu.findItem(R.id.action_settings).setVisible(false);
                menu.findItem(R.id.action_filtro).setVisible(true);
                menu.findItem(R.id.action_save_settings).setVisible(false);
                break;
            case ICON_NONE:
//                menu.findItem(R.id.action_settings).setVisible(false);
                menu.findItem(R.id.action_filtro).setVisible(false);
                menu.findItem(R.id.action_save_settings).setVisible(false);
                break;
            default:
//                menu.findItem(R.id.action_settings).setVisible(false);
                menu.findItem(R.id.action_filtro).setVisible(false);
                menu.findItem(R.id.action_save_settings).setVisible(false);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constantes.INSTANCE_SAVE_MENUACTIVITY_ACTIONBARICON, actionBarIcon);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        actionBarIcon = savedInstanceState.getInt(Constantes.INSTANCE_SAVE_MENUACTIVITY_ACTIONBARICON);
    }

    public void removeAd(){
        AdView adView = (AdView) findViewById(R.id.ad_main);
        adView.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Calendar calendar = Calendar.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constantes.SP_KEY_MONTH, calendar.get(Calendar.MONTH));
        editor.commit();
    }
}
