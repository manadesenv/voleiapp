package com.manades.voleiapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@EnableScheduling
@SpringBootApplication
public class VoleiappApplication {

	static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException {
		VoleiappApplication.trustAllCerts();
		SpringApplication.run(VoleiappApplication.class, args);
	}

	private static void trustAllCerts() throws NoSuchAlgorithmException, KeyManagementException {
		TrustManager[] trustAllCerts = new TrustManager[]{
				new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
					public void checkClientTrusted(X509Certificate[] certs, String authType) {}
					public void checkServerTrusted(X509Certificate[] certs, String authType) {}
				}
		};
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		HostnameVerifier allHostsValid = (hostname, session) -> true;
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

}
