package de.iks.hotischeck.firebase.notes.util;

import java.util.List;

public class SpacingUtils {

	private static int maxSpaces;
	private static char wildcardSign = ' ';
	
	public static void setWildcard(char wildcard) {
		wildcardSign = wildcard;
	}
	
	public static void setSpaces(List<Object> elements) {
		maxSpaces = 0;
		
		for (Object element : elements) {
			maxSpaces = Math.max(element.toString().length(), maxSpaces);
		}
	}
	
	public static void printElementWithWildcard(String element, int spacesAfter) {

		System.out.print(element);
		for (int i = 0; i < (maxSpaces - element.length() + spacesAfter); i++) {
			System.out.print(wildcardSign);
		}
	}
}
