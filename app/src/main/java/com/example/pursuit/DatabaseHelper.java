package com.example.pursuit;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final String DB_NAME = "pursuitDB";
    private final String USER_TABLE = "Users";

    @Override
    protected SQLiteDatabase onCreate(Bundle savedInstanceState) {
        
    }
}
