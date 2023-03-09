package br.com.mtinet.core.script.dbreport;

import java.io.File;

import java.sql.ResultSet;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Alignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ReportFileExcel {
	
	private File file = null;
	private String title = "";
	private ResultSet rs;

	public ReportFileExcel(String title,ResultSet rs,File file) {
		this.file = file;
		this.title = title;
		this.rs = rs;
	}
	
 
	
	public void execute()  throws Exception{
		WritableCellFormat wcf1=new WritableCellFormat(NumberFormats.DEFAULT);
	    wcf1.setAlignment(Alignment.RIGHT);
	    wcf1.setShrinkToFit(true);
	    wcf1.setWrap(true);
		
		WorkbookSettings config = new  WorkbookSettings();
		config.setLocale(Locale.getDefault());
		WritableWorkbook arquivo = Workbook.createWorkbook(this.file,config);
		WritableSheet planilha = arquivo.createSheet(this.title, 0);
		int line = 0;
		for (int c = 1; c< rs.getMetaData().getColumnCount() +1;c++) {
			Label label = new Label(c-1,line,rs.getMetaData().getColumnLabel(c).toUpperCase());
			planilha.addCell(label);
		}
		line++;
		while (rs.next()) {
			for (int c = 1; c< rs.getMetaData().getColumnCount() +1;c++) {
				String type = rs.getMetaData().getColumnTypeName(c);
				if (type.contains("NUM")) {
					Number num = new Number(c-1,line,rs.getDouble(c), wcf1);
					planilha.addCell(num);
				} else {
					Label label = new Label(c-1,line,rs.getString(c));
					planilha.addCell(label);
				}
			}
			line++;
		}		
		arquivo.write();
		arquivo.close();
	}
	
}
