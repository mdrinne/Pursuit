public class Company {
  public static final String TABLE_NAME = "Companies";
  public static final String COMPANY_ID = "id integer primary key";
  public static final String COMPANY_PSWD = "Password VARCHAR";
  public static final String COMPANY_NAME = "CompanyName VARCHAR";
  public static final String COMPANY_FIELD = "Field VARCHAR";
  public static final String COMPANY_EMAIL = "Email VARCHAR";

  public static final String CREATE_QUERY = "CREATE TABLE IF NOT EXISTS " + COMPANY_TABLE + " (" + COMPANY_ID + ", " + COMPANY_PSWD + ", " + COMPANY_NAME + ", " + COMPANY_FIELD + ", " + COMPANY_EMAIL +  ");"

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