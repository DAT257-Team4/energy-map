
//TEMPORARY FILE FOR TESTING API REQUESTS
public class temp_api {
    public static void main(String[] args) {
        ApiRequeste params = new ApiRequeste("A75", //actual generation per type
                                            "A16", //realized
                                            "10Y1001A1001A46L", //SE3
                                            "B16", //solar
                                            "202309110000", 
                                            "202309120000", 
                                            "dd98d018-07ab-4af2-9aa5-45b1a7d7c6b2");

        String xml = GetAPIData.sendAPIRequest(params);
        System.out.println(xml);
    }

}
