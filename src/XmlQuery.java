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

    public static NodeList QueryXMLForEnergyValues(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
            out.append(newLine);
        }
        in.close();

        //xpath is used for querying the XML
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        InputSource source = new InputSource(new StringReader(out.toString()));
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
        source = new InputSource(new StringReader(out.toString()));

        NodeList nodes;
        try {
            nodes = (NodeList) xpath.evaluate("//u:psrType | //u:quantity", source, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }

        return nodes;
    }
}
