package com.example.pursuit.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.example.pursuit.database.models.Company;
import com.example.pursuit.database.models.Student;

public class DatabaseHelper extends SQLiteOpenHelper {
  // Database Version
  private static final int DATABASE_VERSION = 1;

  // Database Name
  private static final String DATABASE_NAME = "pursuit.db";

  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {

    // create Tables
    db.execSQL(Company.CREATE_QUERY);
    db.execSQL(Student.CREATE_QUERY);
  }

  // Upgrading database
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop older table if existed
    db.execSQL("DROP TABLE IF EXISTS " + Company.TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + Student.APPLICANT_TABLE);

    // Create tables again
    onCreate(db);
  }

  public long insertCompany(String name, String email, String password, String field) {
    // get writable database as we want to write data
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    // `id` will be inserted automatically.
    // no need to add them
    values.put(Company.NAME, name);
    values.put(Company.EMAIL, email);
    values.put(Company.PASSWORD, password);
    values.put(Company.FIELD, field);


    // insert row
    long id = db.insert(Company.TABLE_NAME, null, values);

    // close db connection
    db.close();

    // return newly inserted row id
    return id;
  }


  public Company getCompanyForLogin(String email, String password) {
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = db.query(Company.TABLE_NAME, null, Company.EMAIL + "=?", new String[]{email}, null, null, null, null);

    if (cursor != null) {
      Log.d(getClass().getSimpleName(), "moving the cursor forward");
      cursor.moveToFirst();
    }

    if (cursor.getCount() == 0) {
      Log.d(getClass().getSimpleName(), "the cursor is null");
      cursor.close();
      return null;
    }

    if (cursor.getString(cursor.getColumnIndex(Company.PASSWORD)).equals(password)) {

      Company company = new Company(
              cursor.getInt(cursor.getColumnIndex(Company.ID)),
              cursor.getString(cursor.getColumnIndex(Company.NAME)),
              cursor.getString(cursor.getColumnIndex(Company.EMAIL)),
              cursor.getString(cursor.getColumnIndex(Company.PASSWORD)),
              cursor.getString(cursor.getColumnIndex(Company.FIELD)));

      cursor.close();

      return company;

    } else {
      cursor.close();
      return null;
    }
  }
}

  public long insertStudent(String fname, String lname, String university, String major, String minor,
                            String gpa, String bio, String email, String username, String password) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();

    values.put(Student.FIRSTNAME, fname);
    values.put(Student.LASTNAME, lname);
    values.put(Student.UNIVERSITY, university);
    values.put(Student.MAJOR, major);
    values.put(Student.MINOR, minor);
    values.put(Student.GPA, gpa);
    values.put(Student.BIO, bio);
    values.put(Student.EMAIL, email);
    values.put(Student.USERNAME, username);
    values.put(Student.PASSWORD, password);

    // insert row
    long id = db.insert(Student.APPLICANT_TABLE, null, values);

    // close db connection
    db.close();

    return id;
  }
}

