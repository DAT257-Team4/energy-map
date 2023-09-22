import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.*;
public class XmlView {

    //Takes XML plain text stored in InputStream object and parses to String, then prints
    public static void PrintAsString(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
            out.append(newLine);
        }
        in.close();
        System.out.println(out);
    }

    public static void PrintXMLQuery (NodeList results) {
        StringBuilder queryText = new StringBuilder();

        for (int i = 0; i<results.getLength(); i += 2) {
            Element energyCode = (Element) results.item(i);
            Element energyValue = (Element) results.item(i+1);
            queryText.append(CodeFormats.REVERSE_ENERGY_MAP.get(energyCode.getTextContent()));
            queryText.append("\n");
            queryText.append(energyValue.getTextContent());
            queryText.append("\n");
        }

        System.out.println(queryText);
    }
}
