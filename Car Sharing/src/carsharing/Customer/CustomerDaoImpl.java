package carsharing.Customer;

import carsharing.Company.Company;
import carsharing.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CustomerDaoImpl {

    ArrayList<Customer> customers = new ArrayList<>();

    private static final Connection connection = Database.getConnection();
    private static final String listAllQuery = "SELECT * FROM CUSTOMER ORDER BY ID ASC";
    private static final String addNewRecord = "INSERT INTO CUSTOMER (NAME,RENTED_CAR_ID) VALUES(?,NULL)";
    private static final String updateRecord = "UPDATE CUSTOMER SET RENTED_CAR_ID = (SELECT ID FROM CAR WHERE COMPANY_ID = ?) WHERE ID = ?";

    public static ArrayList<Customer> listAll() {
        ArrayList<Customer> result = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(listAllQuery)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int ID = resultSet.getInt("ID");
                String NAME = resultSet.getString("NAME");
                result.add(new Customer(ID,NAME));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static void addNewRecord(String NAME) {
        try (PreparedStatement statement = connection.prepareStatement(addNewRecord)) {

            statement.setString(1, NAME);
            statement.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void updateRecord(int CUSTOMER_ID, int COMPANY_ID) {
        try (PreparedStatement statement = connection.prepareStatement(addNewRecord)) {

            statement.setString(1, String.valueOf(COMPANY_ID));
            statement.setString(2, String.valueOf(CUSTOMER_ID));
            statement.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
