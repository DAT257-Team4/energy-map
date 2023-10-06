import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Configuration {
    public static final String TOKEN=readToken();
    public static final String BASE_URL="https://web-api.tp.entsoe.eu/api";

    static String readToken() {
        try {
            // read input file
            File file = new File("../token.txt");
            Scanner scanner = new Scanner(file);
            String token = scanner.nextLine();
            scanner.close();
            return token;
        // if file couldnt be read print out error mgs
        } catch (FileNotFoundException e) {
            System.out.println("File could not be read.");
            e.printStackTrace();
            return null;
        }
    }
}

