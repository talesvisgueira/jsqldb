package br.com.mtinet.core.dataBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import br.com.mtinet.core.listener.ProgressListener;
import br.com.mtinet.util.TimeHandler;


public class SourceTableHandler {
	
	private Connection conn;
	private String dns;
	private String sql;
	private TableMetaData metaData;
	private ResultSet records;
	private Map currentRecord  ;
	private String incrementColumnName = null;
	private int incrementColumnValue = 0;
	private int recordCount = 0;
	private String splitColumnName = null;
    private String splitColumnDelimiter = ",";
    private String splitColumnType = DelimiterType.NORMAL;
    private Enumeration splitColumnText = null;
    private long tempoInicial = 0;
    private ProgressListener listener;
	
	public SourceTableHandler(Connection conn){
		this.currentRecord = new HashMap();
		this.conn = conn;
		
	}
	
	public void close() {
		this.conn = null;
		this.metaData = null;
		this.records = null;
		this.currentRecord.clear();
		this.currentRecord = null;
		this.listener = null;
	}
	
	public void setListener(ProgressListener listener) {
		this.listener = listener;
	}
	
	public Connection getConn() {
		return this.conn ;
	}
	
	public void setConn(Connection conn) {
		this.conn = conn;
		if (currentRecord == null) currentRecord = new HashMap();
	}
	
	public String getSql() {
		String newSql = sql.replaceAll("\n", " ");
		newSql = newSql.replaceAll("\t", " ");
		return newSql.toString();
	}
	
	public void setSql(String sql,String tableName) throws Exception {
		this.sql = sql;
		this.recordCount = 0;
		this.incrementColumnValue = 0;
		this.setRecordCount();
		if (tableName.contains(".")) tableName = tableName.substring(tableName.indexOf(".")+1,tableName.length());

		this.listener.showNewMessage(tableName);
		
//		this.records = conn.createStatement().executeQuery(sql);	
		this.records = conn.createStatement().executeQuery(sql);
//		this.records.setFetchSize(1000);
		this.metaData = new TableMetaData(records.getMetaData());
		
		tempoInicial = System.currentTimeMillis();
		
	}
	
	public void open() throws Exception {
		this.records = conn.createStatement().executeQuery(sql);
	}
	
	public TableMetaData getMetaData() {
		return metaData;
	}
	
	public void setMetaData(TableMetaData metaData) {
		this.metaData = metaData;
	}
	
	public ResultSet getRecords() {
		return records;
	}
	
	public void setRecords(ResultSet records) {
		this.records = records;
	}
	
	public boolean next(String tableName) throws SQLException {
		int mod = incrementColumnValue % this.listener.getModuleSize();

        if (records == null)
          throw new SQLException("Table is not open");
        if (splitColumnText != null && splitColumnText.hasMoreElements()) {
          incrementColumnValue++;
          Object val = splitColumnText.nextElement();
          currentRecord.put(splitColumnName.toUpperCase(), val);
          if (incrementColumnName != null)
          	 currentRecord.put(incrementColumnName.toUpperCase(), incrementColumnValue);
          return true;
        }

        if (records.next()) {
            incrementColumnValue++;
            currentRecord.clear();
            splitColumnText = null;
   	    	if ( mod==0 && incrementColumnValue > 1 ) 
   	    		showProgress(tableName);
   	    	if (incrementColumnValue==recordCount) 
   	    		showProgress(tableName);
            return true;
        } else {
//        	showProgress(tableName);
//	    	listener.showTime(this.tempoTotal(this.tempoInicial));
//	    	return false;
        	
        	
        	showProgress(tableName.toUpperCase());
        	listener.clearText(25);
	    	listener.showMessage(this.tempoTotal(this.tempoInicial));
	    	return false;
        }
        
    }
	

	
	private void showProgress(String tableName) {
//		if (tableName.contains(".")) tableName = tableName.substring(tableName.indexOf(".")+1,tableName.length());
//		listener.clearLine();
//		listener.showVisorOperation(tableName.toUpperCase());
//   		listener.showProgress(incrementColumnValue);
//   		listener.showTime(TimeHandler.logTempo(this.tempoInicial,incrementColumnValue,this.getRecordCount()));
		

		if (tableName.contains(".")) tableName = tableName.substring(tableName.indexOf(".")+1,tableName.length());
		listener.clearText(85);
		listener.showMessage(String.format("%-45.45s","- "+tableName.toUpperCase()+ ": "));
   		listener.showProgress(incrementColumnValue);
   		listener.showTime(TimeHandler.logTempo(this.tempoInicial,incrementColumnValue,this.getRecordCount()));
	}
	
	
	public Object getObject(String column) throws SQLException {
		column = column.toLowerCase();
	      Object val = currentRecord.get(column);
	      if (val == null && !currentRecord.containsKey(column)) {
						if (incrementColumnName != null && incrementColumnName.equalsIgnoreCase(column)) {
							val = new Integer(incrementColumnValue);
						} else {
							val = records.getObject(column);
							if (column.equalsIgnoreCase(splitColumnName)) {
								splitColumnText = splitText(val.toString(), splitColumnDelimiter);
								if (splitColumnText.hasMoreElements())
									val = (String) splitColumnText.nextElement();
							}
						}
	        currentRecord.put(column, val);
	      }
	      return val;
	}
	 
