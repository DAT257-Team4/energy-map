import java.util.HashMap;
import java.util.Map;
/**
 * A class that contains information about country codes, energy formats, and reverse energy formats.
 */
public class CodeFormats {
    /**
     * List of country codes.
     */
    public final static String []COUNTRY_LIST={"Albania","Armenia","Austria","Azerbaijan","Belarus","Belgium","Bosnia and Herz.","Bulgaria","Croatia","Cyprus","Czech Republic","Denmark","Estonia","Finland","France","Georgia","Germany","Greece","Hungary","Iceland ","Ireland","Italy","Kosovo","Latvia","Lithuania","Luxembourg","Malta ","Moldova","Montenegro","Netherlands","North Macedonia","Norway","Poland","Portugal","Romania","Russia","Serbia","Slovakia","Slovenia","Spain","Sweden","Switzerland","Turkey","Ukraine","United Kingdom"};

    final public static Map<String, String> COUN_MAP = new HashMap<>(){{
        put("albania", "10YAL-KESH-----5");
        put("armenia", "10Y1001A1001B004");
        put("austria", "10YAT-APG------L");
        put("azerbaijan", "10Y1001A1001B05V");
        put("belarus", "BY"         );
        put("belgium", "10YBE----------2");
        put("bosnia and herz.", "10YBA-JPCC-----D");
        put("bulgaria", "10YCA-BULGARIA-R");
        put("croatia", "10YHR-HEP------M");
        put("cyprus", "10YCY-1001A0003J");
        put("czech republic", "10YCZ-CEPS-----N");
        put("denmark", "10Y1001A1001A65H");
        put("estonia", "10Y1001A1001A39I");
        put("finland", "10YFI-1--------U");
        put("france", "10YFR-RTE------C");
        put("georgia", "10Y1001A1001B012");
        put("germany", "10Y1001A1001A83F");
        put("greece", "10YGR-HTSO-----Y");
        put("hungary", "10YHU-MAVIR----U");
        put("iceland ","IS");
        put("ireland", "10YIE-1001A00010");
        put("italy", "10YIT-GRTN-----B");
        put("kosovo", "10Y1001C--00100H");
        put("latvia", "10YLV-1001A00074");
        put("lithuania", "10YLT-1001A0008Q");
        put("luxembourg", "10YLU-CEGEDEL-NQ");
        put("malta ","10Y1001A1001A93C");
        put("moldova", "10Y1001A1001A990");
        put("montenegro", "10YCS-CG-TSO---S");
        put("netherlands", "10YNL----------L");
        put("north macedonia", "10YMK-MEPSO----8");
        put("norway", "10YNO-0--------C");
        put("poland", "10YPL-AREA-----S");
        put("portugal", "10YPT-REN------W");
        put("romania", "10YRO-TEL------P");
        put("russia", "RU");
        put("serbia", "10YCS-SERBIATSOV");
        put("slovakia", "10YSK-SEPS-----K");
        put("slovenia", "10YSI-ELES-----O");
        put("spain", "10YES-REE------0");
        put("sweden", "10YSE-1--------K");
        put("switzerland", "10YCH-SWISSGRIDZ");
        put("turkey", "10YTR-TEIAS----W");
        put("ukraine", "10Y1001C--00003F");
        put("united kingdom","10Y1001A1001A92E");
    }};
    /**
     * mapping energy formats to codes.
     */
    final public static Map<String, String> ENERGY_MAP = new HashMap<>(){{
        put("mixed","A03");
        put("generation","A04");
        put("load","A05");
        put("biomass","B01");
        put("fossil brown coal/lignite","B02");
        put("fossil coal-derived gas","B03");
        put("fossil gas","B04");
        put("fossil hard coal","B05");
        put("fossil oil","B06");
        put("fossil oil shale","B07");
        put("fossil peat","B08");
        put("geothermal","B09");
        put("hydro pumped storage","B10");
        put("hydro run-of-river and poundage","B11");
        put("hydro water reservoir","B12");
        put("marine","B13");
        put("nuclear","B14");
        put("other renewable","B15");
        put("solar","B16");
        put("waste","B17");
        put("wind offshore","B18");
        put("wind onshore","B19");
        put("other","B20");
        put("ac link","B21");
        put("dc link","B22");
        put("substation","B23");
        put("transformer","B24");
    }};
    /**
     * mapping codes to energy formats (reverse mapping).
     */
    final public static Map<String, String> REVERSE_ENERGY_MAP = new HashMap<>(){{
        put("A03", "mixed");
        put("A04", "generation");
        put("A05", "load");
        put("B01", "biomass");
        put("B02", "fossil brown coal/lignite");
        put("B03", "fossil coal-derived gas");
        put("B04", "fossil gas");
        put("B05", "fossil hard coal");
        put("B06", "fossil oil");
        put("B07", "fossil oil shale");
        put("B08", "fossil peat");
        put("B09", "geothermal");
        put("B10", "hydro pumped storage");
        put("B11", "hydro run-of-river and poundage");
        put("B12", "hydro water reservoir");
        put("B13", "marine");
        put("B14", "nuclear");
        put("B15", "other renewable");
        put("B16", "solar");
        put("B17", "waste");
        put("B18", "wind offshore");
        put("B19", "wind onshore");
        put("B20", "other");
        put("B21", "ac link");
        put("B22", "dc link");
        put("B23", "substation");
        put("B24", "transformer");
    }};
}
