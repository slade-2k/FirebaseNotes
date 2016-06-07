package de.iks.hotischeck.firebase.notes.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import org.omg.CORBA.SystemException;

import com.google.gson.Gson;

import de.iks.hotischeck.firebase.notes.model.NotesModel;

public class NotesDAO {
	
	private Gson gson;
	
	public NotesDAO() {
		gson = new Gson();
	}
	
	public boolean pushNotes(HttpURLConnection conn, NotesModel notes) {
		 try {
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			
			String json = gson.toJson(notes);
			OutputStream os = conn.getOutputStream();
			os.write(json.getBytes());
			os.flush();
			
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return false;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}
		return true;
	}
	
	public NotesModel fetchNotes(HttpURLConnection conn) {
		NotesModel notes = null;
		
		try {
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : Http error code: " + conn.getResponseCode());
			}

			String json = readStream(conn.getURL());
			notes = gson.fromJson(json, NotesModel.class);
			

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}
		return notes == null ? new NotesModel() : notes;
	}
	
	public boolean deleteNotes(HttpURLConnection conn) {
		
		try {
			conn.setRequestMethod("DELETE");
			conn.setRequestProperty("OK", "application/json");
			
			if (conn.getResponseCode() != 200) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private String readStream(URL url) {
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(url.openStream()));

			StringBuilder buffer = new StringBuilder();
			char[] chars = new char[1024];
			int read;

			while ((read = reader.read(chars)) != -1) {
				buffer.append(chars, 0, read);
				return buffer.toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeBufferedReader(reader);
		}
		return null;
	}
	
	private void closeBufferedReader(BufferedReader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

}
