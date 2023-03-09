package br.com.mtinet.util;

import java.util.ArrayList;
import java.util.List;

public class SpliterHandler {
	
	private List<String> strings ;
	
	public SpliterHandler(String line, String delimitator) {
		inicialize(line,delimitator);
	}
	
	public SpliterHandler(String line) {
		inicialize(line," ");
	}
	
	private void inicialize(String line, String delimitator) {
		this.strings = new ArrayList<String>();
		String text = "";
		for (int pos = 0; pos < line.length();pos++) {
			String character = line.substring(pos, pos+1);
			if (!character.equals(delimitator)) text += character;
			else {
				if (text.length()>0) this.strings.add(text.trim());
				text = ""; 
			}
		}
		if (text.length()>0) this.strings.add(text.trim());
	}
	
	public List<String> getStrings() {
		return this.strings;
	}
	
	public String getString(int position) {
		int cont = 1;
		for (String value: this.strings) {
			if (cont==position) return value.trim();
			cont++;
		}
		return "";
	}
}
