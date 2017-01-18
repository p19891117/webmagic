package us.codecraft.webmagic.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class Dom4jUtils {
	private SAXReader sax;
	private InputStream in;
	private Map<String, String> namespace;
	private Document document;
	private Dom4jUtils(){}
	private Dom4jUtils(InputStream in){
		this.in = in;
	}
	public static Dom4jUtils custom(InputStream in){
		return new Dom4jUtils(in);
	}
	
	public Dom4jUtils setNamespace(Map<String, String> namespace) {
		this.namespace = namespace;
		return this;
	}
	public Dom4jUtils build() throws DocumentException{
		sax = new SAXReader();
		if(namespace!=null&&namespace.size()>0)
			sax.getDocumentFactory().setXPathNamespaceURIs(namespace);
		try {
			document = sax.read(in);
		} catch (DocumentException e) {
			throw new DocumentException(e);
		}
		return this;
	}
	public String nodeText(String xpath){
		Node node = document.selectSingleNode(xpath);
		if(node==null)
			return "";
		return node.getText();
	}
	public List<String> nodeTexts(String xpath){
		List<String> texts = new ArrayList<>();
		List<?> nodes = document.selectNodes(xpath);
		for(Object obj:nodes){
			if(!(obj instanceof Node))
				continue;
			Node node = (Node) obj;
			texts.add(node.getText());
		}
		return texts;
	}
	public Map<String, String> nodeAtt(String xpath){
		Map<String, String> attrs = nodeAtt(document.selectSingleNode(xpath));
		return attrs;
	}
	public List<Map<String, String>> nodeAtts(String xpath){
		List<?> nodes = document.selectNodes(xpath);
		if(CollectionUtils.isEmpty(nodes))
			return null;
		List<Map<String, String>> attrs = new ArrayList<>();
		for(Object obj:nodes){
			Map<String, String> ele = nodeAtt((Node)obj);
			if(ele!=null)
				attrs.add(ele);
		}
		if(attrs.size()>=0)
			return attrs;
		return null;
	}
	public Map<String, String> nodeAtt(Node node){
		if(node==null)
			return null;
		if(node instanceof Element){
			Map<String, String> attrs = new HashMap<>();
			Element ele = (Element) node;
			Iterator<?> iter = ele.attributeIterator();
			while(iter.hasNext()){
				Object attObj = iter.next();
				if(!(attObj instanceof Attribute))
					continue;
				Attribute att = (Attribute) attObj;
				String attName = att.getName();
				String attValue = att.getText();
				attrs.put(attName, attValue);
			}
			if(attrs.size()>=0)
				return attrs;
		}
		return null;
	}
	public Node node(String xpath){
		return document.selectSingleNode(xpath);
	}
	public List<?> nodes(String xpath){
		return document.selectNodes(xpath);
	}
}
