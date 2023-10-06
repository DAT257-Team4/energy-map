import org.junit.Test;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

public class XMLQueryTest {

    /**
     * Because Germany is a large nation it should generally always return at least one energy type
     * @throws IOException
     */
    @Test
    public void XMLQueryValidInputTest() throws IOException {
        InputStream is = GetAPIData.sendAPIRequest(ApiRequest.ApiReqForEnergySource("all", "germany"));
        NodeList nl = XmlQuery.QueryXMLForEnergyValues(is);

        assert nl.getLength() > 0;
    }


    /**
     * Invalid inputs should return an empty nodeList
     * @throws IOException
     */
    @Test
    public void XMLQueryInvalidInputTest() throws IOException {
        InputStream is = GetAPIData.sendAPIRequest(ApiRequest.ApiReqForEnergySource("invalid", "invalid"));
        NodeList nl = XmlQuery.QueryXMLForEnergyValues(is);

        assert nl.getLength() == 0;
    }
}
