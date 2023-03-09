package br.com.mtinet.util;

import java.util.ArrayList;

import org.w3c.dom.Node;

import br.com.mtinet.core.PropertyHandler;

public class AttributeLoader {
	
	private static PropertyHandler propertyHandler ;
	
	public static void setPropertyHandler(PropertyHandler property) {
		 propertyHandler = property;
	}
	
	private static String getSqlLine(String text) {
		String sql = text.substring(0,text.indexOf(";")+1);
		return sql.trim();
	}
	
	public static ArrayList<String> transformeStringInSqlList(String text) {
		ArrayList<String> list = new ArrayList<String>();
		while(text.contains(";")){
			String sql = getSqlLine(text);
			text = text.substring(text.indexOf(";")+1).trim();
			list.add(sql);
//			System.out.println(sql);
		}
		return list;
	}
	
	public static String getAttributeValue(Node node,String name,boolean required) throws Exception {
		String value = ""; String nodeOwner = ""; String tag = "";
		try {
			nodeOwner = node.getParentNode().getNodeName().toUpperCase();	
			tag = node.getNodeName().toUpperCase();
			value = node.getAttributes().getNamedItem(name).getNodeValue();
			if (propertyHandler!=null) value = propertyHandler.getResolveExpressionInText(value);
			
		} catch(Exception e) { 
			if (required) throwErrorAttributeNotExistInTag(nodeOwner,tag,name);
		}		
		if (required) 
			if (value.equals("")) throwErrorValueAttributeNotProperty(nodeOwner,tag,name,value);
		return value;
	}
	
	private static void throwErrorAttributeNotExistInTag(String node, String tag, String name) throws Exception {
		System.out.print("Erro na target: " + node);
		throw new Exception(" - A tag '" + tag + "' deve possuir o atributo '" + name +"'");
	}
	
	private static void throwErrorValueAttributeNotProperty(String node, String tag, String name,String value) throws Exception {
		System.out.print("Erro na target: " + node);
		throw new Exception(" - O atributo '" + name +"' da tag '"+ tag + "' possui o valor nao definido corretamente.");
	}

}
