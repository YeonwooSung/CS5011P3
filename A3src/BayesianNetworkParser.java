import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class BayesianNetworkParser {
	private XMLParser parser;
	private HashMap<String, BayesianNetworkNode> nodeMap;
	private ArrayList<BayesianNetworkNode> nodeList;

	private final int LOOP_TIME_LIMIT = 1000;
	private final String TAG_NAME_VAR = "VARIABLE";
	private final String TAG_NAME_NAME = "NAME";
	private final String TAG_NAME_OUTCOME = "OUTCOME";
	private final String TAG_NAME_DEF = "DEFINITION";
	private final String TAG_NAME_FOR = "FOR";
	private final String TAG_NAME_GIVEN = "GIVEN";
	private final String TAG_NAME_TABLE = "TABLE";


	BayesianNetworkParser(XMLParser parser) {
		this.parser = parser;
		nodeMap = new HashMap<String, BayesianNetworkNode>();
		nodeList = new ArrayList<BayesianNetworkNode>();
	}

	/**
	 * This method performs the variable elimination for the given node.
	 * @param node - target node
	 * @return If there is no error, returns true. Otherwise, returns false.
	 */
	boolean makeInference(BayesianNetworkNode node) {
		if (node.didInference()) {
			return true;
		}

		ArrayList<String> given = node.getGiven();

		// If the size of given list is 0, then that means that the current node does not have parent node.
		if (given.size() == 0) {
			node.setInference(true);
			return true;
		}

		ArrayList<ArrayList<Double>> lists = new ArrayList<ArrayList<Double>>();

		// use for loops to gets the probability values of parent nodes
		for (String str : given) {
			BayesianNetworkNode givenNode = nodeMap.get(str);

			// check if the parent node did inference or is a root node of the graph.
			if (!givenNode.didInference() && !givenNode.isRootNode()) {
				this.makeInference(givenNode);
				return false;
			}

			lists.add(givenNode.getValues());
		}

		ArrayList<Double> permutations = new ArrayList<Double>();

		ProbabilityUtils.generatePermutations(lists, permutations, 0, 1); // generate the permutations

		ArrayList<Double> result = new ArrayList<Double>();
		ProbabilityUtils.addProbabilityValues(node.getValues(), permutations, result, node.getOutcomeList().size());

		node.setValues(result); //store the result of the inference

		node.setInference(true);
		boolean checker = true;
		ArrayList<BayesianNetworkNode> children = node.getChildren();

		// use for-each loop to iterate children
		for (BayesianNetworkNode child : children) {
			checker = checker && this.makeInference(child);
		}

		return checker;
	}

	ArrayList<BayesianNetworkNode> makeInferences() {
		int count = 0;

		while (true) {
			// check the value of count for time out - to prevent an infinite loop
			if (count == LOOP_TIME_LIMIT) {
				System.out.println("TimeoutError::Time out! Please re-check your bayesian network!");
				System.exit(0);
			}

			boolean checker = true;
			boolean foundRootNode = false;

			for (BayesianNetworkNode node : nodeList) {

				// check if the current node is the root node
				if (node.isRootNode()) {
					foundRootNode = true;
					ArrayList<BayesianNetworkNode> children = node.getChildren();

					// use for loop to iterate child nodes of the current node
					for (BayesianNetworkNode child : children) {
						checker = checker && this.makeInference(child);
					}
				}
			}

			// check if all nodes are inspected and parsed correctly
			if (checker && foundRootNode) break;

			count += 1;
		}

		ArrayList<BayesianNetworkNode> inferenceList = new ArrayList<BayesianNetworkNode>();

		for (BayesianNetworkNode node : nodeList) {
			//TODO
//			System.out.println(node.getName());
//			ArrayList<Double> values = node.getValues();
//			for (double d : values) {
//				System.out.print(d);
//				System.out.print(" ");
//			}
//			System.out.println();
			
			if (node.isLeafNode()) inferenceList.add(node);
		}

		return inferenceList;
	}

	/**
	 * Generate the graph by adding the child nodes.
	 */
	void generateGraph() {
		for (BayesianNetworkNode node : nodeList) {
			ArrayList<String> given = node.getGiven();

			if (given.size() == 0) {
				node.setRoot(true);
			}

			for (String str : given) {
				BayesianNetworkNode parent = nodeMap.get(str);
				parent.addChild(node);
			}
		}

		// use for-each loop to check the number of children.
		for (BayesianNetworkNode node : nodeList) {
			int size = node.getChildren().size();

			// if the node does not have children, then the current node is a leaf node
			if (size == 0) {
				node.setLeafNode(true);
			}
		}
	}

	/**
	 * Parses the "VARIABLE" tags in the xml file.
	 */
	void parseVars() {
		// get the "VARIABLE" nodes
		NodeList vars = parser.findNodeListByTagName(TAG_NAME_VAR);

		// use for loop to iterate the node list
		for (int i = 0; i < vars.getLength(); i++) {
			Element var = (Element) vars.item(i);
			Node nameNode = var.getElementsByTagName(TAG_NAME_NAME).item(0);

			String name = nameNode.getTextContent();
			BayesianNetworkNode node = new BayesianNetworkNode(name);

			ArrayList<String> list = new ArrayList<String>();

			// get the "OUTCOME" nodes
			NodeList outcomeNodes = var.getElementsByTagName(TAG_NAME_OUTCOME);
			int length = outcomeNodes.getLength();

			// use for loop to iterate nodes in the node list
			for (int j = 0; j < length; j++) {
				Node outcomeNode = outcomeNodes.item(j);
				String outcome = outcomeNode.getTextContent();
				list.add(outcome);
			}

			node.setOutcomeList(list);

			nodeMap.put(name, node);
			nodeList.add(node);
		}
	}

	/**
	 * Parses the "DEFINITION" tags in the xml file.
	 */
	void parseDefinitions() throws NumberFormatException {
		// get the "DEFINITION" nodes
		NodeList defs = parser.findNodeListByTagName(TAG_NAME_DEF);

		// use for loop to iterate the node list
		for (int i = 0; i < defs.getLength(); i++) {
			Element def = (Element) defs.item(i);

			String name = def.getElementsByTagName(TAG_NAME_FOR).item(0).getTextContent();

			if (!nodeMap.containsKey(name)) {
				System.out.println("Error::Unexpected name - " + name);
				System.exit(0);
			}

			ArrayList<String> list = new ArrayList<String>();

			// get the "GIVEN" nodes
			NodeList givenNodes = def.getElementsByTagName(TAG_NAME_GIVEN);
			int length = givenNodes.getLength();

			// use for loop to iterate nodes in the node list
			for (int j = 0; j < length; j++) {
				Node givenNode = givenNodes.item(j);
				String given = givenNode.getTextContent();
				list.add(given);
			}

			// get the "TABLE" nodes
			NodeList tableNodes = def.getElementsByTagName(TAG_NAME_TABLE);
			length = tableNodes.getLength();

			ArrayList<Double> valueList = new ArrayList<Double>();
			Node tableNode = tableNodes.item(0);
			String table = tableNode.getTextContent();

			String[] values = table.split(" ");

			// use for loop to iterate nodes in the node list
			for (String value : values) {
				valueList.add(Double.parseDouble(value));
			}

			BayesianNetworkNode node = nodeMap.get(name);
			node.setGiven(list);
			node.setValues(valueList);
		}
	}
}
