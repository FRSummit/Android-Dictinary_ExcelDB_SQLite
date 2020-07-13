package com.frsummit.sl_dictionary_2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DbHelp extends SQLiteOpenHelper {

    String dbName;
    Context context;
    String dbPath;

    String tableName = "Dictionary";
    String EngCol = "English";

    public DbHelp(Context context, String name, int version) {
        super(context, name, null, version);

        this.context = context;
        this.dbName = name;
        this.dbPath = "/data/data/" + "analog.mydisc" + "/databases";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void checkDb() {
        SQLiteDatabase checkDb = null;
        try {
            String filePath = dbPath + dbName;
            checkDb = SQLiteDatabase.openDatabase(filePath, null, 0);
        }catch (Exception e) {
            if(checkDb != null) {
                Log.d("CheckDb", "Database already exist");
                checkDb.close();
            }else {
                copyDatabase();
            }
        }
    }

    private void copyDatabase() {
        this.getReadableDatabase();
        try {
            InputStream is = context.getAssets().open(dbName);
            OutputStream os = new FileOutputStream(dbPath + dbName);
            byte[] buffer = new byte[1024];
            int len;
            while((len = is.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }
            os.flush();
            is.close();
            os.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("CopyDb", "Database copied");
    }

    public void openDatabase() {
        String filePath = dbPath + dbName;
        SQLiteDatabase.openDatabase(filePath, null, 0);
    }

    public ArrayList<String> getEngWord(String query) {
        ArrayList<String> engList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                tableName,
                new String[]{EngCol},
                EngCol + " LIKE ?",
                new String[]{query + "%"},
                null, null,
                EngCol
        );

        int index = cursor.getColumnIndex(EngCol);
        while(cursor.moveToNext()){
            engList.add(cursor.getString(index));
        }
        sqLiteDatabase.close();
        cursor.close();
        return engList;
    }
}
