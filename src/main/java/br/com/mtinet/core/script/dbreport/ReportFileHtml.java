package br.com.mtinet.core.script.dbreport;

import java.io.File;
import java.sql.ResultSet;

import br.com.mtinet.util.FileLog;

public class ReportFileHtml extends FileLog {
	

	private File file = null;
	private String title = "";
	private ResultSet records;

	public ReportFileHtml(String title,ResultSet rs,File file) {
		this.file = file;
		this.title = title;
		this.records = rs;
	}
	
	public void execute()  throws Exception{
		int line = 0;
		String text = "<head> <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> </head> \n";
		text += "<TABLE border=1>\n";
		text += "\t<tr BGCOLOR=\"#CCCCFF\" >\n";		
		for (int c = 1; c< records.getMetaData().getColumnCount() +1;c++) {
			int size = records.getMetaData().getColumnDisplaySize(c) ;
			if (size<records.getMetaData().getColumnLabel(c).length()) 
				size = records.getMetaData().getColumnLabel(c).length();
			size += 1;
			text += "\t\t<th size=\"" + size +"\">";
			text += records.getMetaData().getColumnLabel(c).toUpperCase();
			text += "</th>\n";
		}
		if (this.printLog) System.out.print( text);
		text += "\t</tr>";
		this.save (file,text);
		line++; if (this.printLog) System.out.println(""); 
		while (records.next()) {
			this.save(file,"\t<tr>");
			for (int c = 1; c< records.getMetaData().getColumnCount() +1;c++) {
				text ="\t\t<td>";
				if (records.getString(c)!=null) text += records.getString(c);
				else text += "&nbsp;";
				text +="</td>";
				this.save(file,text);
			}
			if (this.printLog) System.out.print( text);
			this.save(file,"\t</tr>");
			line++; if (this.printLog) System.out.println("");
		}		
		this.save(file,"</Table>");
	}

	
}
