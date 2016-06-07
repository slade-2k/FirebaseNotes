package de.iks.hotischeck.firebase.notes.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
		
		if (notes.messages.isEmpty()) {
			return false;
		}
		
		//No resource leak, because jvm closes the System.in stream
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		Integer id = null;


		while (id == null) {
			System.out.println("Choose the id to delete: ");
			idString = scanner.nextLine();
			
			try {
				id = Integer.parseInt(idString);
				if (id >= notes.messages.size() || id < 0) {
					System.out.println("id must be a value from 0 to " + (notes.messages.size() - 1) + ".");
					id = null;
				}
				
			} catch (NumberFormatException e) {
				System.out.println("Please only enter numbers");
			}	
		}
			
		tmpMessage = notes.messages.get(id);
		notes.messages.remove(id.intValue());
		if (notesDAO.deleteNotes((new FirebaseConnector(source + "/" + id + ".json")).getConnection())) {
			return true;
		} else {
			notes.messages.add(id, tmpMessage);
			return false;
		}
	}
	
	public boolean deleteAllNotes(NotesModel notes, String source) {	
		if (notes.messages.isEmpty()) {
			return false;
		}
		
		notes.messages.removeAll(notes.messages);
		
		if (notesDAO.deleteNotes(new FirebaseConnector(source + ".json").getConnection())) {
			return true;			
		}
		return false;
	}
	
}
