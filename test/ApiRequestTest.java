import org.junit.Test;

public class ApiRequestTest {

    /**
     * Simple checks that the factory returns anything
     */
    @Test
    public void apiRequestFactoryTest() {
        ApiRequest ap = null;
        ap = ApiRequest.ApiReqForEnergySource("test", "test");
        assert ap != null;
    }
}
