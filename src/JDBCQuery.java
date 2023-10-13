import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCQuery {

    /**
     * Queries the database and returns all values associated with a country name
     * @param country the coutnry to be queried
     * @param url the path to the database
     * @return an array list with all energy and quantity values associated with the country
     */
    public static List<String> SqlQuery(String country, String url,String table) {
        Connection connection = null;
        List<String> results = new ArrayList<>();
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Connect to the SQLite database
            connection = DriverManager.getConnection(url);

            if (connection != null) {
                
                // Create a statement object
                Statement statement = connection.createStatement();

                String queryTable = "SELECT energyType, quantity FROM "+table+" WHERE country = '" + country + "'";

                ResultSet rs = statement.executeQuery(queryTable);

                while (rs.next()) {
                    String type = rs.getString("energyType");
                    String quantity = rs.getString("quantity");

                    results.add(type);
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

    /**
     * Queries the database for the quantity of a single energytype associated with the country name
     * @param country country to be queried
     * @param energyType energy type to be queried
     * @param url the location of the database
     * @return the quantity of a single energytype produced by the country
     */
    public static int SqlQuery(String country, String energyType, String url,String table) {
        Connection connection = null;
        int quantity = -1;
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Connect to the SQLite database
            connection = DriverManager.getConnection(url);

            if (connection != null) {
                
                // Create a statement object
                Statement statement = connection.createStatement();

                String queryTable = "SELECT quantity FROM EnergyProduction "+table+" WHERE country = '" + country + "' " +
                        "AND energyType = '" + energyType + "'";

                ResultSet rs = statement.executeQuery(queryTable);

                quantity = rs.getInt("quantity");

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
        return quantity;
    }
}