package fr.masciulli.drinks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Drinks.db";

    public static final String TABLE_DRINKS = "drinks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_HISTORY = "history";
    public static final String COLUMN_WIKIPEDIA = "wikipedia";
    public static final String COLUMN_INSTRUCTIONS = "instructions";

    private static final String SQL_CREATE_DRINKS =
            "CREATE TABLE " + TABLE_DRINKS + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY, "
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_IMAGE_URL + " TEXT,"
                    + COLUMN_HISTORY + " TEXT,"
                    + COLUMN_WIKIPEDIA + " TEXT,"
                    + COLUMN_INSTRUCTIONS + " TEXT)";
    private static final String SQL_DELETE_ALL_DRINKS = "DELETE FROM " + TABLE_DRINKS;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_DRINKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(SQL_DELETE_ALL_DRINKS);
    }
}
