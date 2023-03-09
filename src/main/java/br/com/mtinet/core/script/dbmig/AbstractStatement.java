package br.com.mtinet.core.script.dbmig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Vector;

import br.com.mtinet.core.dataBase.ColumnMetaData;
import br.com.mtinet.core.dataBase.SourceTableHandler;
import br.com.mtinet.core.dataBase.TableMetaData;
import br.com.mtinet.util.FileResource;

public abstract class AbstractStatement  {
	public static String PARAM_ON_ERROR_CONTINUE = "CONTINUE";
	public static String DB_TYPE_CLOB = "CLOB";
	protected int errorCount = 0;
	protected String sql = "";
	protected PreparedStatement pstmt = null;
	protected Vector sourceParams = null;
	protected TableMetaData targetMeta = null;
	protected SourceTableHandler sourcetable = null;
	protected DecimalFormat decimalFormatter = null;
	protected Connection conn;
	protected String schema;
	protected String tableName;
	protected String onError = "continue";
	protected Vector indexes;
	private static Integer itemInteger;
	private static Number  itemNumber;
	private static BigDecimal itemDecimal;
	private static byte[] itemBytes;
	private static String itemString;
	private static TableMetaData sourceMeta;
	private static ColumnMetaData sourceColumn;
	private static ColumnMetaData targetColumn;
	private static Object val;
	private static Clob itemClob;
	
    public SourceTableHandler getSourcetable() {
		return sourcetable;
	}

	public void setSourcetable(SourceTableHandler sourcetable) {
		this.sourcetable = sourcetable;
	}

	public void log(String text, int messageType) {
    	System.out.println(text);
    }
	
    protected void bindParameters() throws SQLException {
      sourceMeta = sourcetable.getMetaData();
      int n = sourceParams.size();
      for (int i = 0; i < n; i++)
      try {
        sourceColumn = (ColumnMetaData) sourceParams.elementAt(i);
        targetColumn = targetMeta.getColumn(sourceColumn.name);
        val = sourcetable.getObject(sourceColumn.name);

        if (sourceColumn != null) {
//          log("src.column: " + sourceColumn, Project.MSG_DEBUG);
        }
        if (targetColumn != null) {
//          log("tgt.column: " + targetColumn, Project.MSG_DEBUG);
        }
        if (val != null) {
//          log("val.class: " + val.getClass().getName(), Project.MSG_DEBUG);
        }

        if (val != null) {
          if (sourceColumn.datatype == targetColumn.datatype) {
        	  pstmt.setObject(i+1, val);
            
          } else if ((val instanceof Number) && (decimalFormatter != null)) {
        	    itemNumber = (Number) val;
        	    itemString = decimalFormatter.format(itemNumber.doubleValue());
				pstmt.setString(i+1, itemString);
          } else if (val instanceof Integer) {
	      	  	itemInteger = (Integer) val;
				pstmt.setInt(i+1, itemInteger);
          } else if (val instanceof BigDecimal) {
        	    itemDecimal= (BigDecimal) val;
				pstmt.setBigDecimal(i+1, itemDecimal);
          } else if (val instanceof Blob) {
        	    itemBytes = blobToBytes((Blob)val);
				pstmt.setBytes(i+1, itemBytes);
          }  else if (val instanceof Clob) {
        	    itemBytes = clobToBytes((Clob) val,1,((Clob) val).length());
				pstmt.setBytes(i+1, itemBytes);
          } else if (val instanceof String && targetColumn.typename.equals(DB_TYPE_CLOB)) {
//        	  itemClob = oracle.sql.CLOB.createTemporary(this.conn, false, oracle.sql.CLOB.DURATION_SESSION);
//        	  itemClob.setString(1, (String) val);
			  pstmt.setClob(i+1, stringToClob((String)val) );
		  } else if (targetColumn != null && targetColumn.typename.equals(DB_TYPE_CLOB)) {
			  itemString = val.toString();
			  bindCharacterStream(pstmt, i+1, itemString);
          } else {
        	    itemString = val.toString();
        	    pstmt.setString(i+1, itemString);
          }
        } else pstmt.setNull(i+1, targetColumn.datatype);
      } catch (SQLException e) { throw e; }
    }

    
    public String showParamets()  {
    	String value = "";
        Object val = null;
        ColumnMetaData sourceColumn = null;
        ColumnMetaData targetColumn = null;
        Number num = null;
        String textVal = null;
        int n = sourceParams.size();
        for (int i = 0; i < n; i++)
        try {
    
          sourceColumn = (ColumnMetaData) sourceParams.elementAt(i);
          targetColumn = targetMeta.getColumn(sourceColumn.name);
          
          val = sourcetable.getObject(sourceColumn.name) ;
          
          if (val != null) {
        	  value += sourceColumn.name + "=" + val + ", ";
          } else {
        	  value += sourceColumn.name + "=null, ";
          }
        } catch (SQLException e) {
//          log("Error on binding param(" + (i+1) + "): " + e, Project.MSG_ERR);
        }
        return value;
      }
    
