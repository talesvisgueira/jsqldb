package br.com.mtinet.core;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PropertyHandler {
	
	private NodeList listOfProperties ;

	public PropertyHandler(NodeList listOfProperties) {
		this.listOfProperties = listOfProperties;
		while(isExpressionInList()) {
			this.adaptProperties();
		}
		
	}
	
	public String getResolveExpressionInText(String text) {
		 while (isExpressionInProperties(text)) {
			 String expression = getExpressionInString(text);
			 String valueExpression = locateExpressionValue(expression);
			 text = text.replace(expression, valueExpression); 
		 } 
		 return text;
	}
	
	private boolean isExpressionInList() {
		 for(int s=0; s<listOfProperties.getLength() ; s++){		 
			 if (isExpressionInProperties(listOfProperties.item(s).getAttributes().getNamedItem("value").getNodeValue())) return true; 
		 }
		 return false;
	}
	
	private void adaptProperties() {
		 for(int s=0; s<listOfProperties.getLength() ; s++){
			 Node node = listOfProperties.item(s);
			 String value = node.getAttributes().getNamedItem("value").getNodeValue();			 
			 if (isExpressionInProperties(value)) {
				 String expression = getExpressionInString(value);
				 String valueExpression = locateExpressionValue(expression);
				 String newValue = value.replace(expression, valueExpression);
				 listOfProperties.item(s).getAttributes().getNamedItem("value").setNodeValue(newValue);
			 }
		 }
		 
	}
	
	public void listProperties() {
		 for(int s=0; s<listOfProperties.getLength() ; s++){
			 Node node = listOfProperties.item(s);
			 String name = node.getAttributes().getNamedItem("name").getNodeValue();
			 String value = node.getAttributes().getNamedItem("value").getNodeValue();
			 System.out.println("PROPERTIES: " +  name + "=" + value );
		 }
	}
	
	private boolean isExpressionInProperties(String value) {
			if(value.contains("$")) return true;
			else return false;
	}
	
	private String getExpressionInString(String text) {
		return text.substring(text.indexOf("${"),text.indexOf("}")+1);
	}
	
	private String getTextInternInExpression(String expression) {
		return expression.substring(expression.indexOf("${")+2,expression.indexOf("}"));
	}
	
	private String locateExpressionValue(String expression) {	
		 String textInExpressin = getTextInternInExpression(expression);
		 for(int s=0; s<listOfProperties.getLength() ; s++){
			 String property = listOfProperties.item(s).getAttributes().getNamedItem("name").getNodeValue();
			 if (textInExpressin.equals(property)){
				 String valueExpression = listOfProperties.item(s).getAttributes().getNamedItem("value").getNodeValue();
				 return valueExpression;
			 } 
		 }
		 return "";
	}
}
