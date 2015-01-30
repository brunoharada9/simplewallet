package br.com.tolive.simplewallet.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.text.NumberFormat;

import br.com.tolive.simplewallet.app.R;
import br.com.tolive.simplewallet.model.Entry;

/**
 * Created by bruno.carvalho on 04/07/2014.
 */
public class DialogDetailsMaker {

    private OnClickEditListener mListener;
    private Context context;
    AlertDialog dialog;

    public DialogDetailsMaker(Context context){
        this.context = context;
    }

    public AlertDialog makeDetailsDialog(Entry entry){
        this.dialog = makeCustomDetailsDialog(entry);
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
    private AlertDialog makeCustomDetailsDialog(final Entry entry) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater)   context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_details, null);

        TextView textDescription = (TextView) view.findViewById(R.id.dialog_details_description);
        TextView textValue = (TextView) view.findViewById(R.id.dialog_details_value);
        TextView textDate = (TextView) view.findViewById(R.id.dialog_details_date);

        TextView cancelButton = (TextView) view.findViewById(R.id.dialog_details_text_cancel);
        TextView editButton = (TextView) view.findViewById(R.id.dialog_details_text_edit);

        textDescription.setMovementMethod(new ScrollingMovementMethod());

        if(entry != null){
            textDescription.setText(entry.getDescription());
            String formatted = NumberFormat.getCurrencyInstance().format((entry.getValue()));
            textValue.setText(formatted.replaceAll("[.]", "t").replaceAll("[,]",".").replaceAll("[t]",","));
            textDate.setText(entry.getDate());
            if (entry.getType() == Entry.TYPE_GAIN) {
                ThemeChanger.setDialogTheme(context, view, ThemeChanger.THEME_GREEN);
            } else {
                ThemeChanger.setDialogTheme(context, view, ThemeChanger.THEME_RED);
            }
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonCancel) {
                DialogDetailsMaker.this.dialog.cancel();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonEdit) {
                mListener.onClickEdit(entry);
                DialogDetailsMaker.this.dialog.cancel();
            }
        });

        dialog.setView(view);
        return dialog.create();
    }

    public void setOnClickEditListener (OnClickEditListener listener){
        mListener = listener;
    }

    public interface OnClickEditListener {
        public void onClickEdit(Entry entry);
    }
}
