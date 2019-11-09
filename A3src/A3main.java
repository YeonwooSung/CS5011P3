import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class A3main {
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: java A3main [any param]");
		}

		String filePath = args[0];

		try {
			// get the xml file
			File fXmlFile = new File(filePath);

			// check if the xml file exists
			if (!fXmlFile.exists()) {
				System.out.println("Invalid file path - File not exists!");
				System.exit(0);
			}

			XMLParser parser = new XMLParser(fXmlFile);
			BayesianNetworkParser bnParser = new BayesianNetworkParser(parser);

			bnParser.parseVars(); //parse the "VARIABLE" tags
			bnParser.parseDefinitions(); //parse the "DEFINITION" tags

			bnParser.generateGraph();
			ArrayList<BayesianNetworkNode> inferenceList = bnParser.makeInferences();

			for (BayesianNetworkNode node : inferenceList) {
				//TODO
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
