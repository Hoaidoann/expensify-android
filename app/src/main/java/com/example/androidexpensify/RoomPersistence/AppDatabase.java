package com.example.androidexpensify.RoomPersistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ExpenseEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase db;

    public abstract ExpenseDao expenseDao();

    public static AppDatabase getDatabase(final Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "expense_database").build();
        }
        return db;
    }
}