package de.iks.hotischeck.firebase.notes.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

import de.iks.hotischeck.firebase.notes.model.NotesModel;

public class NotesService {

	private NotesDAO notesDAO = new NotesDAO();
	private FirebaseConnector fbConn;
	
	public NotesService(String source) {
		this.fbConn = new FirebaseConnector(source);
	}
	
	public NotesModel fetchNotes() {
		return notesDAO.fetchNotes(fbConn.getConnection());
	}
	
	public boolean addNote(NotesModel notes) {
		String message = null;
		
		System.out.println("Enter note: ");
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		try {
			message = bf.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		notes.messages.add(message);
		
		if(notesDAO.pushNotes(fbConn.getConnection(), notes)) {
			return true;
		}
		notes.messages.remove(notes.messages.size() - 1);
		return false;
	}
	
	public boolean deleteNote(NotesModel notes, String source) {
		String idString, tmpMessage;
		
		System.out.println("Choose the id to delete: ");
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		Scanner scanner = new Scanner(System.in);
		Integer id = null;


		while (id == null || id >= notes.messages.size() || id < 0) {
			idString = scanner.nextLine();
			
			try {
				id = Integer.parseInt(idString);
			} catch (NumberFormatException e) {
				System.out.println("rt");
			}	
		}
			
		tmpMessage = notes.messages.get(id);
		notes.messages.remove(id.intValue());
		
		if (notesDAO.deleteNotes((new FirebaseConnector(source + id + ".json")).getConnection())) {
			return true;
		} else {
			notes.messages.add(id, tmpMessage);
			return false;
		}
	}
	
	public boolean deleteAllNotes(NotesModel notes) {
		notes.messages.removeAll(notes.messages);
		
		if (notesDAO.deleteNotes(fbConn.getConnection())) {
			return true;			
		}
		return false;
	}
	
	public NotesModel getNotes() {
		return notesDAO.fetchNotes(fbConn.getConnection());
	}
}
