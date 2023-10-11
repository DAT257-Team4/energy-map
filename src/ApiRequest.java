import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ApiRequest{
    public final String DOCUMENT_TYPE = "A75";
    public final String PROCESS_TYPE = "A16";
    private String In_Domain;
    private String PsrType;
    private String PeriodStart;
    private String PeriodEnd;
    private String securityToken;
    private ApiRequest(){}

    public String getIn_Domain() {
        return In_Domain;
    }

    public String getPsrType() {
        return PsrType;
    }

    public String getPeriodStart() {
        return PeriodStart;
    }

    public String getPeriodEnd() {
        return PeriodEnd;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    /**
     * Creates an ApiRequest object that holds all parameters for the API request
     * @param energyType Specifies an energytype, if blank or "all" this field is ignored
     * @param countryName Specifies a country to query, if blank or "all" this field is ignored
     * @return ApiRequest object with all parameters set
     */
    public static ApiRequest ApiReqForEnergySource(String energyType, String countryName){
        ApiRequest ap = new ApiRequest();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHH00");  
        countryName = countryName.toLowerCase();
        countryName.trim().equalsIgnoreCase("all");


        ap.In_Domain=CodeFormats.COUN_MAP.get(countryName);
        ap.PsrType=CodeFormats.ENERGY_MAP.get(energyType);

        //API is 1 hour behind and expects time in utc.
        //subtract 3 hours to correct for api lag and swedish summer time.
        ap.PeriodStart=dtf.format(LocalDateTime.now().minusHours(5));  // "202309010000" YYYYYMMDD0000
        ap.PeriodEnd=dtf.format(LocalDateTime.now().minusHours(4));
        ap.securityToken=Configuration.TOKEN;
        return ap;
    }
}