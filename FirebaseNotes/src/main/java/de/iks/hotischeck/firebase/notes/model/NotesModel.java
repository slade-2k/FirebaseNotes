package de.iks.hotischeck.firebase.notes.model;

import java.util.ArrayList;
import java.util.List;

public class NotesModel {
	private List<String> messages;
	
	public List<String> getMessages() {
		return this.messages;
	}
	
	public void createNewList() {
		messages = new ArrayList<>();
	}
}
