import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
public class CLI {
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to the energy map!");
        System.out.println("Type 'exit' to exit the program.");
        while (true) {
            System.out.print("Please enter a country name: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String country = reader.readLine().toLowerCase();
            if (country.equals("exit")) {
                break;
            }
            System.out.print("Please enter an energy source: ");
            String energy = reader.readLine().toLowerCase();
            if (energy.equals("exit")) {
                break;
            }
            if (CodeFormats.COUN_MAP.containsKey(country) && CodeFormats.ENERGY_MAP.containsKey(energy)){
                System.out.println("Displaying data for " + energy + " production in " + country + ":");

                ApiRequest params = ApiRequest.ApiReqForEnergySource(energy, country);
                InputStream xml = GetAPIData.sendAPIRequest(params);
                ApiRequest.PrintAsString(xml);
            } else {
                System.out.println("Invalid input");
            }
            
        }
    }
}