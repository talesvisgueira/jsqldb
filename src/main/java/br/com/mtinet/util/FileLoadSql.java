package br.com.mtinet.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class FileLoadSql {
	
	public static ArrayList<String> loadFile( String filePath) throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		String sql = "";  String line = "";   int count = 0;
//		FileResource.printText(FileResource.getText("connection.msg.load.file")+ " " + filePath +"...");
		File file = new File(filePath);
		if (file.exists()) {
//			try {
				Reader reader = new FileReader(filePath);
				BufferedReader in = new BufferedReader(reader);
			    while ((line = in.readLine()) != null){
			    	line = line.trim();
			    	if (line.startsWith("//"))  continue;	
			    	if (line.startsWith("--"))  continue;
			    	StringTokenizer st = new StringTokenizer(line);
			    	if (st.hasMoreTokens()) {
			    		String token = st.nextToken();
			    		if ("REM".equalsIgnoreCase(token))  continue;
			    	}
				    sql += " " + line;
				    sql = sql.trim();
				    if (line.indexOf("--") >= 0) sql += "\n";
				    if (line.contains(";")) {
				    	list.add(sql); 
				    	count++; sql = "";
					}
			    }
			    reader.close();
//			} catch(Exception e) {
//				
//				FileResource.printText("\n   "+e.getMessage());		
//				FileResource.printText("   SQL: "+sql);
//			}
//			FileResource.printText(FileResource.getText("connection.msg.load.file.rowAfected")+ " " + count);
		} else FileResource.printText(FileResource.getText("msg.error") +  filePath + " " + FileResource.getText("connection.msg.load.file.notExists"));
		return list;
	}
}
