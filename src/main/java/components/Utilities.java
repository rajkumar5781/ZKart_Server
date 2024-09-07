package components;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Utilities {
	public static Boolean isValuePresent(String value) {
		if(value!=null && !value.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public static void fileReader(String path) {
		
	}
	public static Document getDocument(String documentPath) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		return documentBuilder.parse(new File(documentPath));
	}
	
	public static Transformer loadTransformars() {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		 Transformer transformer = null;
		 try {
		transformer = transformerFactory.newTransformer();
		} catch (Exception e) {
			// TODO: handle exception
		}
		 return transformer;
	}
}
