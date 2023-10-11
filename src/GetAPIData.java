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

	/**
	 * The function takes an API request object and a base URL, and appends the request parameters to the
	 * URL to create a complete URL with query parameters.
	 * 
	 * @param params An object of type ApiRequest that contains the following properties:
	 * @param baseURL The base URL is the initial URL to which the parameters will be added. It is a
	 * string representing the base URL of the API endpoint.
	 * @return The method is returning the updated baseURL string with the added URL parameters.
	 */
	private static String addUrlParams(ApiRequest params, String baseURL) {
		if (!baseURL.endsWith("?")) {
			baseURL += '?';
		}

		baseURL = baseURL + "securityToken=" + params.getSecurityToken() + "&";
		baseURL = baseURL + "documentType=" + params.DOCUMENT_TYPE + "&";
		baseURL = baseURL + "processType=" + params.PROCESS_TYPE + "&";
		//If PsrType is null it will omit it and instead fetch all energy sources
		if (params.getPsrType() != null) {
			baseURL = baseURL + "PsrType=" + params.getPsrType() + "&";
		}
		baseURL = baseURL + "In_Domain=" + params.getIn_Domain() + "&";
		baseURL = baseURL + "periodStart=" + params.getPeriodStart() + "&";
		baseURL = baseURL + "periodEnd=" + params.getPeriodEnd();

		return baseURL;
	}
}