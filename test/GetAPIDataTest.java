import org.junit.Test;

import java.io.InputStream;

public class GetAPIDataTest {

    /**
     * Simply checks that anything is returned, important thing is that a connection is established which this checks
     */
    @Test
    public void sendAPIRequestTest() {
        InputStream is = GetAPIData.sendAPIRequest(ApiRequest.ApiReqForEnergySource("nuclear", "germany"));
        assert is != null;
    }
}
