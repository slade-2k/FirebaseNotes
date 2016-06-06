package de.iks.hotischeck.firebase.notes.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class FirebaseConnector {
	private String source;
	
	public FirebaseConnector(String source) {
		this.source = source;
	}
	
	public HttpURLConnection getConnection() {
		HttpURLConnection conn = null;

		try {
			URL url = new URL(source);
			conn = (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
