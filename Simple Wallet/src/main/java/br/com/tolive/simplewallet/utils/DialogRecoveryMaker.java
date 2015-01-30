package br.com.tolive.simplewallet.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;

import br.com.tolive.simplewallet.adapter.CustomSpinnerAdapterFile;
import br.com.tolive.simplewallet.app.R;
import br.com.tolive.simplewallet.constants.Constantes;
import br.com.tolive.simplewallet.views.CustomTextView;

/**
 * Created by bruno.carvalho on 10/07/2014.
 */
public class DialogRecoveryMaker {
    public static final int SPINNER_SELECTED_DEFAULT = 0;
    public static final String SPINNER_SELECTED_DEFAULT_STRING = "0";
    private OnClickOkListener mListener;
    private Activity context;
    private ArrayList<File> fileList;
    AlertDialog dialog;

    public DialogRecoveryMaker(Activity context, ArrayList<File> fileList){
        this.fileList = fileList;
        this.context = context;
    }

    public AlertDialog makeRecoveryDialog(){
        this.dialog = makeCustoMailDialog();
        setDialog(dialog);
        return dialog;
    }

    private void setDialog(AlertDialog dialog){
        this.dialog = dialog;
    }

    /**
     * After call this method, call setDialog to update dialog reference
     * so the button don't get NullPointeException.
     *
     * @return custom AlertDialog
     */
    private AlertDialog makeCustoMailDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater)   context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_recovery, null);

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int month = sharedPreferences.getInt(Constantes.SP_KEY_MONTH, Constantes.SP_MONTH_DEFAULT);

        int color = ThemeChanger.getThemeColor(context, month);

        final Spinner spinnerFiles = (Spinner) view.findViewById(R.id.dialog_recovery_spinner_db);

        CustomSpinnerAdapterFile adapterFiles = new CustomSpinnerAdapterFile(context, R.layout.simple_spinner_item, fileList, color);
        adapterFiles.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinnerFiles.setAdapter(adapterFiles);
        spinnerFiles.setSelection(SPINNER_SELECTED_DEFAULT);

        CustomTextView okButton = (CustomTextView) view.findViewById(R.id.dialog_recovery_text_ok);
        CustomTextView cancelButton = (CustomTextView) view.findViewById(R.id.dialog_recovery_text_cancel);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mListener != null){
                   mListener.onClickOk(spinnerFiles.getSelectedItemPosition());
                }

                DialogRecoveryMaker.this.dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogRecoveryMaker.this.dialog.cancel();
            }
        });

        if (color == context.getResources().getColor(R.color.primary_green)) {
            ThemeChanger.setDialogTheme(context, view, ThemeChanger.THEME_GREEN);
        } else {
            ThemeChanger.setDialogTheme(context, view, ThemeChanger.THEME_RED);
        }

        dialog.setView(view);
        return dialog.create();
    }

    public void setOnClickOkListener (OnClickOkListener listener){
        mListener = listener;
    }

    public interface OnClickOkListener {
        public void onClickOk(int position);
    }
}
