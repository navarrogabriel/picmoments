package com.unaj.gabbo.picmoments.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gabbo on 27/10/2017.
 */

public class SQLiteDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "momentsdb";
    private static final int DATABASE_VERSION = 2;


    private static final String CREATE_TABLE_QUERY =
            "Create table usuarios (_ID integer primary key autoincrement, fullName text, user text, password text, mobile text);";

    private static final String CREATE_TABLE_QUERY_MOMENT =
            "Create table moment (_ID integer primary key autoincrement, image BLOB, location text, description text, date text);";

    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);
            sqLiteDatabase.execSQL(CREATE_TABLE_QUERY_MOMENT);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(sqLiteDatabase);
    }

}



