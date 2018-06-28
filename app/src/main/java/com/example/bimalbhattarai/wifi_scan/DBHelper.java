package com.example.bimalbhattarai.wifi_scan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Bimal Bhattarai on 6/21/2018.
 */

public class DBHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "wifi.db";
    private static final int DB_VERSION = 1;

    //Table for measurement
    private static final String ID = "id";
    private static final String SSID = "SSID";
    private static final String VALUE = "VALUE";
    private static final String TABLENAME = "Fingerprint";


    private static DBHelper mInstance;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "create table " + TABLENAME + " (ID INTEGER, SSID TEXT, VALUE FLOAT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        onCreate(db);
    }

    public void insert(String a, Float b) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("scan","reach here");
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("SSID", a);
        contentvalues.put("VALUE", b);
        db.insert("Fingerprint", null, contentvalues);
        db.close();
    }
}
