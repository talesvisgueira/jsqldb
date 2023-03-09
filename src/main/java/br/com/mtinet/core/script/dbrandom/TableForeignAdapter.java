package br.com.mtinet.core.script.dbrandom;


public class TableForeignAdapter {
	
	
	
	public static String getTableConstraint(String script) {
		String tableName = script.substring(script.indexOf("ALTER TABLE")+11,script.length()).toUpperCase().trim();
		tableName = tableName.substring(0,tableName.indexOf("ADD")).trim();
//		if (tableName.indexOf(".")>0) tableName = tableName.substring(tableName.indexOf(".")+1, tableName.length());
		return tableName.trim();
	}
	
	public static String getTableReferences(String script) {
		String tableName = script.substring(script.indexOf("REFERENCES")+10,script.length()).toUpperCase().trim();
		tableName = tableName.substring(0,tableName.indexOf("(")-1).trim();
//		if (tableName.indexOf(".")>0) tableName = tableName.substring(tableName.indexOf(".")+1, tableName.length());
		return tableName.trim();
	}
	
	public static String getFiledsPrimaryKey(String script) {
		String value = script.toUpperCase();
		if (value.contains("REFERENCES")) {
			value = value.substring(value.indexOf("REFERENCES"));
			value = value.substring(value.indexOf("(")+1,value.indexOf(")"));
		} else value = "";
		
		return value;
	}
	
	public static String getFiledsForeignKey(String script) {
		String value = script.toUpperCase();
		if (value.contains("CONSTRAINT")) {
			value = value.substring(value.indexOf("CONSTRAINT"));
			value = value.substring(value.indexOf("(")+1,value.indexOf(")"));
		} else value = "";
		
		return value;
	}
	
	public static String getSqlByTableReferences(String script){
		return "select max(" + getFiledsPrimaryKey(script) + ") from " + getTableReferences(script) + ";";
	}
	
	public static int getRandomValueForeignKey(int maxId) {
		return RandomField.getInt(maxId);
	}
	
}
