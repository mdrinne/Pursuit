public class Company {
  public static final String TABLE_NAME = "Companies";
  public final String COMPANY_ID = "id integer primary key";
  public final String COMPANY_PSWD = "Password VARCHAR";
  public final String COMPANY_NAME = "CompanyName VARCHAR";
  public final String COMPANY_FIELD = "Field VARCHAR";
  public final String COMPANY_EMAIL = "Email VARCHAR";

  public Company() {

  }

  public Company(int id, String name, String email, String password, String field) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.field = field;
  }
}