package com.example.pursuit.database.models;

public class Company {
  public static final String TABLE_NAME = "Companies";
  public static final String COMPANY_ID = "id integer primary key";
  public static final String COMPANY_PSWD = "Password text";
  public static final String COMPANY_NAME = "CompanyName text";
  public static final String COMPANY_FIELD = "Field text";
  public static final String COMPANY_EMAIL = "Email text";

  public static final String NAME = "CompanyName";
  public static final String EMAIL = "Email";
  public static final String PASSWORD = "Password";
  public static final String FIELD = "Field";

  public static final String CREATE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COMPANY_ID + ", " + COMPANY_NAME + ", " + COMPANY_EMAIL + ", " + COMPANY_PSWD + ", " + COMPANY_FIELD +  ");";

  private int id;
  private String name;
  private String email;
  private String password;
  private String field;

  public Company() {

  }

  public Company(int id, String name, String email, String password, String field) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.field = field;
  }
};