package com.example.androidexpensify;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidexpensify.RoomPersistence.AppDatabase;
import com.example.androidexpensify.RoomPersistence.ExpenseEntity;
import com.example.androidexpensify.Sqlite.DatabaseManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Button buttonAddExpense;
    ImageButton calendarBtn;
    SwitchCompat switchExpensePaid;
    Spinner spinnerExpenseCategory;
    EditText editTextExpenseAmount, editTextDate, editTextExpenseName,editTextExpenseAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // using sqlite
        DatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.open();

        // using room persistence
        AppDatabase appDatabase = AppDatabase.getDatabase(getApplicationContext());

        editTextDate = findViewById(R.id.editTextDate);
        editTextExpenseName = findViewById(R.id.editTextExpenseName);
        editTextExpenseAddress = findViewById(R.id.editTextExpenseAddress);
        editTextExpenseAmount = findViewById(R.id.editTextExpenseAmount);
        spinnerExpenseCategory = findViewById(R.id.spinnerExpenseCategory);
        switchExpensePaid = findViewById(R.id.switchExpensePaid);
        calendarBtn = findViewById(R.id.btnCalendar);
        buttonAddExpense = findViewById(R.id.buttonAddExpense);

        editTextDate.setEnabled(false);
        calendarBtn.setOnClickListener(v -> showDatePickerDialog());
        buttonAddExpense.setOnClickListener(v -> createExpenseUsingRoomPersistence(appDatabase));
    }

    private void showDatePickerDialog() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog and set listener
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                (view, _year, _month, _dayOfMonth) -> {
                    // Set selected date to EditText
                    String selectedDate = _dayOfMonth + "/" + (_month + 1) + "/" + _year;
                    editTextDate.setText(selectedDate);
                }, year, month, dayOfMonth);

        // Show DatePickerDialog
        datePickerDialog.show();
    }

    private void clearInputData() {
        editTextDate.setText("");
        editTextExpenseName.setText("");
        editTextExpenseAddress.setText("");
        editTextExpenseAmount.setText("");
        switchExpensePaid.setChecked(false);
    }

    // Using Local Storage ---------------------------------------------
    private void saveExpenseDataToFile() {
        String date = editTextDate.getText().toString();
        String expenseName = editTextExpenseName.getText().toString();
        String expenseAddress = editTextExpenseAddress.getText().toString();
        String expenseAmount = editTextExpenseAmount.getText().toString();
        String expenseCategory = spinnerExpenseCategory.getSelectedItem().toString();
        boolean expensePaid = switchExpensePaid.isChecked();

        // Concatenate all information into one string
        String expenseInfo = date + "," + expenseName + "," + expenseAddress + "," +
                expenseAmount + "," + expenseCategory + "," + expensePaid + "\n";

        FileOutputStream fos = null;
        try {
            // Open or create the file for appending
            fos = openFileOutput("expenses.txt", Context.MODE_APPEND);

            if(expenseName.isEmpty() || expenseAddress.isEmpty() || expenseAmount.isEmpty() || date.isEmpty()){
                Toast.makeText(this, "This fields are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            fos.write(expenseInfo.getBytes());

            Toast.makeText(this, "Expense saved successfully", Toast.LENGTH_SHORT).show();

            clearInputData();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving expense", Toast.LENGTH_SHORT).show();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Using Sqlite -----------------------------------------------------------------------
    private void saveExpenseToSqlite(DatabaseManager databaseManager){
        String date = editTextDate.getText().toString();
        String expenseName = editTextExpenseName.getText().toString();
        String expenseAddress = editTextExpenseAddress.getText().toString();
        int expenseAmount = Integer.parseInt(editTextExpenseAmount.getText().toString());
        String expenseCategory = spinnerExpenseCategory.getSelectedItem().toString();
        String expensePaid = String.valueOf(switchExpensePaid.isChecked());

        databaseManager.addData(expenseName, expenseCategory, expenseAddress, date, expensePaid, expenseAmount);
        clearInputData();
    }

    // Using Room Persistence ----------------------------------------------
    private void createExpenseUsingRoomPersistence(AppDatabase appDatabase){
        String date = editTextDate.getText().toString();
        String expenseName = editTextExpenseName.getText().toString();
        String expenseAddress = editTextExpenseAddress.getText().toString();
        int expenseAmount = Integer.parseInt(editTextExpenseAmount.getText().toString());
        String expenseCategory = spinnerExpenseCategory.getSelectedItem().toString();
        String expensePaid = String.valueOf(switchExpensePaid.isChecked());

        new Thread(() -> {
            ExpenseEntity expense = new ExpenseEntity();
            expense.name = expenseName;
            expense.address = expenseAddress;
            expense.amount = expenseAmount;
            expense.date = date;
            expense.category = expenseCategory;
            expense.isPaid = expensePaid;

            appDatabase.expenseDao().insert(expense);
            runOnUiThread(()-> clearInputData());
        }).start();
    }
}