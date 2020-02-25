package com.example.pursuit.database.models;

public class Student {
    public static final String APPLICANT_TABLE = "Applicants";
    public static final String APPLICANT_COL0 = "id INTEGER PRIMARY KEY";
    public static final String APPLICANT_COL1 = "firstname VARCHAR";
    public static final String APPLICANT_COL2 = "lastname VARCHAR";
    public static final String APPLICANT_COL3 = "university VARCHAR";
    public static final String APPLICANT_COL4 = "major VARCHAR";
    public static final String APPLICANT_COL5 = "minor VARCHAR";
    public static final String APPLICANT_COL6 = "gpa VARCHAR";
    public static final String APPLICANT_COL7 = "bio VARCHAR";
    public static final String APPLICANT_COL8 = "email VARCHAR";
    public static final String APPLICANT_COL9 = "username VARCHAR";
    public static final String APPLICANT_COL10 = "password VARCHAR";

    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "lastname";
    public static final String UNIVERSITY = "university";
    public static final String MAJOR = "major";
    public static final String MINOR = "minor";
    public static final String GPA = "gpa";
    public static final String BIO = "bio";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    private int id;
    private String fname;
    private String lname;
    private String university;
    private String major;
    private String minor;
    private String gpa;
    private String bio;
    private String email;
    private String username;
    private String password;

    public static final String CREATE_QUERY = "CREATE TABLE IF NOT EXISTS " + APPLICANT_TABLE +
            " (" + APPLICANT_COL0 + ", " + APPLICANT_COL1 + ", " +
            APPLICANT_COL2 + ", " + APPLICANT_COL3 + ", " + APPLICANT_COL4 + ", " +
            APPLICANT_COL5 + ", " + APPLICANT_COL6 + ", " + APPLICANT_COL7 + ", " +
            APPLICANT_COL8 + ", " + APPLICANT_COL9 + ", " + APPLICANT_COL10 + ");";

    public Student() {

    }

    public Student(int id, String firstname, String lastname, String university, String major,
                   String minor, String gpa, String bio, String email, String username, String password) {
        this.id = id;
        this.fname = firstname;
        this.lname = lastname;
        this.university = university;
        this.major = major;
        this.minor = minor;
        this.gpa = gpa;
        this.bio = bio;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getUniversity() {
        return university;
    }

    public String getMajor() {
        return major;
    }

    public String getMinor() {
        return minor;
    }

    public String getGpa() {
        return gpa;
    }

    public String getBio() {
        return bio;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
