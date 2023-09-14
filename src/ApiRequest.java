import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
class ApiRequest{
    public String documentType;
    public String processType;
    public String In_Domain;
    public String PsrType;
    public String PeriodStart;
    public String PeriodEnd;
    public String securityToken;
    public ApiRequest(){};

    public static ApiRequest ApiReqForEnergySource(String energyType, String countryName){
        ApiRequest ap =new ApiRequest();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHH00");  


        ap.documentType="A75";
        ap.processType="A16";
        ap.In_Domain=CodeFormats.COUN_MAP.get(countryName);
        ap.PsrType=CodeFormats.ENERGY_MAP.get(energyType);
        ap.PeriodStart=dtf.format(LocalDateTime.now().minusHours(1));  // "202309010000" YYYYYMMDD0000
        ap.PeriodEnd=dtf.format(LocalDateTime.now());
        ap.securityToken=Configuration.TOKEN;
        return ap;
    }



}