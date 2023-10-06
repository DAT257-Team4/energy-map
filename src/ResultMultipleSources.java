import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultMultipleSources {
    public String country;
    public int[] values;

    /**
     * Holds all energy production values for a single country. Updates its values automatically when created
     * @param country The name of the country
     * @throws IOException
     */
    public ResultMultipleSources(String country) throws IOException {
        values=new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        this.country=country;
        updateValues(country);
    }

    /**
     * Queries the SQL database and updates the values held by the ResultMultipleSources object
     * @param country
     */
    public void updateValues(String country) {
        List<String> results = JDBCQuery.SqlQuery(country);

        for (int i = 0; i < results.size(); i+=2) {
            int position;
            int quantity;

            position = CodeFormats.POSITION_MAP.get(CodeFormats.ENERGY_MAP.get(results.get(i)));
            quantity = Integer.parseInt(results.get(i+1));
            values[position] = quantity;
        }

    }
}
