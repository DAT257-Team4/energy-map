import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DBupdate {

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
                    
                    while(i<listRes.getLength()){
                        Element energyCode = (Element) listRes.item(i);
                        i++;
                        int totValue = 0;
                        while(i<listRes.getLength() && ((Element) listRes.item(i)).getTagName().equals("quantity")){
                            totValue+= Integer.parseInt(((Element) listRes.item(i)).getTextContent());
                            i++;
                        }
                        DBUpdate(country, CodeFormats.REVERSE_ENERGY_MAP.get(energyCode.getTextContent()),totValue,statement);
                    
                    
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

    public static void DBUpdate(String country, String energySource, int value, Statement statement) throws SQLException{
        String q="REPLACE INTO `EnergyProduction`  VALUES ('"+country+"','"+energySource+"', "+value+")";        
        statement.executeUpdate(q);
    }
    
}
