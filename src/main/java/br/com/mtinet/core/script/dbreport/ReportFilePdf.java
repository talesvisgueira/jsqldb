package br.com.mtinet.core.script.dbreport;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import br.com.mtinet.util.FileLog;

public class ReportFilePdf  extends FileLog{
	
	private File file = null;
	private String title = "";
	private ResultSet records;
	private boolean multiLine;
	private String page;
	private Font textFont16 = new Font(Font.TIMES_ROMAN,16,Font.BOLD,new Color(0,0,0));
	private Font textFontBold = new Font(Font.TIMES_ROMAN,12,Font.BOLD,new Color(0,0,0));
	private Font textFont= new Font(Font.TIMES_ROMAN,12,Font.NORMAL,new Color(0,0,0));
	private Font textFont4= new Font(Font.TIMES_ROMAN,4,Font.NORMAL,new Color(0,0,0));
	
	public ReportFilePdf(String title,ResultSet rs,File file,String page,boolean multiLine) {
		this.file = file;
		this.title = title;
		this.records = rs;
		this.page = page;
		this.multiLine = multiLine;
	}
	
	private Rectangle getPageFormat(String page) {
		Rectangle pageFormat = PageSize.A4;
		if (this.page.toUpperCase().equals("A1")) pageFormat = PageSize.A1;
		if (this.page.toUpperCase().equals("A2")) pageFormat = PageSize.A2;
		if (this.page.toUpperCase().equals("A3")) pageFormat = PageSize.A3;
		if (this.page.toUpperCase().equals("A5")) pageFormat = PageSize.A5;
		if (this.page.toUpperCase().equals("B4")) pageFormat = PageSize.B4;
		if (this.page.toUpperCase().equals("B5")) pageFormat = PageSize.B5;
		if (this.page.toUpperCase().equals("LETTER")) pageFormat = PageSize.LETTER;
		if (this.page.toUpperCase().equals("LEGAL")) pageFormat = PageSize.LEGAL;
		return pageFormat;
	}
	
	public void execute()  throws Exception {
		
		Document doc = new Document(getPageFormat(this.page),10,10,10,10);
		FileOutputStream file = new FileOutputStream(this.file.getPath());
		PdfWriter writer = PdfWriter.getInstance(doc,file);
		if (this.multiLine) mountReportInLine(doc);
		else mountReportInColumn(doc);
		doc.close();
	}
	
	private void mountReportInLine(Document doc)  throws Exception{
		int columnCount = records.getMetaData().getColumnCount();
		if (columnCount>4) doc.setPageSize(getPageFormat(this.page).rotate());
		doc.open();
		doc.newPage();
		
		PdfPTable tab = new PdfPTable(1);
		insertColumnInTable(tab,"\n"+this.title.toUpperCase(),textFont16,Element.ALIGN_CENTER,1,0.90f);		
		tab.setWidthPercentage(100f);
		int[] head = new int[1];
		head[0] = 100;
		tab.setWidths(head);
		doc.add(tab);

		PdfPTable tabHead = new PdfPTable(columnCount);
		int[] cols = getColunmTableSize(records);
		for (int c = 1; c< columnCount +1;c++) {
			String header =  records.getMetaData().getColumnLabel(c).toUpperCase();
			insertColumnInTable(tabHead,header,textFontBold,Element.ALIGN_CENTER,1,0.90f);
		}
		
		tabHead.setWidthPercentage(100f);
		tabHead.setWidths(cols);
		doc.add(tabHead);
		
		PdfPTable tabRecord = new PdfPTable(columnCount);
		int[] colsRecord = getColunmTableSize(records);
		int count = 0;
		while (records.next()) {
			for (int c = 1; c< columnCount +1;c++) {	
				String rec = records.getString(c);
				if (records.getMetaData().getColumnTypeName(c).contains("NUMER"))
					insertColumnInTable(tabRecord,rec,textFont,Element.ALIGN_CENTER,1,0.99f);
				else insertColumnInTable(tabRecord,rec,textFont,Element.ALIGN_LEFT,1,0.99f);
			}
			count++;
		}	
		insertColumnInTable(tabRecord,"TOTAL DE REGISTROS: " + count ,textFont,Element.ALIGN_RIGHT,columnCount,0.90f);;
		tabRecord.setWidthPercentage(100f);
		tabRecord.setWidths(colsRecord);
		doc.add(tabRecord);
	}
	
	private void mountReportInColumn(Document doc)  throws Exception{
		int columnCount = records.getMetaData().getColumnCount();
		int columnView = 2;
		doc.open();
		doc.newPage();
		
		while (records.next()) {
			PdfPTable tab = new PdfPTable(1);
			insertColumnInTable(tab,"\n"+this.title.toUpperCase(),textFont16,Element.ALIGN_CENTER,1,0.90f);		
			tab.setWidthPercentage(100f);
			int[] head = new int[1];
			head[0] = 100;
			tab.setWidths(head);
			doc.add(tab);
			
			PdfPTable tabRecord = new PdfPTable(columnView);
			int[] colsRecord = new int[columnView];
			colsRecord[0] = 30;
			colsRecord[1] = 70;
			insertColumnInTable(tabRecord,"CAMPO",textFontBold,Element.ALIGN_CENTER,1,0.90f);
			insertColumnInTable(tabRecord,"VALOR",textFontBold,Element.ALIGN_CENTER,1,0.90f);
			for (int c = 1; c< columnCount +1;c++) {	
				String rec = records.getString(c);
				insertColumnInTable(tabRecord,records.getMetaData().getColumnName(c)+":",textFont,Element.ALIGN_LEFT,1,0.90f);
				insertColumnInTable(tabRecord,rec,textFont,Element.ALIGN_LEFT,1,0.99f);
			}
			insertColumnInTable(tabRecord,"",textFont4,Element.ALIGN_CENTER,columnView,0.90f);;
			tabRecord.setWidthPercentage(100f);
			tabRecord.setWidths(colsRecord);
			doc.add(tabRecord);
			doc.newPage();
		}	
		
		
		
	}
	
	private int[] getColunmTableSize (ResultSet rs)  throws Exception {
		int columnCount = records.getMetaData().getColumnCount();
		int[] cols = new int[columnCount];
		for (int c = 1; c< columnCount +1;c++) {	
			int size = records.getMetaData().getColumnDisplaySize(c) ;
			if (size<records.getMetaData().getColumnLabel(c).length()) 
				size = records.getMetaData().getColumnLabel(c).length();
			size += 5; 
			cols[c-1] = size;
		}
		return cols;
	}
	
	private void insertColumnInTable(PdfPTable tab, String text,Font font, int align, int colspan, float grayFill) {
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph(text,font));
		cell.setColspan(colspan) ;
		cell.setGrayFill(grayFill);
		cell.setHorizontalAlignment(align);
		tab.addCell(cell);
	}
	 
}
