import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JDBCQuery {

    public static List<String> SqlQuery(String country) {
        Connection connection = null;
        List<String> results = new ArrayList<>();
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Connect to the SQLite database
            String url = "jdbc:sqlite:db/energy-production-db";
            connection = DriverManager.getConnection(url);

            if (connection != null) {
                System.out.println("Connected to the database!");

                // Create a statement object
                Statement statement = connection.createStatement();

                String queryTable = "SELECT energyType, quantity FROM EnergyProduction WHERE country = '" + country + "'";

                ResultSet rs = statement.executeQuery(queryTable);

                while (rs.next()) {
                    String energyType = rs.getString("energyType");
                    String quantity = rs.getString("quantity");

                    results.add(energyType);
                    results.add(quantity);
                }

                // Close the statement
                statement.close();

                // Close the connection when done
                connection.close();

            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection to the database failed.");
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}