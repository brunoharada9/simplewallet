package br.com.tolive.simplewallet.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import br.com.tolive.simplewallet.constants.Constantes;
import br.com.tolive.simplewallet.db.EntryDAO;
import br.com.tolive.simplewallet.model.Entry;
import br.com.tolive.simplewallet.utils.EntryConverter;
import br.com.tolive.simplewallet.utils.ThemeChanger;

/**
 * Created by bruno.carvalho on 10/07/2014.
 */
public class StoreFragment extends Fragment{

    public static final String SAVE_ERROR = "save_error";

    public StoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().invalidateOptionsMenu();


        ThemeChanger themeChanger = new ThemeChanger((ActionBarActivity) getActivity());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int color = themeChanger.setThemeColor(sharedPreferences.getInt(Constantes.SP_KEY_MONTH, Constantes.SP_MONTH_DEFAULT), null);
        themeChanger.setMenuColor(getActivity().findViewById(R.id.fragment_menu_list), color);
        themeChanger.setAllViewsColor(getView(), color);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        ImageView imageMailStore = (ImageView) view.findViewById(R.id.fragment_store_image_email);

        imageMailStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DialogEmailMaker dialogEmailMaker = new DialogEmailMaker(getActivity());
                //dialogEmailMaker.setOnClickOkListener(new DialogEmailMaker.OnClickOkListener() {
                    //@Override
                    //public void onClickOk(String month, String year) {
                        /*EntryDAO dao = EntryDAO.getInstance(getActivity());
                        ArrayList<Entry> entries;
                        if (year.equals(DialogEmailMaker.SPINNER_SELECTED_DEFAULT_STRING) && month.equals(DialogEmailMaker.SPINNER_SELECTED_DEFAULT_STRING)) {
                            entries = dao.getEntry(null, null);
                        } else if (year.equals(DialogEmailMaker.SPINNER_SELECTED_DEFAULT_STRING)) {
                            entries = dao.getEntry(month, null);
                        } else if (month.equals(DialogEmailMaker.SPINNER_SELECTED_DEFAULT_STRING)) {
                            entries = dao.getEntry(null, year);
                        } else {
                            entries = dao.getEntry(month, year);
                        }*/
                        String filename = saveOnSdCard();
                        File file = null;
                        if(!filename.equals(SAVE_ERROR)) {
                            file = getFromSdCard(filename);
                        }
                        Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.fragment_store_text_email_subject));
                        intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.fragment_store_text_email_instructions));
                        if(file != null) {
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                        }
                        intent.setData(Uri.parse("mailto:")); // or just "mailto:" for blank
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                        startActivity(intent);
                   // }
                //});
                //AlertDialog emailDialog = dialogEmailMaker.makeRecoveryDialog();
                //emailDialog.show();
            }
        });

        ImageView imageSdCardStore = (ImageView) view.findViewById(R.id.fragment_store_image_sdcard);

        imageSdCardStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOnSdCard();
            }
        });

        return view;
    }

    private File getFromSdCard(String filename) {
        Log.d("TAG", "File path: " + Constantes.STORE_FOLDER_NAME + "/" + filename);
        return new File(filename, Constantes.STORE_FOLDER_NAME + "/" + filename);
    }

    private String saveOnSdCard() {
        String filename = SAVE_ERROR;
        if(isExternalStorageWritable()){
            try {
                File newFolder = new File(Environment.getExternalStorageDirectory(), Constantes.STORE_FOLDER_NAME);
                if (!newFolder.exists()) {
                    newFolder.mkdir();
                }
                try {
                    Calendar calendar = Calendar.getInstance();
                    filename = Constantes.STORE_FILE_NAME +
                            //TODO : Option to store more them 1 file
                            "_" +
                            calendar.get(Calendar.DAY_OF_MONTH) +
                            "_" +
                            (calendar.get(Calendar.MONTH) + 1) +
                            "_" +
                            calendar.get(Calendar.YEAR) +
                            Constantes.STORE_FILE_EXT;
                    File file = new File(newFolder, filename);
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    EntryDAO dao = EntryDAO.getInstance(getActivity());
                    ArrayList<Entry> entries = dao.getEntry(null, null);
                    Log.d("TAG", entries.toString());
                    String entriesJSON = EntryConverter.toJson(entries);
                    Log.d("TAG", entriesJSON);
                    FileOutputStream fos;
                    byte[] data = entriesJSON.getBytes();
                    try {
                        fos = new FileOutputStream(file);
                        fos.write(data);
                        fos.flush();
                        fos.close();
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.fragment_store_text_sucess), Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.fragment_store_text_fail), Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.fragment_store_text_fail), Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.fragment_store_text_fail), Toast.LENGTH_SHORT).show();
                throw new RuntimeException(e);
            }
            return filename;
        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.fragment_store_text_fail_open_SD), Toast.LENGTH_SHORT).show();
        }
        return filename;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