	protected Enumeration splitText(String text, String delimiter) {
	      if (DelimiterType.NORMAL.equals(splitColumnType))
	        return splitNormalText(text, delimiter);
	      else if (DelimiterType.ROW.equals(splitColumnType))
	        return splitRowText(text, delimiter);
	      else {
	        Vector list = new Vector();
	        list.add(text);
	        return list.elements();
	      }
	}
	
	 protected Enumeration splitNormalText(String text, String delimiter) {
	      Vector list = new Vector();
	      try {
	        String token = "";
	        StringTokenizer st = new StringTokenizer(text, delimiter);
	        if (st.hasMoreTokens()) {
	          token = st.nextToken().trim();
	          if (token.length() > 0) {
	            list.add(token);
	          }
	        }
	        return list.elements();
	      } catch (Exception e) {
//	        log("splitNormaText error: " + e, Project.MSG_ERR);
	        return list.elements();
	      }
	 }
	 
	 protected Enumeration splitRowText(String text, String delimiter) {
	      Vector list = new Vector();
	      StringReader sr = new StringReader(text);
	      BufferedReader br = new BufferedReader(sr);
	      try {
	        String token = "";
	        String line = "";

	        while ((line = br.readLine()) != null){
	          line = line.trim();
	          if (line.startsWith(delimiter)) {
	            if (token.length() > 0)
	              list.add(token);
	            token = line.substring(delimiter.length());
	          } else {  //continuacao do token
	            //if (token.length() > 0)
	            //  token += "\n";
	            token += line;
	          }
	        }
	        if (token.length() > 0) //ultimo token
	          list.add(token);
	        return list.elements();
	      } catch (Exception e) {
//	        log("splitNormaText error: " + e, Project.MSG_ERR);
	        return list.elements();
	      } finally {
	        try {
	          br.close();
	        } catch (IOException ignore) {}
						sr.close();
	      }
	    }
	 
	 public int getRecordPosition() {
		 return this.incrementColumnValue;
	 }
	 
	 public void setRecordCount() {
		    String viewTempName = "view_temp";
		    this.recordCount = 0;
		    String countSql = "";
		    String sql = "";
		    if (this.conn.toString().toUpperCase().contains("FIREBIRD") ) {
		    	try {
		    		sql = "create view " + viewTempName + " as "+getSql().toString();
			    	conn.createStatement().execute(sql);
			    	countSql = "select count(*) from " +viewTempName;
		    	} catch (Exception e) {}
		    	
		    } else if (conn.toString().toUpperCase().contains("ORACLE")) {
		    	countSql = "select count(*) from (" + getSql().toString() + ")";
		    } else {
		    	sql = getSql().toString();
		    	if (sql.toLowerCase().contains("order by")) 
		    		sql = sql.substring(0, sql.indexOf("order by"));
		    	countSql = "select count(*) from (" + sql + ") _sql_ ";
		    }
//			String countSql = "select count(*) from (" + getSql().toString() + ")";
//			try {if (!conn.toString().toUpperCase().contains("ORACLE") ) countSql += " _sql_ "; } catch (Exception e) {}
			Statement stmt = null;
			ResultSet res = null;
			try {
				stmt = conn.createStatement();
				res = stmt.executeQuery(countSql);
				if (res.next()) {
					this.recordCount =  res.getInt(1);
					this.listener.setMax(this.recordCount);
				} else this.recordCount =  -1;
			
			} catch (Exception ignore) {
				this.recordCount =  -1;
			} finally {
				if (res != null) {
					try {
						res.close();
					} catch (Exception ignore) {}
				}
				if (stmt != null) {
					try {
						stmt.close();
					} catch (Exception ignore) { }
				}
			}
			if (this.conn.toString().toUpperCase().contains("FIREBIRD") ) {
		    	try {
		    		sql = "drop view " + viewTempName;
			    	conn.createStatement().execute(sql);
		    	} catch (Exception e) {}
		    	
		    }
		}
	 
	 public int getRecordCount() {
		 return this.recordCount;
	 }

	
	private String tempoTotal(long inicio ) {
		long agora = System.currentTimeMillis();
		long decorrido = agora - inicio;
		return " Tempo total: " + TimeHandler.tempo2String(decorrido);
	}
	
}
