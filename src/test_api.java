
//TEMPORARY FILE FOR TESTING API REQUESTS
public class test_api {
    public static void main(String[] args) {
        ApiRequest params = ApiRequest.ApiReqForEnergySource("Solar", "Sweden");
        String xml = GetAPIData.sendAPIRequest(params);
        System.out.println(xml);
    }
}
