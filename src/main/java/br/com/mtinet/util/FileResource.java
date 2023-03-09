package br.com.mtinet.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class FileResource {

	
	public static ResourceBundle getBundle() {
		Locale locale = Locale.getDefault();
		ResourceBundle bundle = ResourceBundle.getBundle("app",locale);
		if (bundle==null) ResourceBundle.getBundle("app",new Locale("en","US"));
    	return bundle;
	}
	public static String getText(String value) {
		return getBundle().getString(value);
	}
//	public static String getText(String value,String param) {
//		String text = getBundle().getString(value).replace("$s", param);
//		return text;
//	}
	public static String getText(String value,String ... params) {
		String text = "";
		for (String param : params ) {
			text = getBundle().getString(value).replace("$s", param);
		}
		return text;
	}
	public static void printBundle(String value) {
		System.out.println(getBundle().getString(value));
	}
	public static void printBundleInLine(String value) {
		System.out.print(getBundle().getString(value));
	}
	public static void printText(String value) {
		System.out.println(value);
	}
}
