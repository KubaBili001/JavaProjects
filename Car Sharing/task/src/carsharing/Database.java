package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {

    public static final String filePath = "jdbc:h2:./src/carsharing/db/";
    public static final String driver = "org.h2.Driver";
    public static String dbName;


    private static final String dropCustomer = "DROP TABLE IF EXISTS CUSTOMER";
    private static final String dropCompany  = "DROP TABLE IF EXISTS COMPANY";
    private static final String dropCar      = "DROP TABLE IF EXISTS CAR";


    private static final String initCustomer = "CREATE TABLE CUSTOMER (ID INT AUTO_INCREMENT, NAME VARCHAR(255) UNIQUE NOT NULL, RENTED_CAR_ID INT, " +
            "PRIMARY KEY(ID), FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID))";
    private static final String initCompany  = "CREATE TABLE COMPANY (ID INT AUTO_INCREMENT, NAME VARCHAR(255) UNIQUE NOT NULL, PRIMARY KEY(ID))";
    private static final String initCar      = "CREATE TABLE CAR (ID INT AUTO_INCREMENT, NAME VARCHAR(255) UNIQUE NOT NULL," +
            " COMPANY_ID INT NOT NULL , PRIMARY KEY(ID), FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID))";

    public static void initDatabase() {

        try {

            Class.forName(driver);
            Connection connection = DriverManager.getConnection(filePath + dbName);
            connection.setAutoCommit(true);

            Statement statement = connection.createStatement();

            statement.execute(dropCustomer);
            statement.execute(dropCar);
            statement.execute(dropCompany);

            statement.execute(initCompany);
            statement.execute(initCar);
            statement.execute(initCustomer);

            statement.close();
            connection.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(filePath + dbName);
            connection.setAutoCommit(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return connection;
    }

}
