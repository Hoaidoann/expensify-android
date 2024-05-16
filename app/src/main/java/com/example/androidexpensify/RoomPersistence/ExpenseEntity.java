package com.example.androidexpensify.RoomPersistence;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ExpenseEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "isPaid")
    public String isPaid;

    @ColumnInfo(name = "amount")
    public int amount;
}
