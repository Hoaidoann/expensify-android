package com.example.androidexpensify.Sqlite;

public class ExpenseModel {
    public int id;
    public String name, date, address, category, isPaid;
    public int amount;

    public ExpenseModel(int id, String name, String date, String address, String category, String isPaid, int amount) {

        this.id = id;
        this.name = name;
        this.date = date;
        this.category = category;
        this.address = address;
        this.isPaid = isPaid;
        this.amount = amount;
    }
}
