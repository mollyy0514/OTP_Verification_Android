package com.example.servera;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 5;
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IDENTITY = "identity";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_TIME = "time";

    String phoneNumber = null;

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_IDENTITY + " TEXT NOT NULL, " +
                    COLUMN_GENDER + " TEXT NOT NULL, " +
                    COLUMN_PHONE_NUMBER + " TEXT NOT NULL, " +
                    COLUMN_TIME + " TEXT NOT NULL" +
                    ");";

    // Initial data for pre-population
    private static final String[] INITIAL_NAMES = {"Serene Vitale", "Hyungwon Chae", "Minhyuk Lee", "Rachel Green", "Chandler Bing", "Luke Dunphy", "Haley Dunphy", "Sheldon Cooper", "Jacob Peralta", "Amy Santiago"};
    private static final String[] INITIAL_IDENTITY = {"A234567890", "A110330801", "M108172077", "L238693204", "F130919616", "G119937740", "G294067294", "K194749470", "E191278675", "D283785742"};
    private  static final String[] INITIAL_GENDER = {"female", "male", "male", "female", "male", "male", "female", "male", "male", "female",};
    private static final String[] INITIAL_PHONE_NUMBERS = {"+1 555-123-4567", "0940105514", "093112215", "0979437918", "0995168810", "0969820060", "0981139379", "0998124315", "0941085016", "0943038115"};
    private static final String[] INITIAL_TIME = {"", "", "", "", "", "", "", "", "", ""};

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        // Pre-populate the database with initial data
        for (int i = 0; i < INITIAL_NAMES.length; i++) {
            insertUser(db, INITIAL_NAMES[i], INITIAL_IDENTITY[i], INITIAL_GENDER[i], INITIAL_PHONE_NUMBERS[i], INITIAL_TIME[i]);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void insertUser(SQLiteDatabase db, String name, String identity, String gender, String phoneNumber, String time) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_IDENTITY, identity);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);
        values.put(COLUMN_TIME, time);

        db.insert(TABLE_NAME, null, values);
    }

    public String getPhoneNumber(String name, String identity) {
        SQLiteDatabase db = this.getReadableDatabase();


        // Define the raw SQL query
        String query = "SELECT " + COLUMN_PHONE_NUMBER +
                " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_NAME + " = '"+ name +"' AND " + COLUMN_IDENTITY + " = '"+ identity +"'";

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

    public void updatePatientTime(String name, String time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues newTime = new ContentValues();
        newTime.put(COLUMN_TIME, time);

        // Specify the WHERE clause to identify the user by name
        String whichPatient = COLUMN_NAME + " = '" + name + "'";

        // Update the row(s) that match the WHERE clause
        db.update(TABLE_NAME, newTime, whichPatient, null);

        // Close the database
        db.close();
    }

}
