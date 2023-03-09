package br.com.mtinet.core.script.dbreport;

import java.io.File;
import java.sql.ResultSet;

import br.com.mtinet.util.FileLog;
import br.com.mtinet.util.MessageCommon;

public class ReportFileTxt extends FileLog {
	

	private File file = null;
	private String title = "";
	private ResultSet records;

	public ReportFileTxt(String title,ResultSet rs,File file) {
		this.file = file;
		this.title = title;
		this.records = rs;
	}
	
	public void execute()  throws Exception{
		String text ="";
		int line = 0;
		for (int c = 1; c< records.getMetaData().getColumnCount() +1;c++) {
			int size = records.getMetaData().getColumnDisplaySize(c) ;
			if (size<records.getMetaData().getColumnLabel(c).length()) 
				size = records.getMetaData().getColumnLabel(c).length();
			size += 1;
			String header =  records.getMetaData().getColumnLabel(c).toUpperCase();
			text += String.format("%-"+size+"."+size+"s",MessageCommon.alignText(header, size, MessageCommon.ALIGN_CENTER));
		}
		if (this.printLog) System.out.print( text);
		this.save (file,text);
		line++; if (this.printLog) System.out.println(""); 
		while (records.next()) {
			text ="\n";
			for (int c = 1; c< records.getMetaData().getColumnCount() +1;c++) {
				int size = records.getMetaData().getColumnDisplaySize(c) ;
				if (size<records.getMetaData().getColumnLabel(c).length()) 
					size = records.getMetaData().getColumnLabel(c).length();
				size += 1;
				String rec = String.format("%-"+size+"."+size+"s",records.getString(c));
				
				text += rec;
			}
			if (this.printLog) System.out.print( text);
			this.save(file,text);
			line++; if (this.printLog) System.out.println("");
		}		

	}

	
}
