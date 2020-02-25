package com.example.pursuit.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    // `id` and `timestamp` will be inserted automatically.
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
