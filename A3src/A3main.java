import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * The main class.
 * @author 160021429
 */
public class A3main {
	public static void main(String[] args) {
		String filePath = null;

		// check the number of command line arguments
		if (args.length < 1) {
			filePath = "bn1.xml";
		} else {
			filePath = args[0];
		}

		// validate the file name
		if (!filePath.endsWith(".xml")) {
			System.out.println("Usage: java A3main <file_path_of_bn>");
			System.out.println("Please recheck the file name!");
			System.exit(0);
		}

		try {
			// get the xml file
			File fXmlFile = new File(filePath);

			// check if the xml file exists
			if (!fXmlFile.exists()) {
				System.out.println("Invalid file path - File not exists!");
				System.out.println("Please recheck the file path!");
				System.exit(0);
			}

			XMLParser parser = new XMLParser(fXmlFile);
			BayesianNetworkParser bnParser = new BayesianNetworkParser(parser);

			bnParser.parseVars(); //parse the "VARIABLE" tags
			bnParser.parseDefinitions(); //parse the "DEFINITION" tags

			bnParser.generateGraph(); //generate graph for bayesian network
			bnParser.makeInferences(); //make inference with the variable elimination algorithm

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
