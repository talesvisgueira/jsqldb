package br.com.mtinet.core;


import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FileXmlLoader {
	private Document doc = null;
	private PropertyHandler propertyHandler ;
	private NodeList listOfTarget;
//	private String tagDefault = "target";
	private static final String NODE_TARGERT = "target";
	private static final String NODE_TASK = "task";
	
	public FileXmlLoader(String fileName) throws Exception {		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        this.doc = docBuilder.parse (new File(fileName));
        this.doc.getDocumentElement ().normalize ();    
        this.propertyHandler = new PropertyHandler(this.getNodeList("property"));
	}

	public void setPropertyHandler(PropertyHandler property) {
		this.propertyHandler = property;
	}
	public PropertyHandler getPropertyHandler() {
		return this.propertyHandler;
	}

	public NodeList getNodeList(String tagName) throws Exception {
		return this.doc.getElementsByTagName(tagName);
	}
	
	public NodeList getNodeDefautlList() throws Exception {
		NodeList nodeList = this.doc.getElementsByTagName(NODE_TARGERT);
		if (nodeList==null) nodeList = this.doc.getElementsByTagName(NODE_TARGERT);
		return nodeList;
	}
	
	public String getAttributeValue(String attribute) throws Exception {
		return this.doc.getDocumentElement().getAttribute(attribute);
	}
	
	public String getAttributeValue(Node node, String attribute) throws Exception {
		String value = ""; String nodeOwner = ""; String tag = "";
		nodeOwner = node.getParentNode().getNodeName().toUpperCase();	
		tag = node.getNodeName().toUpperCase();
		value = node.getAttributes().getNamedItem(attribute).getNodeValue();
		if (this.propertyHandler!=null) value = this.propertyHandler.getResolveExpressionInText(value);
		return value;
	}
	
	public String getAttributeValue(Node node, String attribute,boolean required) throws Exception {
		String value = ""; String nodeOwner = ""; String tag = "";
		try {
			nodeOwner = node.getParentNode().getNodeName().toUpperCase();	
			tag = node.getNodeName().toUpperCase();
			value = node.getAttributes().getNamedItem(attribute).getNodeValue();
			if (this.propertyHandler!=null) value = this.propertyHandler.getResolveExpressionInText(value);
			
		} catch(Exception e) { 
			if (required) throwErrorAttributeNotExistInTag(nodeOwner,tag,attribute);
		}		
		if (required) 
			if (value.equals("")) throwErrorValueAttributeNotProperty(nodeOwner,tag,attribute,value);
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
	
	public Node getTargetNode (String target) throws Exception {
		this.listOfTarget = getNodeList(NODE_TARGERT);
		for(int s=0; s<this.listOfTarget.getLength() ; s++){
            Node node = this.listOfTarget.item(s);
            if(node.getNodeType() == Node.ELEMENT_NODE){
            	Node attributeName = node.getAttributes().getNamedItem("name");
            	String value = attributeName.getNodeValue() ;
            	if (value.toLowerCase().equals(target.toLowerCase())) {
            		return node;
            	}
            }
		}
		return null; 
	}

}
