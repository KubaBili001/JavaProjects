package carsharing.Company;

import carsharing.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CompanyDaoImpl {

    private static final Connection connection = Database.getConnection();
    private static final String listAllQuery = "SELECT * FROM COMPANY ORDER BY ID ASC";
    private static final String addNewRecord = "INSERT INTO COMPANY (NAME) VALUES(?)";

    public static ArrayList<Company> listAll() {
        ArrayList<Company> result = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(listAllQuery)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int ID = resultSet.getInt("ID");
                String NAME = resultSet.getString("NAME");
                result.add(new Company(ID,NAME));
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
}
