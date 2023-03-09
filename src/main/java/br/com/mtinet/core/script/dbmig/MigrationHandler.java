package br.com.mtinet.core.script.dbmig;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.com.mtinet.core.connect.ConnectionConfig;
import br.com.mtinet.core.dataBase.PoolConnection;
import br.com.mtinet.core.dataBase.SourceTableHandler;
import br.com.mtinet.core.dataBase.TableMetaData;
import br.com.mtinet.core.listener.ProgressListener;
import br.com.mtinet.core.script.AbstractHandler;
import br.com.mtinet.core.script.Handler;
import br.com.mtinet.exception.BuildException;
import br.com.mtinet.util.AttributeLoader;
import br.com.mtinet.util.FileLoadSql;
import br.com.mtinet.util.MessageCommon;
import br.com.mtinet.util.TimeHandler;

public class MigrationHandler extends AbstractHandler  implements Handler {
	private String operation = "import";
	private String tableName = "";
	private Vector indexes = new Vector();
	private TableMetaData targetMetaData;	
	private SourceTableHandler sourceTable;
	private String dsnTarget = "";
	private boolean stopAction = false;
	private MigrateStatement statementActive ;
	

	public MigrationHandler(Node tagNode,PoolConnection poolConn,ProgressListener listener)  throws Exception{		
		this.setParameters(tagNode,poolConn, listener);
	}
	
	public String getOperation() {
		return operation;
	}
	
	public void setOperation(String operation) {
		this.operation = operation;
	}	
 
	public String getTableName() {
		return tableName;
	}
	
	private void printTitle() {
		System.out.println("     	              MIGRACAO DE DADOS" );
		MessageCommon.printLine();
		System.out.println("     TABELA                                                 TEMPO");
		MessageCommon.printLine();
	}
	
	public void setListener(ProgressListener listener) {
		this.listener = listener;
	}
	
	public void stop() {
		this.stopAction = true;
	}
	
	public void execute() throws Exception {
		printTitle();
		dsnTarget = AttributeLoader.getAttributeValue(tagNode,"dsn",true) ;
		boolean autoCommit = AttributeLoader.getAttributeValue(tagNode,"autocommit",true).toUpperCase().equals("TRUE");
		String onError = AttributeLoader.getAttributeValue(tagNode,"onerror",false) ;		
		NodeList nodesMig = tagNode.getChildNodes();
		int totalRecord = 0;
		Node elementTagMig;
		for (int m=0;m<nodesMig.getLength();m++) {
			if (stopAction) break;
			elementTagMig = nodesMig.item(m);
			if (elementTagMig.getNodeType()== Node.ELEMENT_NODE) {
				if (elementTagMig.getNodeName().toUpperCase().equals("TRANSACTION")) {
					long tempoInicial  = System.currentTimeMillis();
					String value = elementTagMig.getTextContent().trim();
					String name = AttributeLoader.getAttributeValue(elementTagMig,("name"),false);
					ArrayList<String> sqls = AttributeLoader.transformeStringInSqlList(value);
					this.listener.setMax(sqls.size());
					int count = 0;
					if (name.length()>0) {
						this.listener.showNewMessage( name.toUpperCase());						
					}
					ConnectionConfig conf = poolConn.getConn(dsnTarget);	
					if (!conf.getConn().getAutoCommit()) conf.getConn().setAutoCommit(autoCommit);
					listener.setMax(sqls.size());
					for (String sql : sqls) {	
						if (stopAction) break;
						sql = sql.replace(";", "");
						if(conf.getConn().createStatement().executeUpdate(sql)>-1) count++;
					}
					listener.clearText(31);
					this.listener.showProgress(count);
					listener.showMessage(this.tempoTotal(tempoInicial));
					if (!conf.getConn().getAutoCommit()) conf.getConn().commit();
				}
				
				if (elementTagMig.getNodeName().toUpperCase().equals("MIGRATION")) {
					this.indexes = new Vector();
					this.setOperation(AttributeLoader.getAttributeValue(elementTagMig,"operation",true));
					this.setTableName(AttributeLoader.getAttributeValue(elementTagMig,("tablename"),true));
					if (!this.operation.equals("insert"))
					    this.setIndexes(AttributeLoader.getAttributeValue(elementTagMig,("indexes"),false));
					NodeList sources = elementTagMig.getChildNodes();
					for (int s = 0; s<sources.getLength();s++) {
						if (stopAction) break;
						Node source = sources.item(s);
						if (source.getNodeType()== Node.ELEMENT_NODE) {
							String dsnSource = AttributeLoader.getAttributeValue(source,"dsn",true);
							String tableTarget = AttributeLoader.getAttributeValue(source,"tableName",false);
							String sqlMig = "";
							if (tableTarget.length()>0) {
								sqlMig = "select * from " + tableTarget;
							} else {
								sqlMig = source.getTextContent().trim() ;
							}
							
							this.getSourceTable().setListener(listener) ;
							this.getSourceTable().setConn(this.poolConn.getConn(dsnSource).getConn());
							this.getSourceTable().setSql(sqlMig,this.getTableName());
							
							this.prepareStatement(dsnTarget,autoCommit,AttributeLoader.getAttributeValue(elementTagMig,"operation",true));							
							statementActive.setOnError(onError);
							
											
							try {
//								this.listener.showVisorOperation(this.tableName);
								statementActive.execute();
							} catch (Exception e) {
								System.out.println("\nERROR IN: " + statementActive.showParamets() ) ;
								throw new Exception(e.getMessage());
							}
							statementActive = null;
							totalRecord += this.getSourceTable().getRecordCount();;
						}
					
					}
					
					this.poolConn.getConn(dsnTarget).getConn().commit();
				}
				
				if (elementTagMig.getNodeName().toUpperCase().equals("EXECUTE")) {
					ConnectionConfig conf = poolConn.getConn(dsnTarget);	
					this.filePath = AttributeLoader.getAttributeValue(elementTagMig,"file",true);
					String name = AttributeLoader.getAttributeValue(elementTagMig,"name",true);
					this.listener.showNewMessage( name);
					executeFileSql(conf.getConn(),filePath);
					
				}
			}
		}
		elementTagMig = null;
		System.out.println("");
		MessageCommon.printLine();
		System.out.println("                                         TOTAL DE REGISTROS AFETADOS: "+ totalRecord);

	}
	