    public byte[] blobToBytes(java.sql.Blob b){
        java.io.InputStream is = null;
        try {
            is = b.getBinaryStream();
            byte[] bytes = new byte[(int)b.length()];
            is.read(bytes);
            return bytes;
        } catch( java.sql.SQLException e ){
            return null;
        } catch( java.io.IOException e ){
            return null;
        } finally {
            try  {
                if ( is != null )is.close();
            } catch( Exception e ) {}
        }
    }

    public byte[] clobToBytes (Clob value, long start, long length) {
		try {
		    return value.getSubString(start, (int) length).getBytes();
		} catch (SQLException e) {
			return null;
		}
	}
    
    public byte[] stringToBytes (String value) {
    	return value.getBytes();
    }
    
    public Clob stringToClob(String text) {
    	Clob localClob = null;
    	try {
    		localClob = this.conn.createClob();
	    	BufferedWriter br = new BufferedWriter(localClob.setCharacterStream(0));
	    	StringReader strrd=new StringReader(text);
	    	char aux;
	    	do{
		    	aux=(char)strrd.read();
		    	br.write(aux);
	    	}while(aux != -1);
    	} catch (Exception e) { 
    		System.out.println(e.getMessage());
    	}
    	return localClob;
    }


		private String clob2String(Clob clob) throws SQLException {
			BufferedReader in = null;
			char[] buffer = null;
			String text = null;
			try {
			int n = (int) clob.length();
			buffer = new char[(int) clob.length()];
			in = new BufferedReader(clob.getCharacterStream());
			in.read(buffer);
			in.close();
			text = new String(buffer);
			return text;
			} catch (IOException e) {
				System.err.println(FileResource.getText("operation.msg.error") + e);
				return null;
			}
		}

  private void bindCharacterStream(PreparedStatement pstmt, int index, String value) throws SQLException {
      Reader in = new StringReader(value);
      try {
        pstmt.setCharacterStream(index, in, value.length());
      } finally {
        try {
          in.close();
        } catch (Exception e) {
//          log("Error on closing CLOB stream: " + e, Project.MSG_ERR);
        }
      }
    }

    private int getResultSetCount(ResultSet res) throws SQLException {
      int n = 0;
      if (res.next()) {
        n = res.getInt(1);
      }
      return n;
    }

    public int execute() throws SQLException, Exception {
	    int n = 0;
    	while(this.sourcetable.next(tableName)) {
    		try {
    			bindParameters();
      	      	if (!pstmt.execute()) {
      	      		n = pstmt.getUpdateCount();
      	      	} else {
      	      		n = getResultSetCount(pstmt.getResultSet());
      	      	}
      	      
    		} catch(Exception e) { 
    			if (!this.onError.toUpperCase().equals(PARAM_ON_ERROR_CONTINUE)) {
    				System.out.println("\n" + FileResource.getText("operation.msg.error") +  showParamets());
    				throw e; 
    			}
    		}
    	}
      return n;
    }
    
    public int executeRecord() throws SQLException, Exception {
	    int n = 0;
    		try {
    			bindParameters();
      	      	if (!pstmt.execute()) {
      	      		n = pstmt.getUpdateCount();
      	      	} else {
      	      		n = getResultSetCount(pstmt.getResultSet());
      	      	}
      	      
    		} catch(Exception e) { 
    			throw e;
    		}

      return n;
    }

    public void close() {
//    	itemClob = null;
//    	itemBytes = null;
      if (pstmt != null)
        try {
          pstmt.close();
        } catch (SQLException ignore) {}
    }

	public PreparedStatement getPstmt() {
		return pstmt;
	}

	public void setPstmt(PreparedStatement pstmt) {
		this.pstmt = pstmt;
	}
	
	public void setOnError(String value) {
		this.onError = value;
	}
    
    
  }
