package com.example.final_project.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBOpener extends SQLiteOpenHelper {

        protected final static String DATABASE_NAME = "GAURDIAN.db";
        protected final static int VERSION_NUM = 1;
        public final static String TABLE_NAME = "STORY";
        public final static String COL_STORY_ID = "STORY_ID";
        public final static String COL_CATEGORY = "CATEGORY";
        public final static String COL_DATE = "DATE";
        public final static String COL_TITLE = "TITLE";
        public final static String COL_URL = "URL";
        public final static String COL_FAVOURITE = "FAVOURITE";
        public final static String COL_ID = "_id";
        private boolean isDatabaseInitialized = false;

    public DBOpener(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
        isDatabaseInitialized = true;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!isDatabaseInitialized) {
            dropTable(db);
            createTable(db);
            isDatabaseInitialized = true;
        }
    }

    private void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    private void createTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME +
                        "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COL_STORY_ID + " text, "
                        + COL_CATEGORY + " text, "
                        + COL_DATE + " text, "
                        + COL_TITLE + " text, "
                        + COL_URL + " text, "
                        + COL_FAVOURITE + " INTEGER "
                        + ");"
        );
    }

    public long addNewStory(String storyID, String category, String date, String title, String url, int favourite) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_STORY_ID, storyID);
        values.put(COL_CATEGORY, category);
        values.put(COL_DATE, date);
        values.put(COL_TITLE, title);
        values.put(COL_URL, url);
        values.put(COL_FAVOURITE, favourite);

        long id = db.insert(TABLE_NAME, null, values);

        return id;
    }

    public long updateStory(Story story) {
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println();

        ContentValues values = new ContentValues();
        values.put(COL_STORY_ID, story.getStoryID());
        values.put(COL_CATEGORY, story.getCategory());
        values.put(COL_DATE, story.getDate());
        values.put(COL_TITLE, story.getTitle());
        values.put(COL_URL, story.getUrl());
        values.put(COL_FAVOURITE, story.isFavourite());

        String[] idValue = new String[] { story.getTitle() };

        long id = db.update(TABLE_NAME, values, COL_TITLE + "=?", idValue);
        return id;
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

    public void printCursor(Cursor c) {
        SQLiteDatabase db = this.getWritableDatabase();
        String database = String.valueOf(db.getVersion());
        String columnsTotal = String.valueOf(c.getColumnCount());
        String[] columnNames = c.getColumnNames();
        int resultsTotal = c.getCount();

        System.out.printf("Database Version: %-5s Total Columns: %-5s Total Rows: %-5d\n", database, columnsTotal, resultsTotal);
        System.out.printf("%-4s %-120s %-15s %-20s %-120s %-150s %-5s\n", columnNames[0], columnNames[1], columnNames[2], columnNames[3], columnNames[4], columnNames[5], columnNames[6]);

        int idIndex = c.getColumnIndex(DBOpener.COL_ID);
        int storyIdIndex = c.getColumnIndex(DBOpener.COL_STORY_ID);
        int categoryIndex = c.getColumnIndex(DBOpener.COL_CATEGORY);
        int dateIndex = c.getColumnIndex(DBOpener.COL_DATE);
        int titleIndex = c.getColumnIndex(DBOpener.COL_TITLE);
        int urlIndex = c.getColumnIndex(DBOpener.COL_URL);
        int favouriteIndex = c.getColumnIndex(DBOpener.COL_FAVOURITE);

        while (c.moveToNext()) {
            long id = c.getLong(idIndex);
            String storyIdText = c.getString(storyIdIndex);
            String categoryText = c.getString(categoryIndex);
            String dateText = c.getString(dateIndex);
            String titleText = c.getString(titleIndex);
            String urlText = c.getString(urlIndex);
            boolean isFavourite;
            if (c.getInt(favouriteIndex) != 0) {
                isFavourite = true;
            } else {
                isFavourite = false;
            }

            System.out.printf("%-4s %-120s %-15s %-20s %-120s %-150s %-5s\n", id, storyIdText, categoryText, dateText, titleText, urlText, isFavourite);
        }
//        db.close();
    }

}
