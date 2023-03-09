package br.com.mtinet.core.script.dbimport;

import java.sql.Connection;
import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.com.mtinet.core.dataBase.PoolConnection;
import br.com.mtinet.core.listener.ProgressListener;
import br.com.mtinet.core.script.AbstractHandler;
import br.com.mtinet.core.script.Handler;
import br.com.mtinet.core.script.dbddl.DDLFactory;
import br.com.mtinet.util.AttributeLoader;
import br.com.mtinet.util.FileLoadSql;
import br.com.mtinet.util.MessageCommon;

public class ImportHandler  extends AbstractHandler  implements Handler{
	
//	private SourceTableHandler sourceTable;
	private String dsnTarget;
	private String type;
	private DDLFactory objDdl;
	private String dirPath;
	private boolean autoCommit;
	
	public ImportHandler(Node tagNode,PoolConnection poolConn,ProgressListener listener) throws Exception {
		this.setParameters(tagNode,poolConn, listener);
		this.dsnTarget = AttributeLoader.getAttributeValue(tagNode,"dsn",true) ;
		this.schema = AttributeLoader.getAttributeValue(tagNode,"schema",true) ;
		this.autoCommit = AttributeLoader.getAttributeValue(tagNode,"autocommit",true).toUpperCase().equals("TRUE");
//		this.sourceTable = new SourceTableHandler(this.poolConn.getConn(dsnTarget).getConn());	

//		this.type = AttributeLoader.getAttributeValue(tagNode,"type",true) ;
//		this.testTypeExport();
//		if (type.toUpperCase().equals(""))
//			this.filePath = AttributeLoader.getAttributeValue(tagNode,"filePath",true) ;
//		else this.dirPath = AttributeLoader.getAttributeValue(tagNode,"dirPath",true) ;
//		throw new Exception(" - AVISO: A funcao da tag '" + tagNode.getNodeName().toUpperCase() + "' ainda nao foi implementada...");
	}
	
	private void printTitle() {
		System.out.println("     		    IMPORTACAO DE DADOS PARA " + this.schema.toUpperCase());
		MessageCommon.printLine();
		System.out.println("     TABELA                                         REGISTROS");
		MessageCommon.printLine();
	}
	
	private void testTypeExport() throws Exception {
		if (!"sql csv txt".toLowerCase().contains(type.toLowerCase())) throw new Exception("O Atributo 'type' da tag dbExport nao e valido. Ex: [sql,csv]");
	}
	
	public void stop() {
		
	}
	
	public void execute() throws Exception  {
		String tableName = ""; String tableCurrent = "";
		printTitle(); 
		NodeList nodesImport = tagNode.getChildNodes();
		ArrayList<String> list; String cause = "";
		for (int m=0;m<nodesImport.getLength();m++) {
			Node elementTag = nodesImport.item(m);
			try {
			if (elementTag.getNodeType()== Node.ELEMENT_NODE) {
				
				if (elementTag.getNodeName().toUpperCase().equals("SCRIPT")) {
					this.filePath = AttributeLoader.getAttributeValue(elementTag,("filePath"),true);
					list = FileLoadSql.loadFile(filePath);
					Connection conn = this.poolConn.getConn(dsnTarget).getConn();
					int count = 0;					
					for (String sql : list) {
						cause = sql;
						tableName = getTableNameByScriptInsert(sql);	
						if (!tableCurrent.equals(tableName)) {
							tableCurrent = tableName; System.out.println("");
						}
						conn.createStatement().execute(sql.trim());
						System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
						System.out.print( String.format("%-50.50s","- "+tableName+ ": ") + String.format("%-25.25s",++count + " / " +list.size()));
					
					}

				} else if (elementTag.getNodeName().toUpperCase().equals("CVS")) {
					
				} else if (elementTag.getNodeName().toUpperCase().equals("TXT")) {
					
				}
			}
			} catch (Exception e ) { 
				throw new Exception(e.getMessage() + "\n" + cause);
			}
		}
		System.out.println("\n");
		MessageCommon.printLine();
	}
	
	private String getTableNameByScriptInsert(String script) {
		String value ="";
		value = script.toUpperCase().substring(script.indexOf("INSERT INTO")+12,script.length());
		if (getCountCaracter("(",script)>1)
			value = value.toUpperCase().substring(0,value.indexOf("("));
		else value = value.toUpperCase().substring(0,value.indexOf("VALUES"));
		return value.trim();
	}
	
	private int getCountCaracter(String value,String text) {
		int count = 0;
		for (int c = 0; c< text.length(); c++) {
			String test = text.substring(c,c+1);
			if (test.equals(value)) count++;
		}
		return count;
	}
}
