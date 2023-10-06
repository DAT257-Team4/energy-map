import org.junit.Test;

public class ApiRequestTest {

    /**
     * Simply checks that the factory returns anything
     */
    @Test
    public void apiRequestFactoryTest() {
        ApiRequest ap = ApiRequest.ApiReqForEnergySource("test", "test");
        assert ap != null;
    }
}
