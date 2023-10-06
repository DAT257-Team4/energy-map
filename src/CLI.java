import java.util.List;
import java.util.Scanner;

public class CLI {


    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Do you wish to update the database?");
        System.out.println("Y/N");
        String answer = input.nextLine().toLowerCase().trim();

        if (answer.equals("y"))
            DBupdate.updateValues();

        String country = null;
        String energyType;

        System.out.println("Do you wish to query the database?");
        System.out.println("Y/N");

        answer = input.nextLine().toLowerCase().trim();
        if (answer.equals("y")) {
            while (country == null) {
                System.out.println("Type a country name");
                country = input.nextLine().toLowerCase().trim();
            }

            System.out.println("If you wish specify an energy type, else press enter");
            energyType = input.nextLine().toLowerCase().trim();

            List<String> results;
            int result;
            if (!energyType.isEmpty()) {
                result = JDBCQuery.SqlQuery(country, energyType, DBupdate.dbURL);
                System.out.println(energyType + " production in country " + country + " equals " + result);
            } else {
                results = JDBCQuery.SqlQuery(country, DBupdate.dbURL);
                for (String s : results) {
                    System.out.println(s);
                }
            }
        }
    }
}
