import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DBupdate {

    public static String dbURL = "jdbc:sqlite:db/energy-production-db";

    /**
     * Updates energy production values in the database for specified countries and energy types.
     */
    public static void updateValues(){

        Connection conn;
        String [] countries =  CodeFormats.COUNTRY_LIST;
        
        try {
            conn=DriverManager.getConnection(dbURL);
            Statement statement = conn.createStatement();

            initDatabase(dbURL);

            for (String country : countries) {
                ApiRequest req= ApiRequest.ApiReqForEnergySource("all",country);
                ApiRequest req2= ApiRequest.ApiReqForInstalledEnergy("all",country);
                
                
                try{
                    
                    NodeList l2=XmlQuery.QueryXMLForEnergyValues(XmlQuery.inputStreamToString(GetAPIData.sendAPIRequest(req2)));
                    int i=0;

                    deletePreviousDataInstalled(country, statement);
                    while(i<l2.getLength()){
                        Element energyCode = (Element) l2.item(i);
                        i++;
                        int totValue = 0;
                        while(i<l2.getLength() && ((Element) l2.item(i)).getTagName().equals("quantity")){
                            totValue+= Integer.parseInt((l2.item(i)).getTextContent());
                            i++;
                        }
                        insertValueToDBInstalled(country, CodeFormats.REVERSE_ENERGY_MAP.get(energyCode.getTextContent()),totValue,statement);
                    
                    }
                        
                        
                        
                    NodeList listRes=XmlQuery.QueryXMLForEnergyValues(XmlQuery.inputStreamToString(GetAPIData.sendAPIRequest(req)));
                    
                    i=0;
                    deletePreviousData(country, statement);
                    while(i<listRes.getLength()){
                        Element energyCode = (Element) listRes.item(i);
                        i++;
                        int totValue = 0;
                        while(i<listRes.getLength() && ((Element) listRes.item(i)).getTagName().equals("quantity")){
                            totValue+= Integer.parseInt((listRes.item(i)).getTextContent());
                            i++;
                        }
                        insertValueToDB(country, CodeFormats.REVERSE_ENERGY_MAP.get(energyCode.getTextContent()),totValue,statement);
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
                
            }
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * If the database file is empty this will create the appropriate tables
     * @param url contains the file path to the database and the SQLite driver
     */
    private static void initDatabase(String url) {
        try {
            Connection conn=DriverManager.getConnection(url);
            Statement statement = conn.createStatement();

            // SQL query to create a table
            String createTable = "CREATE TABLE IF NOT EXISTS EnergyProduction (" +
                    "country TEXT NOT NULL," +
                    "energyType TEXT NOT NULL," +
                    "quantity INT," +
                    "PRIMARY KEY (country, energyType)" +
                    ")";

            // Execute the SQL query
            statement.execute(createTable);

            String createTable2 = "CREATE TABLE IF NOT EXISTS EnergyInstalled (" +
            "country TEXT NOT NULL," +
            "energyType TEXT NOT NULL," +
            "quantity INT," +
            "PRIMARY KEY (country, energyType)" +
            ")";

            // Execute the SQL query
            statement.execute(createTable2);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method take a country and a statement connection to database and remove all the values for that county
     * @param country the country
     * @param statement the statement connection to the DB
     * @throws SQLException
     */
    private static void deletePreviousData(String country, Statement statement) throws SQLException {
        String q = "DELETE FROM `EnergyProduction`  WHERE Country='"+country+"'";
        statement.executeUpdate(q);
    }

    /**
     * This function execute the query to add the values to the DB
     * @param country   The country
     * @param energySource  The energy source name
     * @param value     The energy production value.
     * @param statement The statament connection to the DB
     * @throws SQLException If a database access error occurs
     */
    private static void insertValueToDB(String country, String energySource, int value, Statement statement) throws SQLException{
        String q="REPLACE INTO `EnergyProduction`  VALUES ('"+country+"','"+energySource+"', "+value+")";        
        statement.executeUpdate(q);
    }


    private static void deletePreviousDataInstalled(String country, Statement statement) throws SQLException {
        String q = "DELETE FROM `EnergyInstalled`  WHERE Country='"+country+"'";
        statement.executeUpdate(q);
    }

    /**
     * This function execute the query to add the values to the DB
     * @param country   The country
     * @param energySource  The energy source name
     * @param value     The energy production value.
     * @param statement The statament connection to the DB
     * @throws SQLException If a database access error occurs
     */
    private static void insertValueToDBInstalled(String country, String energySource, int value, Statement statement) throws SQLException{
        String q="REPLACE INTO `EnergyInstalled`  VALUES ('"+country+"','"+energySource+"', "+value+")";        
        statement.executeUpdate(q);
    }
    
}
