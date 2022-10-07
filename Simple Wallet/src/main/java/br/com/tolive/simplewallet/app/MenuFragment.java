package br.com.tolive.simplewallet.app;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import br.com.tolive.simplewallet.adapter.MenuItemListAdapter;
import br.com.tolive.simplewallet.model.MenuItem;
import br.com.tolive.simplewallet.utils.LayoutHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {
    public static final String KEY_MENU_ITENS = "key_menu_itens";

    private ArrayList<MenuItem> menuItens;
    private OnMenuItemClickListener mMenuItemClickListener;

    public MenuFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        
        this.menuItens = (ArrayList<MenuItem>) getArguments().getSerializable(KEY_MENU_ITENS);
        final MenuItemListAdapter adapter = new MenuItemListAdapter(getActivity(), menuItens);

        ListView listItens = (ListView) view.findViewById(R.id.fragment_menu_list);
        listItens.setAdapter(adapter);

        listItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int count = parent.getChildCount();
                for (int i = 0; i < count; i++){
                    parent.getChildAt(i).setBackgroundColor(getActivity().getResources().getColor(R.color.snow));
                }
                view.setBackgroundColor(getActivity().getResources().getColor(R.color.clicked));
                if (mMenuItemClickListener != null) {
                    mMenuItemClickListener.onMenuItemClick(position, (MenuItem) adapter.getItem(position));
                }
            }
        });

        ImageView imageView = (ImageView) view.findViewById(R.id.fragment_menu_image_content);

        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();

        final TypedArray styledAttributes = getActivity().getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        float actionBarSize = styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        float height =  9 * ((displayMetrics.widthPixels - LayoutHelper.dpToPixel(getActivity(), 56) - actionBarSize)/16);
        imageView.setMinimumHeight((int) height);

        return view;
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener mMenuItemClickListener){
        this.mMenuItemClickListener = mMenuItemClickListener;
    }

    public interface OnMenuItemClickListener {
        public void onMenuItemClick(int position, MenuItem menuItem);
    }
}
