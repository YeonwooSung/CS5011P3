import java.util.ArrayList;


public class BayesianNetworkNode {
	private boolean inference;
	private boolean leafNode;
	private boolean root;
	private String name;
	private ArrayList<String> given;
	private ArrayList<String> outcomeList;
	private ArrayList<Double> values;
	private ArrayList<BayesianNetworkNode> children;

	BayesianNetworkNode(String name) {
		this.name = name;
		this.children = new ArrayList<BayesianNetworkNode>();

		inference = false;
		root = false;
	}

	BayesianNetworkNode(String name, ArrayList<String> given) {
		this.name = name;
		this.given = given;
		this.children = new ArrayList<BayesianNetworkNode>();

		inference = false;
		root = false;
	}

	/**
	 * Add child node to the list.
	 * @param node - the child node
	 */
	public void addChild(BayesianNetworkNode node) {
		children.add(node);
	}

	/**
	 * Getter for children.
	 * @return children
	 */
	public ArrayList<BayesianNetworkNode> getChildren() {
		return children;
	}

	/**
	 * Getter for inference.
	 * @return inference
	 */
	public boolean didInference() {
		return inference;
	}

	/**
	 * Setter for inference.
	 * @param inference
	 */
	public void setInference(boolean inference) {
		this.inference = inference;
	}

	/**
	 * Setter for values.
	 * @param values
	 */
	public void setValues(ArrayList<Double> values) {
		this.values = values;
	}

	/**
	 * Setter for leafNode.
	 * @param leafNode
	 */
	public void setLeafNode(boolean leafNode) {
		this.leafNode = leafNode;
	}

	/**
	 * Setter for root.
	 * @param root
	 */
	public void setRoot(boolean root) {
		this.root = root;
	}

	/**
	 * Setter for given.
	 * @param given
	 */
	public void setGiven(ArrayList<String> given) {
		this.given = given;
	}

	/**
	 * Setter for outcomeList.
	 * @param outcomeList
	 */
	public void setOutcomeList(ArrayList<String> outcomeList) {
		this.outcomeList = outcomeList;
	}

	/**
	 * Getter for name.
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for leafNode.
	 * @return leafNode
	 */
	public boolean isLeafNode() {
		return leafNode;
	}

	/**
	 * Check if this node is a root node.
	 * @return root
	 */
	public boolean isRootNode() {
		return root;
	}

	/**
	 * Getter for given.
	 * @return given
	 */
	public ArrayList<String> getGiven() {
		return given;
	}

	/**
	 * Getter for outcomeList.
	 * @return outcomeList
	 */
	public ArrayList<String> getOutcomeList() {
		return outcomeList;
	}

	/**
	 * Getter for values.
	 * @return values
	 */
	public ArrayList<Double> getValues() {
		return values;
	}
}