	private String tempoTotal(long inicio ) {
		long agora = System.currentTimeMillis();
		long decorrido = agora - inicio;
		return " Tempo total: " + TimeHandler.tempo2String(decorrido);
	}
	
	private void executeFileSql(Connection conn,String filePath) throws Exception {
		long tempoInicial  = System.currentTimeMillis();
		ArrayList<String> list = FileLoadSql.loadFile(filePath);		
		int count = 0;
		listener.setMax(list.size());
		for (String sql : list) {
			conn.createStatement().execute(sql.trim());
			count++;
		}	
		conn.commit();
		listener.clearText(31);
		this.listener.showProgress(count);
		listener.showMessage(this.tempoTotal(tempoInicial));
//		System.out.print("                                  " + count);
	}

	public void setTableName(String tableName) throws Exception {
		this.tableName = tableName;		
		try {
			Connection conn = this.poolConn.getConn(dsnTarget).getConn();
			if (conn==null) throw new Exception("Datasource " + dsnTarget + " not definde"); 
			this.targetMetaData = new TableMetaData(conn, tableName);
		}catch(Exception e) { 
			String msg = "Table " + this.tableName + " not found in datasource " + dsnTarget + " ERROR: " + e.getCause();
			throw new Exception(msg) ;
		}
	}
	
	public void setIndexes(String indexNames) throws BuildException {
		  
	      if (indexNames.length() > 0) {
	          StringTokenizer tok = new StringTokenizer(indexNames, ",", true);
	          while (tok.hasMoreTokens()) {
	              String token = tok.nextToken().trim();
	              if (token.equals("") || token.equals(",")) {
	                  throw new BuildException("Syntax Error: Index attribute for mig has an empty string for indexes.");
	              }
	              this.indexes.add(token.toUpperCase());
	              
	              if (tok.hasMoreTokens()) {
	                  token = tok.nextToken();
	                  if (!tok.hasMoreTokens() || !token.equals(",")) {
	                      throw new BuildException("Syntax Error: Index attribute for mig ends with a , character");
	                  }
	              }
	          }
	      }
	}
	
	public SourceTableHandler getSourceTable() {
		if (sourceTable==null) this.sourceTable = new SourceTableHandler(this.poolConn.getConn(0).getConn());
		return sourceTable;
	}

	public void setSourceTable(SourceTableHandler sourceTable) {
		this.sourceTable = sourceTable;
	}
	
	public void prepareStatement(String dsn,boolean autoCommit, String operation) throws SQLException,Exception {
		Connection conn = this.poolConn.getConn(dsn).getConn();
		conn.setAutoCommit(autoCommit) ;
	      if (operation.toLowerCase().equals("insert")) {
	    	  this.statementActive =  new InsertStatement(conn, this.getSourceTable() , this.tableName, this.targetMetaData);
	      } else if (operation.toLowerCase().equals("update")) {	    	  
	    	  this.statementActive =    new UpdateStatement(conn,this.getSourceTable() ,this.tableName,this.targetMetaData,indexes);
	      } else if (operation.toLowerCase().equals("import")) {
	    	 this.statementActive =   new ImportStatement(conn,this.getSourceTable() ,this.tableName,this.targetMetaData,indexes);
	      } else throw new Exception("ERRO: Propriedade operation nao e valida.");
	     
	 }
	
}
