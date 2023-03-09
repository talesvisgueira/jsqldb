package br.com.mtinet.core.dataBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class TableMetaData {
    private Map columns = null;

    public TableMetaData(ResultSetMetaData meta) throws SQLException,Exception {
        initialize(meta);
    }
    
    public TableMetaData(Connection conn,String schema,   String tablename) throws SQLException,Exception {
    	initialize(conn,schema+"."+tablename);
    }

    public TableMetaData(Connection conn,   String tablename) throws SQLException,Exception {
    	initialize(conn,tablename);
    }
    
    private void initialize(Connection conn,   String tablename) throws SQLException,Exception {
    	Statement stmt  = null;
        ResultSet res = null;
        ResultSetMetaData meta = null;
        try {
          stmt = conn.createStatement();
          res = stmt.executeQuery("select * from "  +tablename + " where 1 = 2");
          meta = res.getMetaData();
          initialize(meta);
        } finally {
          if (res != null) {
            try {
              res.close();
            } catch (SQLException ignore) {}
          }
          if (stmt != null) {
            try {
              stmt.close();
            } catch (SQLException ignore) {}
          }
        }
    }

    

    private void initialize(ResultSetMetaData meta) throws SQLException,Exception {
      columns = new HashMap();
      int n = meta.getColumnCount();
      ColumnMetaData column = null;
      for (int i = 1; i <= n; i++) {
        column = new ColumnMetaData();
        column.index = i;
        column.name = meta.getColumnName(i).toUpperCase();
        try {
			  column.classname = meta.getColumnClassName(i);
		} catch (Exception e) {
			 throw new Exception("Don�t support ColumnMetaData.getColumnClassName()");
		}
        column.typename = meta.getColumnTypeName(i);
        column.datatype = meta.getColumnType(i);
        try {
			column.precision = meta.getPrecision(i);
        	column.scale = meta.getScale(i);
        	column.size = meta.getColumnDisplaySize(i);
		} catch (Exception ignore) {
			throw new Exception("Invalid column's precision or scale");
		}
        columns.put(column.name, column);

      }
    }

    public ColumnMetaData getColumn(String name) {
      Object column = columns.get(name);
      if (column == null)  return null;
      else return (ColumnMetaData) column;
    }

    public Enumeration getColumns() {
      Vector ordered = new Vector(columns.values());
      Collections.sort(ordered);
      return ordered.elements();
    }

    public boolean contains(String columnName) {
      return columns.containsKey(columnName);
    }

  }