import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetAPIData {

	public static final String BASE_URL = "https://web-api.tp.entsoe.eu/api";

	/**
	 * Send request via GET method.
	 *
	 * @param params  - object including all URL params
	 * @return plain XML content
	 */
	public static String sendAPIRequest(ApiRequeste params) {
		//prepare URL with requesting params
		String finalURL = addUrlParams(params, BASE_URL);

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
				return parseToPlainString(con.getInputStream());
			} else {
				return parseToPlainString(con.getErrorStream());
			}
		} catch (IOException e) {
			System.out.println(String.format("Unexpected error. Message %s", e.getMessage()));
		}
		return null;
	}

	private static String addUrlParams(ApiRequeste params, String baseURL) {
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

	private static String parseToPlainString(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder out = new StringBuilder();
		String newLine = System.getProperty("line.separator");
		String line;
		while ((line = reader.readLine()) != null) {
			out.append(line);
			out.append(newLine);
		}
		in.close();
		return out.toString();
	}
}