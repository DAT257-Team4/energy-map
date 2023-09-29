import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultMultipleSources {
    public String country;
    public int[] values;
    public final static String[] ENERGY_SOURCES={"Mixed", "Generation", "Load", "Biomass", "Fossil Brown coal/Lignite",
            "Fossil Coal-derived gas", "Fossil Gas", "Fossil Hard coal", "Fossil Oil", "Fossil Oil shale", "Fossil Peat",
            "Geothermal", "Hydro Pumped Storage", "Hydro Run-of-river and poundage", "Hydro Water Reservoir", "Marine",
            "Nuclear", "Other renewable", "Solar", "Waste", "Wind Offshore", "Wind Onshore", "Other", "AC Link",
            "DC Link", "Substation", "Transformer"};


    public ResultMultipleSources(String country) throws IOException {
        values=new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        this.country=country;
        updateValues(country);
    }

    public void updateValues(String country) {
        List<String> results = JDBCQuery.SqlQuery(country);

        for (int i = 0; i < results.size(); i+=2) {
            int position;
            int quantity;

            position = POSITION_MAP.get(CodeFormats.ENERGY_MAP.get(results.get(i)));
            quantity = Integer.parseInt(results.get(i+1));
            values[position] = quantity;
        }

    }

    //@TODO Make a more elegant solution
    final private Map<String, Integer> POSITION_MAP = new HashMap<>(){{
        put("A03", 0);
        put("A04", 1);
        put("A05", 2);
        put("B01", 3);
        put("B02", 4);
        put("B03", 5);
        put("B04", 6);
        put("B05", 7);
        put("B06", 8);
        put("B07", 9);
        put("B08", 10);
        put("B09", 11);
        put("B10", 12);
        put("B11", 13);
        put("B12", 14);
        put("B13", 15);
        put("B14", 16);
        put("B15", 17);
        put("B16", 18);
        put("B17", 19);
        put("B18", 20);
        put("B19", 21);
        put("B20", 22);
        put("B21", 23);
        put("B22", 24);
        put("B23", 25);
        put("B24", 26);
    }};
}
