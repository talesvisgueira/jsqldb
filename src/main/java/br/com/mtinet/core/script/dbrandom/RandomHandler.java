package br.com.mtinet.core.script.dbrandom;

import java.sql.Connection;
import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.com.mtinet.core.dataBase.DataBaseTest;
import br.com.mtinet.core.dataBase.PoolConnection;
import br.com.mtinet.core.listener.ProgressListener;
import br.com.mtinet.core.script.AbstractHandler;
import br.com.mtinet.core.script.Handler;
import br.com.mtinet.core.script.dbddl.DDLFactory;
import br.com.mtinet.core.script.dbddl.DDLObject;
import br.com.mtinet.util.AttributeLoader;
import br.com.mtinet.util.MessageCommon;

public class RandomHandler extends AbstractHandler  implements Handler{

	private Connection conn;
	private int minRecord = 0;
	private int maxRecord  = 0;
	private TableOrderHandler orderHandler ;
	private boolean autoCommit;
	
	public RandomHandler(Node tagNode,PoolConnection poolConn,ProgressListener listener) throws Exception {
		this.setParameters(tagNode,poolConn, listener);
		String dsn = AttributeLoader.getAttributeValue(tagNode,"dsn",true);
		this.schema = AttributeLoader.getAttributeValue(tagNode,"schema",true) ;
		this.autoCommit = AttributeLoader.getAttributeValue(tagNode,"autocommit",true).toUpperCase().equals("TRUE");
		String minRecord = AttributeLoader.getAttributeValue(tagNode,"minRecord",true) ;
		String maxRecord = AttributeLoader.getAttributeValue(tagNode,"maxRecord",true) ;
		this.conn = this.poolConn.getConn(dsn).getConn();		
		this.conn.setAutoCommit(autoCommit) ;
		this.setMaxRecord(maxRecord);
		this.setMinRecord(minRecord) ;
	
		DDLFactory factory = new DDLFactory("",conn,"temp.txt",schema,false);
		orderHandler = new TableOrderHandler(factory);
	}

	public void setMinRecord(String minRecord) {
		this.minRecord =  new Integer(minRecord).intValue();
	}

	public void setMaxRecord(String maxRecord) {
		this.maxRecord = new Integer(maxRecord).intValue();;
	}
	
	public void stop() {
		
	}
	
	
	public void execute() throws Exception {
		NodeList nodeTables = tagNode.getChildNodes();
		if (nodeTables.getLength()==0) {
			orderHandler.execute();
			printTitle();
			this.executeAll();
		} else {
			printTitle();
			for (int a=0;a<nodeTables.getLength();a++) {
				Node tableNode = nodeTables.item(a);
				if (tableNode.getNodeType()== Node.ELEMENT_NODE) {
					String tableName = AttributeLoader.getAttributeValue(tableNode,"name",true) ;
					this.execute(tableName);
				}
			}
		}
		MessageCommon.printLine();
	}
	
	

	private void printTitle() {
		MessageCommon.printLine();
		System.out.println("     	      GERACAO ALEATORIA DE DADOS DO " + schema.toUpperCase());
		MessageCommon.printLine();
		System.out.println("   TABELA                                             REGISTROS");
		MessageCommon.printLine();
	}

	private void executeAll()  throws Exception{
		
		ArrayList<DDLObject> tableList = orderHandler.getNewOrder();
		
		for (DDLObject table : tableList) {
			SqlRandomFactory obj = null;
			if (DataBaseTest.hasSchemaInDataBase(this.conn)) 
			   obj = new SqlRandomFactory(schema,conn,table,orderHandler.foreignKeyByTable(table.getName()),this.listener);
			else obj = new SqlRandomFactory(conn,table,orderHandler.foreignKeyByTable(table.getName()),this.listener);
			
			 int tot = RandomField.getInt(minRecord,maxRecord);
			 obj.execute(tot);
		}
	}
	
	private void execute(String tableName)  throws Exception{
		 DDLObject table = orderHandler.getObjTableByName(tableName);
		 SqlRandomFactory obj = null;
		 if (DataBaseTest.hasSchemaInDataBase(this.conn)) 
		    obj = new SqlRandomFactory(schema,conn,table,orderHandler.foreignKeyByTable(tableName),this.listener);
		 else obj = new SqlRandomFactory(conn,table,orderHandler.foreignKeyByTable(tableName),this.listener);
		 int tot = RandomField.getInt(minRecord,maxRecord);
		 obj.execute(tot);
	}

}
