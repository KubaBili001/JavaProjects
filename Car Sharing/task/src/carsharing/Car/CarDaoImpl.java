package carsharing.Car;

import carsharing.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CarDaoImpl {

    private static final Connection connection = Database.getConnection();
    private static final String listAllQuery = "SELECT * FROM CAR WHERE COMPANY_ID = ? ORDER BY ID ASC";
    private static final String addNewRecord = "INSERT INTO CAR (NAME,COMPANY_ID) VALUES(?,?)";

    public static ArrayList<Car> listCompanyCars(int COMPANY_ID) {

        ArrayList<Car> result = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(listAllQuery)) {

            statement.setString(1, String.valueOf(COMPANY_ID));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int ID = resultSet.getInt("ID");
                String NAME = resultSet.getString("NAME");

                result.add(new Car(ID,NAME,COMPANY_ID));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static void addNewRecord(String NAME, int COMPANY) {

        try (PreparedStatement statement = connection.prepareStatement(addNewRecord)) {

            statement.setString(1, NAME);
            statement.setString(2, String.valueOf(COMPANY));
            statement.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
