package com.example.servera;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 4;
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";

    String phoneNumber = null;

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_PHONE_NUMBER + " TEXT NOT NULL" +
                    ");";

    // Initial data for pre-population
    private static final String[] INITIAL_NAMES = {"123", "name2", "name3"};
    private static final String[] INITIAL_PASSWORDS = {"12345", "password2", "password3"};
    private static final String[] INITIAL_PHONE_NUMBERS = {"+1 555-123-4567", "9876543210", "5555555555"};

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        // Pre-populate the database with initial data
        for (int i = 0; i < INITIAL_NAMES.length; i++) {
            insertUser(db, INITIAL_NAMES[i], INITIAL_PASSWORDS[i], INITIAL_PHONE_NUMBERS[i]);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void insertUser(SQLiteDatabase db, String name, String password, String phoneNumber) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);

        db.insert(TABLE_NAME, null, values);
    }

    public String getPhoneNumber(String name, String password) {
        SQLiteDatabase db = this.getReadableDatabase();


        // Define the raw SQL query
        String query = "SELECT " + COLUMN_PHONE_NUMBER +
                " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_NAME + " = '"+ name +"' AND " + COLUMN_PASSWORD + " = '"+ password +"'";

        // Execute the raw query with selection arguments
        Cursor cursor = db.rawQuery(query, null);

        // Check if the cursor has data
        if (cursor != null && cursor.moveToFirst()) {
            // Check if the column exists in the result set
            int phoneNumberColumnIndex = cursor.getColumnIndex(COLUMN_PHONE_NUMBER);
            if (phoneNumberColumnIndex != -1) {
                // Retrieve the phone number from the cursor
                phoneNumber = cursor.getString(phoneNumberColumnIndex);
            }
            cursor.close();
        }

        // Close the database
        db.close();

        return phoneNumber;
    }

}
