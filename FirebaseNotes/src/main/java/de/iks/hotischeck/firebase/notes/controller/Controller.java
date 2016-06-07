package de.iks.hotischeck.firebase.notes.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import de.iks.hotischeck.firebase.notes.model.NotesModel;
import de.iks.hotischeck.firebase.notes.model.Options;
import de.iks.hotischeck.firebase.notes.util.SpacingUtils;

public class Controller {
	
	private static final String CONFIG_PATH = "config.properties";
	
	private NotesModel notes = new NotesModel();
	private NotesService notesService;
	private Properties prop;

	public static void main(String[] args) {
		Controller controller = new Controller();
		controller.init();
	}

	public void init() {
		
		this.prop = this.getPropValues(CONFIG_PATH);
		this.notesService = new NotesService(prop.getProperty("source"));
		
		if ((this.notes = notesService.fetchNotes()).messages == null) {
			this.notes.messages = new ArrayList<>();
			System.out.println("FirebaseNotes is empty! Created new List.\n");
		}
		
		this.mainMenuLoop();
		
	}
	
	public void mainMenuLoop() {
		Options option = null;
		boolean breakOut = false;
		
		do {
			option = promptForOption();
			switch(option) {
			case HELP: 
				this.showHelp();
				break;
				
			case GET_NOTES:
				if (!this.notes.messages.isEmpty()) {
					this.printNotes(this.notes);										
				} else {
					System.out.println("No entries.\n");
				}
				break;
				
			case ADD_NOTE: 
				if (notesService.addNote(notes)) {
					System.out.println("Note successfully added.\n");
				} else {
					System.out.println("Note could not be added.\n");
				}
				break;
			
			case DELETE_NOTE:
				if (notesService.deleteNote(notes, prop.getProperty("noteSource"))) {
					System.out.println("Note successfully deleted.\n");
				} else {
					System.out.println("Error while trying to delete the note. Maybe there are no notes yet.\n");
				}
				break;
				
			case DELETE_ALL:
				if (notesService.deleteAllNotes(notes, prop.getProperty("noteSource"))) {
					System.out.println("Deleted all notes.\n");
				} else {
					System.out.println("Error while trying to delete all notes. Maybe there are no notes yet.\n");
				}
				break;
				
			case FETCH_NOTES:
				notes = notesService.fetchNotes();
				break;
				
			case EXIT:
			case QUIT:
				breakOut = true;
				break;
			}	
			
		} while (!breakOut);
	}
	
	private void showHelp() {
		Properties helpProp = this.getPropValues("help.properties");
		
		List<Object> listOfProperties = Collections.list(helpProp.keys());
		listOfProperties.sort(null);
		
		for (Object entry : listOfProperties) {
			
			SpacingUtils.setSpaces(listOfProperties);
			SpacingUtils.setWildcard(' ');
			SpacingUtils.printElementWithWildcard(entry.toString(), 2);

			SpacingUtils.setSpaces(Collections.list(helpProp.elements()));
			SpacingUtils.setWildcard(' ');
			SpacingUtils.printElementWithWildcard(helpProp.getProperty(entry.toString()), 2);

			System.out.println("");
		}
		System.out.println("");
	}
	
	private Options promptForOption() {
		String input = null;
		
		do {
			System.out.print("> ");
			BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
			try {
				input = bf.readLine();
			} catch (IOException e) {
				System.out.println("Error while processing input");
				e.printStackTrace();
			}			
		} while (!isEnumValue(input));
		return Options.valueOf(input.toUpperCase());
	}
	
	private boolean isEnumValue(String input) {
		for (Options opt : Options.values()) {
			if (opt.name().equalsIgnoreCase(input)) {
				return true;
			}
		}
		return false;
	}
	
	private Properties getPropValues(String propFileName) {
		Properties prop = new Properties();

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

		try {
			if (inputStream != null) {
				prop.load(inputStream);
				return prop;
			} else {
				System.out.println("Property file could not be found!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void printNotes(NotesModel notes) {
		for (int i = 0; i < notes.messages.size(); i++) {
			System.out.println(i + " | "+ notes.messages.get(i));
		}
	}

	

	

}
