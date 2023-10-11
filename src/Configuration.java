import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

public class Configuration {
    public static final String TOKEN = readToken();
    public static final String BASE_URL="https://web-api.tp.entsoe.eu/api";

    static String readTokenFromFile() {
        try {
            // read input file
            File file = new File("../token.txt");
            Scanner scanner = new Scanner(file);
            String token = scanner.nextLine();
            scanner.close();
            System.out.println("Reading TOKEN from file.");
            return token;
        // if file couldnt be read print out error mgs
        } catch (FileNotFoundException e) {
            System.out.println("File could not be read.");
            e.printStackTrace();
            return null;
        }
    }
    
    static String readToken() {
        // Read the value of the environmental variable
        Map<String, String> env = System.getenv();
        String token = env.get("TOKEN");
        
        // Check if the environmental variable is defined
        if (token.equals("insert_token") || token == null) {
            System.out.println("Environmental variable TOKEN is not defined in enviromental variables.");
            
            return readTokenFromFile();
        }
        
        System.out.println("TOKEN read from environment variable.");
        return token;
    }
}

