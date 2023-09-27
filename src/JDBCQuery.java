import java.sql.*;

public class JDBCQuery {
    public static void main(String[] args) {
        Connection connection = null;
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

                // SQL query to create a table
                String createTable = "CREATE TABLE IF NOT EXISTS EnergyProduction (" +
                        "country TEXT NOT NULL," +
                        "energyType TEXT NOT NULL," +
                        "quantity INT," +
                        "PRIMARY KEY (country, energyType)" +
                        ")";

                // Execute the SQL query
                statement.execute(createTable);

                String queryTable = "SELECT energyType, quantity FROM EnergyProduction WHERE country = 'Italy'";

                ResultSet rs = statement.executeQuery(queryTable);

                System.out.println(rs.getString(1));
                System.out.println(rs.getString(2));
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
    }
}