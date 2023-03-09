package br.com.mtinet.core.script.dbrandom;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class RandomField {
	// 33 - 47  caracteres de simpolos
	// 48 - 57  numeros
	// 58 - 64  pontuacao e sinais
	// 65 - 90  letras maiusculas
	// 91 - 96  pontuacao e acentos
	// 97 - 122 letras minusculas

	private static int createRandomIndexCaracterAscii() {
		Random obj = new Random();
		int decimal = obj.nextInt(255);
	    return decimal;
	}
	
	private static char getNumericValid() {
		int decimal = createRandomIndexCaracterAscii();
		if ( (decimal>48 && decimal <57)  ) {
			return (char)decimal;
		} else return getNumericValid();
	}
	
	
	// Metodos da classe
	public static char getAscii() {
		int decimal = createRandomIndexCaracterAscii();
		if (decimal>33 && decimal <126) {
			return (char)decimal;
		} else return getAscii();
	}
	
	public static char getCaracter() {
		int decimal = createRandomIndexCaracterAscii();
		if ((decimal>65 && decimal <90) || (decimal>97 && decimal <122)  ) {
			return (char)decimal;
		} else return getCaracter();
	}
	
	public static String getTextFixe(int size) {
		String value = "";
		for (int p =0; p<size; p++ ) {
			value += getCaracter();
		}
		return value;
	}
	
	public static String getPhraseRandom(int sizeMax) {
		String phrase = "";
		List<Integer> words = getMountWordInPhrase(sizeMax) ;
		for (Integer size : words) {
			if (size.intValue() > 1) 
				phrase = getTextRandom(size.intValue()-1) + " ";
			
		}
		return phrase;
	}

	
	public static String getTextRandom(int sizeMax) {
		Random obj = new Random();
		int sizeRandom = obj.nextInt(sizeMax)+1;
		char[] letras = new char[sizeRandom];
		for (int p = 0 ; p<sizeRandom; p++) {			
			letras[p] = RandomField.getCaracter();
		}
		obj = null;
		return new String(letras);
	}
	
	public static List getMountWordInPhrase(int size) {
		int maxSizeWord = 20;
		List<Integer> wordsSize = new ArrayList<Integer>();
		Random obj = new Random();
		
		while (size > 0) {
			int sizeRandom = obj.nextInt(maxSizeWord);
			
			if (size > sizeRandom) {
				wordsSize.add(new Integer(sizeRandom));
				size -= (sizeRandom + 1);
			} else {
				wordsSize.add(new Integer(size));
				size = 0;
			}
		}
		return wordsSize;
	}

	public static int getInt(int size) {
		Random obj = new Random();
	    return    obj.nextInt(size) ;
	}
	
	public static Long getNumeric(long size) {
		String value = "";
		String literais = new Long(size).toString();
		for (int p =0 ; p< literais.length(); p++) {
			value += getNumericValid();
		}
		return new Long(value);
	}
	
	public static Timestamp getTimestamp() {
 
		int day = getInt(1,31);
		int monty = getInt(1,12);
		int year = getInt(1920,2012);
		String data = day + "/" + monty + "/" + year;
		
		GregorianCalendar cal = new GregorianCalendar();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt","br"));
		try {
			cal.setTime(new java.util.Date( dateFormat.parse(data).getTime() ));
		}catch(Exception e) { }; 
		return new Timestamp( cal.getTime().getTime());	 
		 
 	
	}
	
	public static Date getDate() {
		long timestamp = 0;
		int day = getInt(31);
		int monty = getInt(12);
		int year = getInt(1920,2012);
		String data = day + "/" + monty + "/" + year;
		
		GregorianCalendar cal = new GregorianCalendar();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt","br"));
	    
		try{
			timestamp = dateFormat.parse(data).getTime();
 			cal.setTime(new java.util.Date( timestamp ));	 					
		}catch(Exception e) { }; 
 		return cal.getTime();

	}
	
	public static String getDateAsString() {
		long timestamp = 0;
		int day = getInt(1,31);
		int monty = getInt(1,12);
		int year = getInt(1920,2012);
//		String data = day + "/" + monty + "/" + year;
		String data = year + "-" + monty + "-" + day;
		
		GregorianCalendar cal = new GregorianCalendar();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt","br"));
		return data ;
	}
	
	public static String getByteAsString(int size) {
		byte[] b = getTextFixe(size).getBytes();
		return b.toString();
	}
	
	
	public static int getInt(int min, int max) {
		Random obj = new Random();
		int decimal = obj.nextInt(max);
		if (decimal >=min && decimal <= max) 
			return decimal;
		else return getInt(min,max);
	}
	
	
	
//	public static String getNumericFormatedBySize(Long size) {
//		String value = "";
//		String literais = new Long(size).toString();
//		for (int p =0 ; p< literais.length(); p++) {
//			value += getNumericValid();
//		}
//		return value;
//	}
	
	
}
