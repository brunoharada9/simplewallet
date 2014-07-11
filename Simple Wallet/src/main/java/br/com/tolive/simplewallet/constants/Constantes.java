package br.com.tolive.simplewallet.constants;

import br.com.tolive.simplewallet.app.R;

/**
 * Created by bruno.carvalho on 30/06/2014.
 */
public class Constantes {
    public static final String FONT_PATH_ROBOTO_CONDENSED_BOLD = "fonts/RobotoCondensed-Bold.ttf";

    public static final String NAV_DRAWER_ITEMS[] = { "Adicionar", "Lista", "Info", "Graficos" };
    public static final int NAV_DRAWER_ICONS[] = { R.drawable.ic_add, R.drawable.ic_list
                                                  ,R.drawable.ic_about, android.R.drawable.ic_menu_info_details };

    public static final int NAV_DRAWER_INDEX_ADD = 0;
    public static final int NAV_DRAWER_INDEX_LIST = 1;
    public static final int NAV_DRAWER_INDEX_ABOUT = 2;
    //public static final int NAV_DRAWER_INDEX_GARPH = 0;

    public static final String SHARED_PREFERENCES = "preferences";
    public static final String SP_KEY_YELLOW = "yellow";
    public static final String SP_KEY_RED = "red";
    public static final float SP_YELLOW_DEFAULT = 500;
    public static final float SP_RED_DEFAULT = 200;

    public static final String SPINNER_MONTH_ITENS[] = { "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez" };
    public static final String SPINNER_YEARS_ITENS[] = { "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020" };

    public static final String PACKAGE_TOLIVE_HEALTHY = "br.gmob.tolivehealthy";
    public static final String INSTANCE_SAVE_MENUACTIVITY_ACTIONBARICON = "instance_save_menuactivity_actonBarIcon";
}
