package br.com.mtinet.core.script.dbreport;

import java.io.File;
import java.sql.ResultSet;

import org.w3c.dom.Node;

import br.com.mtinet.core.dataBase.PoolConnection;
import br.com.mtinet.core.dataBase.SourceTableHandler;
import br.com.mtinet.core.listener.ProgressListener;
import br.com.mtinet.core.script.AbstractHandler;
import br.com.mtinet.core.script.Handler;
import br.com.mtinet.util.AttributeLoader;
import br.com.mtinet.util.MessageCommon;

public class ReportHandler extends AbstractHandler implements Handler {
	public static final String TYPE_TXT = "txt";
	public static final String TYPE_CSV = "csv";
	public static final String TYPE_XLS = "xls";
	public static final String TYPE_PDF = "pdf";
	public static final String TYPE_HTML = "html";
	private SourceTableHandler sourceTable;
	private String dsnSource;
	private String type;
	private String name;

	public ReportHandler(Node tagNode,PoolConnection poolConn,ProgressListener listener) throws Exception {
		this.setParameters(tagNode,poolConn,listener);		
		this.dsnSource = AttributeLoader.getAttributeValue(tagNode,"dsn",true) ;
		this.sourceTable = new SourceTableHandler(this.poolConn.getConn(dsnSource).getConn());	
		this.sourceTable.setListener(listener);
		this.name = AttributeLoader.getAttributeValue(tagNode,"name",true) ;
		this.type = AttributeLoader.getAttributeValue(tagNode,"type",true) ;
		this.testTypeReport();
		this.filePath = AttributeLoader.getAttributeValue(tagNode,"dirPath",true) ;
		this.filePathAdapt();
	}
	
	private void filePathAdapt() throws Exception {
		filePath += "//" +this.name + "." + this.type.toLowerCase();
	}
	
	private void testTypeReport() throws Exception {
		if (!(TYPE_TXT+TYPE_CSV+TYPE_XLS+TYPE_PDF+TYPE_HTML).toLowerCase().contains(type.toLowerCase())) throw new Exception("O Atributo 'type' da tag dbReport nao e valido. Ex: [txt,csv,xls,html,pdf]");
	}
	
	public void stop() {
		
	}
	
	public void execute() throws Exception {			
		String sql = tagNode.getTextContent().toString();
		String title = AttributeLoader.getAttributeValue(tagNode,"title",true) ;
		String page = AttributeLoader.getAttributeValue(tagNode,"page",true) ;
		boolean multiLine = AttributeLoader.getAttributeValue(tagNode,"multiLine",true).toLowerCase().equals("true");
		ResultSet rs = this.poolConn.getConn(this.dsnSource).getConn().createStatement().executeQuery(sql);
		
		File file = new File(this.filePath);
		if (file.exists()) file.delete();
		if (type.toUpperCase().equals(TYPE_TXT.toUpperCase())) 
			new ReportFileTxt(title,rs,file).execute();
		else if (type.toUpperCase().equals(TYPE_CSV.toUpperCase())) 
			new ReportFileCvs(title,rs,file).execute();
		else if (type.toUpperCase().equals(TYPE_PDF.toUpperCase())) 
			new ReportFilePdf(title,rs,file,page,multiLine).execute();
		else if (type.toUpperCase().equals(TYPE_XLS.toUpperCase())) 
			new ReportFileExcel(title,rs,file).execute();
		else if (type.toUpperCase().equals(TYPE_HTML.toUpperCase())) 
			new ReportFileHtml(title,rs,file).execute();
		listener.showMessage(" - O relatorio foi criado no arquivo '" + this.filePath + "'...\n");
		MessageCommon.printLine();	 
	}
	
}
