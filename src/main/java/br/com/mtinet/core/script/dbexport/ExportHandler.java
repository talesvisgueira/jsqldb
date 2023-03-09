package br.com.mtinet.core.script.dbexport;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.com.mtinet.core.dataBase.DataBaseTest;
import br.com.mtinet.core.dataBase.PoolConnection;
import br.com.mtinet.core.dataBase.SourceTableHandler;
import br.com.mtinet.core.dataBase.TableMetaData;
import br.com.mtinet.core.listener.ProgressListener;
import br.com.mtinet.core.script.AbstractHandler;
import br.com.mtinet.core.script.Handler;
import br.com.mtinet.core.script.dbddl.DDLFactory;
import br.com.mtinet.core.script.dbddl.DDLObject;
import br.com.mtinet.core.script.dbrandom.TableOrderHandler;
import br.com.mtinet.util.AttributeLoader;
import br.com.mtinet.util.MessageCommon;

public class ExportHandler extends AbstractHandler implements Handler {
	
	private SourceTableHandler sourceTable;
	private String dsnSource;
	private String type;
	private DDLFactory objDdl;
	private String dirPath;
	private boolean fileExist = false;
	private TableOrderHandler orderHandler ;


	public ExportHandler(Node tagNode,PoolConnection poolConn,ProgressListener listener) throws Exception {
		this.setParameters(tagNode,poolConn, listener);
		this.dsnSource = AttributeLoader.getAttributeValue(tagNode,"dsn",true) ;
		this.sourceTable = new SourceTableHandler(this.poolConn.getConn(dsnSource).getConn());	
		this.sourceTable.setListener(listener);
		this.schema = AttributeLoader.getAttributeValue(tagNode,"schema",true) ;
		this.type = AttributeLoader.getAttributeValue(tagNode,"type",true) ;
		this.testTypeExport();
		this.dirPath = AttributeLoader.getAttributeValue(tagNode,"dirPath",true) ;
		this.objDdl = new DDLFactory("dns",sourceTable.getConn(),schema+".sql" ,schema,false);		
	}
	
	private void testTypeExport() throws Exception {
		if (!"sql csv txt xls".toLowerCase().contains(type.toLowerCase())) throw new Exception("O Atributo 'type' da tag dbExport nao e valido. Ex: [sql,csv]");
	}
	
	private void createFile(String file) throws Exception {
		this.filePath = dirPath + "//" + file;
		this.createNewFile();
	}
	
	private void printTitle() {
		System.out.println("     		  EXPORTACAO DE DADOS DO "  + this.schema.toUpperCase());
		MessageCommon.printLine();
		System.out.println("     TABELA                     REGISTROS            TEMPO");
		MessageCommon.printLine();
	}
	
	public void stop() {
		
	}
	
	public void execute() throws Exception {		
		printTitle();
		NodeList nodeTables = tagNode.getChildNodes();
		if (nodeTables.getLength()==0) {
			this.executeAll();
		}else {
			for (int a=0;a<nodeTables.getLength();a++) {
				Node tableNode = nodeTables.item(a);
				if (tableNode.getNodeType()== Node.ELEMENT_NODE) {
					String tableName = AttributeLoader.getAttributeValue(tableNode,"name",true) ;
					if (tableNode.getNodeName().toUpperCase().equals("TABLE")) {									
						this.execute(tableName);	
					}else if (tableNode.getNodeName().toUpperCase().equals("SOURCE")) {
						String source = tableNode.getTextContent().trim() ;
						this.execute(tableName,source);
					}		
					
				}
			}
		}
		this.listener.showNewMessage("");
		MessageCommon.printLine();
	}
	
	private void createFileExport(String tableName) throws Exception{		
		if (type.toUpperCase().equals(ExportStatement.EXPORT_TYPE_SQL.toUpperCase())) 
			if (!fileExist) createFile(schema + "." +ExportStatement.EXPORT_TYPE_SQL);
		else if (type.toUpperCase().equals(ExportStatement.EXPORT_TYPE_CSV.toUpperCase())) 
			createFile(schema + "_" +tableName+ "."+ExportStatement.EXPORT_TYPE_CSV);
		else if (type.toUpperCase().equals(ExportStatement.EXPORT_TYPE_TXT.toUpperCase())) 
			createFile(schema + "_" +tableName+ "."+ExportStatement.EXPORT_TYPE_TXT);
		this.fileExist = true;
	}
	
	private void executeAll() throws Exception  {		
		orderHandler = new TableOrderHandler(objDdl);
		orderHandler.execute();
		ArrayList<DDLObject> tables = orderHandler.getNewOrder();
//		ArrayList<DDLObject> tables = objDdl.getDDLObjectTables();
		for (DDLObject table : tables) {
			createFileExport(table.getName());	
			String sql = "";
			TableMetaData targetMeta =null;
			ExportStatement export = null;
			if (DataBaseTest.hasSchemaInDataBase(this.sourceTable.getConn()))
				  sql = "select * from " + schema + "."+ table.getName();
			else sql = "select * from " +  table.getName();
			sourceTable.setSql(sql,table.getName());
			if (DataBaseTest.hasSchemaInDataBase(this.sourceTable.getConn())) {
				  targetMeta = new TableMetaData(sourceTable.getConn(),schema, table.getName());
				  export = new ExportStatement(sourceTable,schema,table.getName(),targetMeta,this.type,filePath);
			}else {
				targetMeta = new TableMetaData(poolConn.getConn(this.dsnSource).getConn(), table.getName());
				export = new ExportStatement(sourceTable,table.getName(),targetMeta,this.type,filePath);
			}
			export.executeStatement();
		}	
	}
	
	private void execute(String tableName) throws Exception  {		
		createFileExport(tableName);
		String sql = "";
		TableMetaData targetMeta =null;
		ExportStatement export = null;
		if (DataBaseTest.hasSchemaInDataBase(this.sourceTable.getConn()))  sql = "select * from " + schema + "."+ tableName;
		else sql = "select * from " +  tableName;		
		sourceTable.setSql(sql,tableName);		
		if (DataBaseTest.hasSchemaInDataBase(this.sourceTable.getConn())) {
			  targetMeta = new TableMetaData(sourceTable.getConn(),schema, tableName);
			  export = new ExportStatement(sourceTable,schema,tableName,targetMeta,this.type,filePath);
		}else {
			targetMeta = new TableMetaData(poolConn.getConn(this.dsnSource).getConn(), tableName);
			export = new ExportStatement(sourceTable,tableName,targetMeta,this.type,filePath);
		}
		export.executeStatement();
	}
	
	private void execute(String tableName,String source)throws Exception  {		
		createFileExport(tableName);
		sourceTable.setSql(source.toString(),tableName);
		TableMetaData targetMeta = null;
		ExportStatement export = null;
		if (DataBaseTest.hasSchemaInDataBase(this.sourceTable.getConn())) {
			  targetMeta = new TableMetaData(sourceTable.getConn(),schema, tableName);
			  export = new ExportStatement(sourceTable,schema,tableName,targetMeta,this.type,filePath);
		} else {
			targetMeta = new TableMetaData(sourceTable.getConn(), tableName);
			export = new ExportStatement(sourceTable,tableName,targetMeta,this.type,filePath);
		}
		
		export.executeStatement();
	}
}
