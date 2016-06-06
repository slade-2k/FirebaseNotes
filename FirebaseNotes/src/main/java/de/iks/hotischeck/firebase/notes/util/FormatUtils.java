package de.iks.hotischeck.firebase.notes.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class FormatUtils {

	private int maxSpaces = 0;
	
	public void getSpaces(List<String> words, int spacesAfter) {
		
		for (String word : words) {
			this.maxSpaces = Math.max(this.maxSpaces, word.length());
		}
		
		this.maxSpaces += this.maxSpaces + spacesAfter;		
	}
	
	public void printSpaces(String word) {
		System.out.print(word);
		
		for (int i = 0; i <= (this.maxSpaces - word.length()); i++) {
			System.out.print(" ");
		}
	}
	
	public List<String> enumToArrayList(Enumeration<?> e) {
		List<String> arrayList = new ArrayList<>();
		
		while (e.hasMoreElements()) {
			arrayList.add(e.nextElement().toString());
		}
		return arrayList;
	}
}
