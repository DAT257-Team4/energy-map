import org.junit.Test;

public class ApiRequestTest {

    /**
     * Simply checks that the factory returns an object with the correct values
     */
    @Test
    public void apiRequestFactoryTest() {
        ApiRequest ap = ApiRequest.ApiReqForEnergySource("solar", "sweden");
        assert (ap.getPsrType().equals(CodeFormats.ENERGY_MAP.get("solar")) &&
                (ap.getIn_Domain().equals(CodeFormats.COUN_MAP.get("sweden"))));
    }
}
