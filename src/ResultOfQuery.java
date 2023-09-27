import java.io.IOException;
import java.io.InputStream;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class ResultOfQuery {
    public String country;
    public String source;
    public int value;

    public ResultOfQuery(String country,String energyType){
        ApiRequest params = ApiRequest.ApiReqForEnergySource(energyType, country);
        InputStream xml = GetAPIData.sendAPIRequest(params);

        
        Element energyValue;
        try {
            NodeList l = XmlQuery.QueryXMLForEnergyValues(xml);
            energyValue = (Element) l.item(1);
            String ev=energyValue.getTextContent();
            value=Integer.parseInt(ev);
        } catch (Exception e) {
            value=0;
        }

        this.country=country;
        source=energyType;
    }
}
