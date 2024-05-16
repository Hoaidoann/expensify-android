package com.example.androidexpensify.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Thêm một dòng mới vào cơ sở dữ liệu
    public void addData(String name, String category, String address, String date, String isPaid, int amount) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, name);
        values.put(DBHelper.COLUMN_CATEGORY, category);
        values.put(DBHelper.COLUMN_AMOUNT, amount);
        values.put(DBHelper.COLUMN_IS_PAID, isPaid);
        values.put(DBHelper.COLUMN_ADDRESS, address);
        values.put(DBHelper.COLUMN_DATE, date);

        database.insert(DBHelper.TABLE_NAME, null, values);
    }
}
