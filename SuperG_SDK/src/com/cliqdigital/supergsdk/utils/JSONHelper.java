package com.cliqdigital.supergsdk.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;

public class JSONHelper {
	// always verify the host - dont check for certificate
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	/**
	 * Trust every server - dont check for any certificate
	 */
	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { 
				new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * a URL the Helper will connect to
	 */
	private URL myURL;
	
	/**
	 * The connection to the myURL field
	 */
	private URLConnection uc;

	/**
	 * Data retrieved from uc
	 */
	private InputStream in;

	/**
	 * Returns the JSONObject at the specified URL.
	 * 
	 * @param url
	 *            The url where to get the json file.
	 * @return The element at the specified url.
	 */
	public JSONObject getJSONObject(String url) {
		String res = null;
		JSONObject producedObject = null;
		
		initURL(url);

		try {
			connect();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			res = convertStreamToString(in);
			if (!res.equals("")) {
				producedObject = new JSONObject(res);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return producedObject;
	}

	/**
	 * Initializes url into helper class.
	 * 
	 * @param url
	 *            The url that the Helperclass will download the JSONObject
	 *            from.
	 */
	public void initURL(String url) {
		try {
			myURL = new URL(url);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Opens connection to url and gets data from it.
	 */
	@SuppressLint("DefaultLocale")
	private boolean connect() {
		boolean ret = true;
		try {
			//StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			//StrictMode.setThreadPolicy(policy);
			
			if (myURL.getProtocol().toLowerCase().equals("https")) {
			    trustAllHosts();
				HttpsURLConnection https = (HttpsURLConnection) myURL.openConnection();
				https.setHostnameVerifier(DO_NOT_VERIFY);
				uc = https;
			} else {
				uc = (HttpURLConnection) myURL.openConnection();
			}
			
			uc.connect();
			in = uc.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}

	/**
	 * Converts an InputStream to a String.
	 * 
	 * @param is
	 *            The InputStream object to convert
	 * @return A String with the data from the InputStream <br>
	 *         "" if Error during process.
	 */
	private String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return sb.toString();
		} else {
			return "";
		}
	}
}
