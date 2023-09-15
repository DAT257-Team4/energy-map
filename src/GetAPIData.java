import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetAPIData {

	/**
	 * Send request via GET method.
	 *
	 * @param params  - Object including all URL params
	 * @return Plain XML with energy production for 1 hour. Countries may provide this data 
	 * in intervals of 1 hour, 30 minutes, or 15 minutes. Returns XML with error code 999 
	 * and a reason if request failed.
	 */
	public static InputStream sendAPIRequest(ApiRequest params) {
		//prepare URL with requesting params
		String finalURL = addUrlParams(params, Configuration.BASE_URL);

		try {
			//prepare connection
			URL url = new URL(finalURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();
			//System.out.println(responseCode);
			//success
			if (responseCode == HttpURLConnection.HTTP_OK) {
				//get result as plain XML
				//return parseToPlainString(con.getInputStream());
				return con.getInputStream();
			} else {
				//return parseToPlainString(con.getErrorStream());
				return con.getErrorStream();
			}
		} catch (IOException e) {
			System.out.println(String.format("Unexpected error. Message %s", e.getMessage()));
		}
		return null;
	}

	private static String addUrlParams(ApiRequest params, String baseURL) {
		if (!baseURL.endsWith("?")) {
			baseURL += '?';
		}

		baseURL = baseURL + "securityToken=" + params.securityToken + "&";
		baseURL = baseURL + "documentType=" + params.documentType + "&";
		baseURL = baseURL + "processType=" + params.processType + "&";
		baseURL = baseURL + "PsrType=" + params.PsrType + "&";
		baseURL = baseURL + "In_Domain=" + params.In_Domain + "&";
		baseURL = baseURL + "periodStart=" + params.PeriodStart + "&";
		baseURL = baseURL + "periodEnd=" + params.PeriodEnd;

		return baseURL;
	}
}