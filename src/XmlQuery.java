import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.Iterator;

public class XmlQuery {

    /**
     * Takes the XML received from the API and extracts the energy type and corresponding quantity into a nodelist
     * @param xml The XML received as an InputStream from the API
     * @return A list of nodes containing energy types and quantities
     * @throws IOException
     */
    public static NodeList QueryXMLForEnergyValues(String xml) throws IOException {

        //xpath is used for querying the XML
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        InputSource source = new InputSource(new StringReader(xml.toString()));
        String namespace;

        //Retrieves the namespace
        try {
            namespace = xpath.evaluate("namespace-uri(/*)", source);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }

        //Binds the prefix u to the namespace of the XML document. If not present xpath.evaluate will not work
        xpath.setNamespaceContext(new NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                if ("u".equals(prefix)) {
                    return namespace;
                }
                return null;
            }

            @Override
            public String getPrefix(String namespaceURI) {
                return null;
            }

            @Override
            public Iterator<String> getPrefixes(String namespaceURI) {
                return null;
            }
        });

        //Re-opens the InputSource
        source = new InputSource(new StringReader(xml.toString()));

        NodeList nodes;
        try {
            nodes = (NodeList) xpath.evaluate("//u:psrType | //u:quantity", source, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }

        return nodes;
    }

    /**
     * Converts the contents of an InputStream into a Java-readable StringBuilder
     * @param in InputStream received from HTMLConnection
     * @return StringBuilder object with the contents of the InputStream
     * @throws IOException
     */
    public static String inputStreamToString(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
            out.append(newLine);
        }
        in.close();

        return out.toString();
    }
}
