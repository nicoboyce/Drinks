package fr.masciulli.drinks.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.model.Liquor;
import rx.Observable;

public class Database {
    private final SQLiteOpenHelper sqLiteHelper;

    public Database(Context context) {
        sqLiteHelper = new DatabaseHelper(context);
    }

    public Observable<List<Long>> storeDrinks(List<Drink> drinks) {
        return Observable.from(drinks)
                .map(this::createDrinkContentValues)
                .flatMap(this::insertDrinkContentValues)
                .flatMap(this::checkInserted)
                .toList();
    }

    public Observable<List<Long>> storeLiquors(List<Liquor> liquors) {
        return Observable.from(liquors)
                .map(this::createLiquorContentValues)
                .flatMap(this::insertLiquorContentValues)
                .flatMap(this::checkInserted)
                .toList();
    }

    private ContentValues createDrinkContentValues(Drink drink) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, drink.name());
        values.put(DatabaseHelper.COLUMN_IMAGE_URL, drink.imageUrl());
        values.put(DatabaseHelper.COLUMN_HISTORY, drink.history());
        values.put(DatabaseHelper.COLUMN_WIKIPEDIA, drink.wikipedia());
        values.put(DatabaseHelper.COLUMN_INSTRUCTIONS, drink.instructions());
        return values;
    }

    private ContentValues createLiquorContentValues(Liquor liquor) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, liquor.name());
        values.put(DatabaseHelper.COLUMN_IMAGE_URL, liquor.imageUrl());
        values.put(DatabaseHelper.COLUMN_HISTORY, liquor.history());
        values.put(DatabaseHelper.COLUMN_WIKIPEDIA, liquor.wikipedia());
        return values;
    }

    private Observable<Long> insertDrinkContentValues(ContentValues contentValues) {
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();
        return Observable.just(database.insert(DatabaseHelper.TABLE_DRINKS, null, contentValues));
    }

    private Observable<Long> insertLiquorContentValues(ContentValues contentValues) {
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();
        return Observable.just(database.insert(DatabaseHelper.TABLE_LIQUORS, null, contentValues));
    }

    private Observable<Long> checkInserted(long index) {
        return index > 0 ? Observable.just(index) : Observable.error(new SQLException("Failed inserting item"));
    }

    public Observable<Integer> dropAllDrinks() {
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();
        return Observable.just(database.delete(DatabaseHelper.TABLE_DRINKS, null, null));
    }

    public Observable<List<Drink>> getAllDrinks() {
        return getAllDrinksCursor()
                .map(this::drinksCursorToContentValues)
                .flatMap(Observable::from)
                .map(this::contentValuesToDrink)
                .toList();
    }

    private Observable<Cursor> getAllDrinksCursor() {
        SQLiteDatabase database = sqLiteHelper.getReadableDatabase();
        return Observable.just(database.query(DatabaseHelper.TABLE_DRINKS, null, null, null, null, null, null));
    }

    private List<ContentValues> drinksCursorToContentValues(Cursor cursor) {
        List<ContentValues> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_NAME, getFromCursor(cursor, DatabaseHelper.COLUMN_NAME));
            values.put(DatabaseHelper.COLUMN_IMAGE_URL, getFromCursor(cursor, DatabaseHelper.COLUMN_IMAGE_URL));
            values.put(DatabaseHelper.COLUMN_HISTORY, getFromCursor(cursor, DatabaseHelper.COLUMN_HISTORY));
            values.put(DatabaseHelper.COLUMN_WIKIPEDIA, getFromCursor(cursor, DatabaseHelper.COLUMN_WIKIPEDIA));
            values.put(DatabaseHelper.COLUMN_INSTRUCTIONS, getFromCursor(cursor, DatabaseHelper.COLUMN_INSTRUCTIONS));

            result.add(values);
        }
        return result;
    }

    private String getFromCursor(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        return cursor.getString(index);
    }

    private Drink contentValuesToDrink(ContentValues contentValues) {
        String name = contentValues.getAsString(DatabaseHelper.COLUMN_NAME);
        String imageUrl = contentValues.getAsString(DatabaseHelper.COLUMN_IMAGE_URL);
        String history = contentValues.getAsString(DatabaseHelper.COLUMN_HISTORY);
        String wikipedia = contentValues.getAsString(DatabaseHelper.COLUMN_WIKIPEDIA);
        String instructions = contentValues.getAsString(DatabaseHelper.COLUMN_INSTRUCTIONS);
        //TODO fetch real ingredients
        List<String> ingredients = new ArrayList<>();
        return Drink.create(name, imageUrl, history, wikipedia, instructions, ingredients);
    }

    public Observable<List<Liquor>> getAllLiquors() {
        return getAllLiquorsCursor()
                .map(this::liquorsCursorToContentValues)
                .flatMap(Observable::from)
                .map(this::contentValuesToLiquor)
                .toList();
    }

    private Observable<Cursor> getAllLiquorsCursor() {
        SQLiteDatabase database = sqLiteHelper.getReadableDatabase();
        return Observable.just(database.query(DatabaseHelper.TABLE_LIQUORS, null, null, null, null, null, null));
    }

    private List<ContentValues> liquorsCursorToContentValues(Cursor cursor) {
        List<ContentValues> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_NAME, getFromCursor(cursor, DatabaseHelper.COLUMN_NAME));
            values.put(DatabaseHelper.COLUMN_IMAGE_URL, getFromCursor(cursor, DatabaseHelper.COLUMN_IMAGE_URL));
            values.put(DatabaseHelper.COLUMN_HISTORY, getFromCursor(cursor, DatabaseHelper.COLUMN_HISTORY));
            values.put(DatabaseHelper.COLUMN_WIKIPEDIA, getFromCursor(cursor, DatabaseHelper.COLUMN_WIKIPEDIA));

            result.add(values);
        }
        return result;
    }

    private Liquor contentValuesToLiquor(ContentValues contentValues) {
        String name = contentValues.getAsString(DatabaseHelper.COLUMN_NAME);
        String imageUrl = contentValues.getAsString(DatabaseHelper.COLUMN_IMAGE_URL);
        String history = contentValues.getAsString(DatabaseHelper.COLUMN_HISTORY);
        String wikipedia = contentValues.getAsString(DatabaseHelper.COLUMN_WIKIPEDIA);
        //TODO fetch other names
        List<String> otherNames = new ArrayList<>();
        return Liquor.create(name, imageUrl, history, wikipedia, otherNames);
    }
}
