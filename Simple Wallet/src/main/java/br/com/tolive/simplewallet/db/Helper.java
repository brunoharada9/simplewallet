package br.com.tolive.simplewallet.db;

import br.com.tolive.simplewallet.model.Entry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Helper extends SQLiteOpenHelper {

    private static String mDBName = "simple_wallet";
    private static int mVersion = 2;

    private static final String[] DB_CREATE_SCRIPT = { "CREATE TABLE "
            + Entry.ENTITY_NAME + " ( " + Entry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Entry.DESCRIPTION + " TEXT, " + Entry.VALUE + " FLOAT, " + Entry.TYPE + " INTEGER, "
            + Entry.CATEGORY + " TEXT, " + Entry.DATE + " TEXT, " + Entry.MONTH + " INTEGER );" };

    private static final String[] DB_DESTROY_SCRIPT = {"DROP TABLE IF EXISTS " + Entry.ENTITY_NAME + ";"};

    public Helper(Context context) {
        super(context, mDBName, null, mVersion);
        // this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String SQL : DB_CREATE_SCRIPT) {
            db.execSQL(SQL);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO save user data before destroy the table
        ArrayList<Entry> entries = saveData(db);
        for (String destroyTable : DB_DESTROY_SCRIPT) {
            db.execSQL(destroyTable);
        }
        for (String SQL : DB_CREATE_SCRIPT) {
            db.execSQL(SQL);
        }
        setData(db, entries);
    }

    private ArrayList<Entry> saveData(SQLiteDatabase db) {
        ArrayList<Entry> entries = new ArrayList<Entry>();

        String selection = String.format("SELECT * FROM %s", Entry.ENTITY_NAME);
        String[] selectionArgs = {};

        //SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selection, selectionArgs);

        while(cursor.moveToNext()){
            Entry entry = new Entry();

            entry.setId(cursor.getLong(cursor.getColumnIndex(Entry.ID)));
            entry.setDescription(cursor.getString(cursor.getColumnIndex(Entry.DESCRIPTION)));
            entry.setValue(cursor.getFloat(cursor.getColumnIndex(Entry.VALUE)));
            entry.setType(cursor.getInt(cursor.getColumnIndex(Entry.TYPE)));
            entry.setCategory(cursor.getString(cursor.getColumnIndex(Entry.CATEGORY)));
            entry.setDate(cursor.getString(cursor.getColumnIndex(Entry.DATE)));
            entry.setMonth(cursor.getInt(cursor.getColumnIndex(Entry.MONTH)));

            entries.add(entry);
        }

        cursor.close();
        //db.close();

        return entries;
    }

    public void setData(SQLiteDatabase db, ArrayList<Entry> entries) {
        //SQLiteDatabase db = getWritableDatabase();

        for (Entry entry : entries) {
            ContentValues values = new ContentValues();

            values.put(Entry.DESCRIPTION, entry.getDescription());
            values.put(Entry.VALUE, entry.getValue());
            values.put(Entry.TYPE, entry.getType());
            values.put(Entry.CATEGORY, entry.getCategory());
            values.put(Entry.DATE, entry.getDate());
            values.put(Entry.MONTH, entry.getMonth());


            db.insert(Entry.ENTITY_NAME, null, values);
        }
        
        //db.close();
    }
}
