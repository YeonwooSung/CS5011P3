import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {
	Element rootElement;

	XMLParser(File fXmlFile) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbf.newDocumentBuilder();
		Document doc = docBuilder.parse(fXmlFile);
		
		//Element.normalize() method is optional, but recommended thing.
		//ref - <http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work>
		doc.getDocumentElement().normalize();

		this.rootElement = doc.getDocumentElement();
	}

	NodeList findNodeListByTagName(String tagName) {
		return rootElement.getElementsByTagName(tagName);
	}
}
