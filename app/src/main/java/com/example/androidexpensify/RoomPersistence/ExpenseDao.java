package com.example.androidexpensify.RoomPersistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert
    void insert(ExpenseEntity expenseEntity);

    @Insert
    void insertAll(List<ExpenseEntity> expenseEntities);

    @Query("SELECT * FROM ExpenseEntity WHERE id = :id")
    ExpenseEntity getById(int id);

    @Update
    void update(ExpenseEntity expenseEntity);

    @Delete
    void delete(ExpenseEntity expenseEntity);
}
