package br.com.mtinet.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


public class FileLoadCvs {
	private  String line ;
	private  ArrayList<String> columns;
	private  ArrayList<String> lines;

	public ArrayList<String> getColumns() {
		return columns;
	}

	public void setColumns(ArrayList<String> columns) {
		this.columns = columns;
	}

	public ArrayList<String> getLines() {
		return lines;
	}

	public void setLines(ArrayList<String> lines) {
		this.lines = lines;
	}

	private void loadColumns(String filePath) throws Exception {
		Reader reader = new FileReader(filePath);
		BufferedReader in = new BufferedReader(reader);
		String line = in.readLine();
		columns = new  ArrayList<String>();
		SpliterHandler spliter = new SpliterHandler(line,",");
		for (String campo : spliter.getStrings() ){
			columns.add(campo);
		}
		reader.close();
	}
	
	private void convertToSQL(String tableName, String filePath) throws Exception {
		String sql = "";   int count = 0; 
		Reader reader = new FileReader(filePath);
		BufferedReader in = new BufferedReader(reader);
		lines = new ArrayList<String>();
	    while ((line = in.readLine()) != null){
//	    	line = line.trim();
	    	if (line.contains(this.columns.get(1))) continue;
	    	SpliterHandler spliter = new SpliterHandler(line,",");
	    	List<String> campos = spliter.getStrings();
			for (String campo : campos ){
				if (campo.contains("'")) campo = campo.replace("'","''''");
				sql += "'" + campo + "', ";
			}
			if (campos.size() == (this.columns.size()))
			   lines.add("insert into " + tableName + " values (" + sql.substring(0,sql.length()-2) + ");");
			else lines.add("insert into " + tableName + " values (" + sql + " '');");
			count++;
			sql = "";
	    }
	    reader.close();
	}
	
	private String tratarCampoHora(String value) {
		SpliterHandler spliter = new SpliterHandler(value,".");
		return spliter.getString(1) + ":" + spliter.getString(2) + ":" +spliter.getString(3) ;
	}
	
	private void loadRowArray(String filePath) throws Exception {
		String sql = "";   int count = 0; 
		Reader reader = new FileReader(filePath);
		BufferedReader in = new BufferedReader(reader);
		lines = new ArrayList<String>();
	    while ((line = in.readLine()) != null){
//	    	line = line.trim();
//	    	if (line.contains(this.columns.get(1))) continue;
	    	SpliterHandler spliter = new SpliterHandler(line,",");
	    	List<String> campos = spliter.getStrings();
			String timestamp = campos.get(0) + " " + tratarCampoHora (campos.get(1));
			String lista = "";
			for (int i = 2;i<15;i++) {
				lista += campos.get(i) + ",";
			}
      	 lines.add( "''"+timestamp + "''," + lista + ",");
      	 lista = "";
			count++;
			sql = "";
	    }
	    reader.close();
	}
	
	public void loadFile( String filePath) throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		String sql = "";  String line = "";   int count = 0;
//		FileResource.printText(FileResource.getText("connection.msg.load.file")+ " " + filePath +"...");
		File file = new File(filePath);
		if (file.exists()) {
			loadColumns(filePath);
			this.loadRowArray(filePath);
		} else FileResource.printText(FileResource.getText("msg.error") +  filePath + " " + FileResource.getText("connection.msg.load.file.notExists"));
	}
	
	public ArrayList<String> loadFileCSVtoSQL(String tableName, String filePath) throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		String sql = "";  String line = "";   int count = 0;
//		FileResource.printText(FileResource.getText("connection.msg.load.file")+ " " + filePath +"...");
		File file = new File(filePath);
		if (file.exists()) {
			loadColumns(filePath);
			convertToSQL(tableName,filePath);
		} else FileResource.printText(FileResource.getText("msg.error") +  filePath + " " + FileResource.getText("connection.msg.load.file.notExists"));
		return list;
	}
}
