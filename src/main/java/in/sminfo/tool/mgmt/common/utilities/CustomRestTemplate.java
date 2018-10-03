package in.sminfo.tool.mgmt.common.utilities;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class CustomRestTemplate {

	public static RestTemplate restTemplate(String hostName, Integer port, String userName, String password) {
		try {

			// only if need if server certificate is self signed(not a valid)
			trustSelfSignedSSL();
			final HttpHost host = new HttpHost(hostName, port, "https");

			// Null Host name verifier is used so that code works with self
			// signed certificate
			final CloseableHttpClient client = HttpClientBuilder.create()
					.setDefaultCredentialsProvider(provider(userName, password)).useSystemProperties()
					.setSSLHostnameVerifier(new NullHostnameVerifier()).build();

			final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactoryDigestAuth(
					host, client);
			final RestTemplate restTemplate = new RestTemplate(requestFactory);

			final int timeout = 5;
			configureTimeouts(requestFactory, timeout);
			return restTemplate;
		} catch (Exception e) {
			return null;
		}
	}

	private static void trustSelfSignedSSL() {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
						throws java.security.cert.CertificateException {
				}

				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws java.security.cert.CertificateException {
					// TODO Auto-generated method stub

				}

			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLContext.setDefault(ctx);
		} catch (Exception ex) {
			throw new RuntimeException("Exception occurred ", ex);
		}
	}

	private static final void configureTimeouts(final HttpComponentsClientHttpRequestFactory requestFactory,
			final int timeout) {
		final HttpClient httpClient = requestFactory.getHttpClient();
		// - note: timeout via raw String parameters
		// httpClient.getParams().setParameter("http.connection.timeout",
		// timeout * 1000);
		// httpClient.getParams().setParameter("http.socket.timeout", timeout *
		// 1000);
		// httpClient.getParams().setParameter("http.connection-manager.timeout",
		// new Long(timeout * 1000)); //
		// httpClient.getParams().setParameter("http.protocol.head-body-timeout",
		// timeout * 1000);
		// - note: timeout via the API
		// final HttpParams httpParams = httpClient.getParams();
		// HttpConnectionParams.setConnectionTimeout(httpParams, timeout *
		// 1000);
		// http.connection.timeout
		// HttpConnectionParams.setSoTimeout(httpParams, timeout * 1000);
		// http.socket.timeout
	}

	private static final CredentialsProvider provider(String userName, String password) {
		final CredentialsProvider provider = new BasicCredentialsProvider();
		final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, password);
		provider.setCredentials(AuthScope.ANY, credentials);
		return provider;
	}

}

class NullHostnameVerifier implements HostnameVerifier {
	@Override
	public boolean verify(final String hostname, final SSLSession session) {
		return true;
	}
}
