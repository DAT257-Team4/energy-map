import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DBupdate {
    /**
     * Updates energy production values in the database for specified countries and energy types.
     */
    public static void updateValues(){

        Connection conn = null; 
        String [] countries =  CodeFormats.COUNTRY_LIST;
        
        try {
            conn=DriverManager.getConnection("jdbc:sqlite:db/energy-production-db");
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

            for (String country : countries) {
                ApiRequest req= ApiRequest.ApiReqForEnergySource("all",country);
                try{
                    NodeList listRes=XmlQuery.QueryXMLForEnergyValues(GetAPIData.sendAPIRequest(req));
                    
                    int i=0;
                    DeletePreviousData(country, statement);
                    while(i<listRes.getLength()){
                        Element energyCode = (Element) listRes.item(i);
                        i++;
                        int totValue = 0;
                        while(i<listRes.getLength() && ((Element) listRes.item(i)).getTagName().equals("quantity")){
                            totValue+= Integer.parseInt(((Element) listRes.item(i)).getTextContent());
                            i++;
                        }
                        InsertValueToDB(country, CodeFormats.REVERSE_ENERGY_MAP.get(energyCode.getTextContent()),totValue,statement);
                    };

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
     * This method take a country and a statement connection to database and remove all the values for that county
     * @param country the country
     * @param statement the statement connection to the DB
     * @throws SQLException
     */
    private static void DeletePreviousData(String country, Statement statement) throws SQLException {
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
    private static void InsertValueToDB(String country, String energySource, int value, Statement statement) throws SQLException{
        String q="REPLACE INTO `EnergyProduction`  VALUES ('"+country+"','"+energySource+"', "+value+")";        
        statement.executeUpdate(q);
    }
    
}
