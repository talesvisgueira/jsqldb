package br.com.mtinet.core.dataBase;

public class EnumeratedAttribute {
	private static String operation;
	public static String getValue() {
	    return operation;
	}
	public static void setValue(String value) {
	   operation = value;
	}
}
