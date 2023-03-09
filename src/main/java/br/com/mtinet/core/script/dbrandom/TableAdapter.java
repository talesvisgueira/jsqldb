package br.com.mtinet.core.script.dbrandom;

public class TableAdapter {
	public static String getFiledsPrimaryKey(String script) {
		String value = script.toUpperCase();
		if (value.contains("PRIMARY KEY")) {
			value = value.substring(value.indexOf("PRIMARY KEY"));
			value = value.substring(value.indexOf("(")+1,value.indexOf(")"));
		} else value = "";
		
		return value;
	}
}
