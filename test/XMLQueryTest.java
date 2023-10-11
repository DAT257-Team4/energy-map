import org.junit.Test;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

public class XMLQueryTest {

    public final String exampleXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<GL_MarketDocument xmlns=\"urn:iec62325.351:tc57wg16:451-6:generationloaddocument:3:0\">" +
    "<TimeSeries>" +
        "<MktPSRType>" +
            "<psrType>B16</psrType>" +
        "</MktPSRType>" +
        "<Period>" +
            "<resolution>PT60M</resolution>" +
                "<Point>" +
                    "<position>1</position>" +
                        "<quantity>545</quantity>" +
                "</Point>" +
        "</Period>" +
    "</TimeSeries>" +
"</GL_MarketDocument>";

    /**
     * The nodelist this returns should contain one psrType and one quantity node from the sample above
     */
    @Test
    public void XMLQueryValidInputTest() throws IOException {
        NodeList nl = XmlQuery.QueryXMLForEnergyValues(exampleXML);

        assert nl.getLength() == 2;
    }
}
