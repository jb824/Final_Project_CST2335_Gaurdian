package com.example.final_project.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConnection extends SQLiteOpenHelper {
    protected final static String DATABASE_NAME = "GAURDIAN_DB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "STORY";
    public final static String COL_STORY_ID = "STORY_ID";
    public final static String COL_SUMMARY = "SUMMARY";
    public final static String COL_DATE = "DATE";
    public final static String COL_TITLE = "TITLE";
    public final static String COL_URL = "URL";
    public final static String COL_FAVOURITE = "FAVOURITE";
    public final static String COL_ID = "_id";
    public DBConnection(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME + " " +
                        "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COL_STORY_ID + " TEXT,"
                        + COL_SUMMARY + "TEXT,"
                        + COL_DATE + "TEXT,"
                        + COL_TITLE + "TEXT,"
                        + COL_URL + "TEXT,"
                        + COL_FAVOURITE + "INTEGER);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
