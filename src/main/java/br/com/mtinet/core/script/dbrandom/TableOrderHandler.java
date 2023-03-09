package br.com.mtinet.core.script.dbrandom;

import java.util.ArrayList;

import br.com.mtinet.core.script.dbddl.DDLFactory;
import br.com.mtinet.core.script.dbddl.DDLObject;

public class TableOrderHandler {
	private DDLFactory factory;
	private ArrayList<DDLObject> foreignKeyList;
	private ArrayList<DDLObject> tableList;
	private ArrayList<DDLObject> orderList;
//	private TableForeignAdapter foreignAdapter ;
	private int MAX_REPEAT = 10;
	
	public TableOrderHandler(DDLFactory factory ) {
		this.factory = factory;		
		this.tableList = factory.getDDLObjectTables();	
		foreignKeyList = factory.getAllDDLObjectForeingKey();
		orderList= new ArrayList<DDLObject>();
	}
	
	public void execute() throws Exception {
		int count = 0;
		System.out.println("- Ordenando as " + this.tableList.size() + " tabelas existentes...");
		System.out.print("- Tentativa de ordenacao: " + String.format("%-5.5s",count));		
		while(this.tableList.size()>0) {
			System.out.print("\b\b\b\b\b" + String.format("%-5.5s",++count));
			transferirTabelasBase();
			removeTableOrder();
			removeForeignOrder();
			if (count>=MAX_REPEAT) 
				throw new Exception("\n ERRO: Impossivel ordenar tabelas");
		}
		System.out.println("                RESULTADO: Tabelas ordenadas");
	}
	
	public void transferirTabelasBase() {
		for (DDLObject table : tableList) {
	    	 if (hasForeignForTableList(table.getName())) {
	    		 ArrayList<DDLObject> list = foreignKeyByTable(table.getName());
	    		 int value = 0;
	    		 for (DDLObject obj : list) {
	    			 String tableReference = TableForeignAdapter.getTableReferences(obj.getScript());
	    			 if (tableReference.indexOf(".")>0) tableReference = tableReference.substring(tableReference.indexOf(".")+1, tableReference.length());
	    			 if (hasTableInOrderList(tableReference)) {
	    				 value++;
	    			 } 
	    		 }
	    		 if (value==list.size()) {
	    			 insertTableInOrderList(table.getName(),table.getScript()); 
	    		 }
	    	 } else {
	    		 insertTableInOrderList(table.getName(),table.getScript());	    		  
	    	 }
	    }		
	}
	
	private void insertTableInOrderList(String tableName,String script) {
		 if (!hasTableInOrderList(tableName)) {
			 DDLObject newTable = new DDLObject("TABLE",tableName,script);
    		 orderList.add(newTable);	 
		 }
	}
	public ArrayList<DDLObject> foreignKeyByTable(String tableName ){
		factory.setPrintLog(false);
		ArrayList<DDLObject> list = new ArrayList<DDLObject>();
		ArrayList<DDLObject> foreignList = factory.getAllDDLObjectForeingKey();
		for (DDLObject obj : foreignList) {
			String table = TableForeignAdapter.getTableConstraint(obj.getScript());
			if (table.indexOf(".")>0) table = table.substring(table.indexOf(".")+1, table.length());
			if (table.toUpperCase().equals(tableName.toUpperCase())) 
				list.add(obj);
		}
		return list;
	}
	
	private boolean hasForeignForTableList(String tableName) {
		String table ="";
		for (DDLObject obj : foreignKeyList) {
			table = TableForeignAdapter.getTableConstraint(obj.getScript());
			if (table.indexOf(".")>0) table = table.substring(table.indexOf(".")+1, table.length());
   		    if (table.toUpperCase().equals(tableName.toUpperCase())) 
   		    	return true;
   	 	}
		return false;
	}
	
	public ArrayList<DDLObject> getNewOrder() {
		return orderList;
	}
	
	public DDLObject getObjTableByName(String tableName) throws Exception{
		for (DDLObject table : tableList) {
			if (table.getName().toUpperCase().equals(tableName.toUpperCase())) return table;
		}
		throw new Exception("Tabela '" + tableName +"' nao existe no banco de dados.");
	}
	
	private void removeTableOrder() {
		ArrayList<DDLObject> newList = new ArrayList<DDLObject>();
		for (DDLObject table : tableList) {
			if (!hasTableInOrderList(table.getName())) {
				newList.add(table);
			}
		}
		this.tableList = newList;
	}
	
	private boolean hasTableInOrderList(String tableName) {
		for (DDLObject table : orderList) {
			if (table.getName().toUpperCase().equals(tableName.toUpperCase())) 
				return true;
		}
		return false;
	}
	
	private void removeForeignOrder(){
		ArrayList<DDLObject> newList = new ArrayList<DDLObject>();
		for (DDLObject fk  : foreignKeyList) {			 
			String tableReference = TableForeignAdapter.getTableReferences(fk.getScript()).toUpperCase();
			if (tableReference.indexOf(".")>0) tableReference = tableReference.substring(tableReference.indexOf(".")+1, tableReference.length());
			boolean value = false;
			for (DDLObject table : orderList) {	
				String tableOrder = table.getName().toUpperCase();
				if (tableReference.equals(tableOrder) ) {
					value = false; break;
				} else value = true;
			}
			if (value) newList.add(fk);
		}
		
		this.foreignKeyList = newList;
	}

	
//	private String getTableConstraintInForeignKeyList(String script) {
//		String tableName = script.substring(script.indexOf("ALTER TABLE")+11,script.length()).trim();
//		tableName = tableName.substring(0,tableName.indexOf("ADD")).trim();
//		if (tableName.indexOf(".")>0) tableName = tableName.substring(tableName.indexOf(".")+1, tableName.length());
//		return tableName.trim();
//	}
//	
//	private String getTableReferencesInForeignKey(String script) {
//		String tableName = script.substring(script.indexOf("REFERENCES")+10,script.length()).trim();
//		tableName = tableName.substring(0,tableName.indexOf("(")-1).trim();
//		if (tableName.indexOf(".")>0) tableName = tableName.substring(tableName.indexOf(".")+1, tableName.length());
//		return tableName.trim();
//	}

}
