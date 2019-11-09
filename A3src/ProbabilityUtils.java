import java.util.ArrayList;


public class ProbabilityUtils {

	/**
	 * Generate the permutations.
	 *
	 * @param lists - list that contains the lists of all variables' values
	 * @param result - list that we will store the result values
	 * @param depth - depth of the recursion
	 * @param current - current value
	 */
	public static void generatePermutations(ArrayList<ArrayList<Double>> lists, ArrayList<Double> result, int depth, double current) {
		if (depth == lists.size()) {
	        result.add(current);
	        return;
	    }

		ArrayList<Double> list = lists.get(depth);
		int size = list.size();

	    for (int i = 0; i < size; i++) {
	    	int newDepth = depth + 1;
	    	double newCurrent = current * list.get(i);
	        generatePermutations(lists, result, newDepth, newCurrent);
	    }
	}

	public static void addProbabilityValues(ArrayList<Double> values, ArrayList<Double> list, ArrayList<Double> result, int sizeOfOutcomes) {
		double[] vals = new double[list.size() * values.size()];
		int index = 0;

		for (double d1 : list) {
			for (int i = 0; i < sizeOfOutcomes; i++) {
				double d2 = values.get(index);
				double newVal = d1 * d2;
				vals[index] = newVal;
				index += 1;
			}
		}

		for (index = 0; index < sizeOfOutcomes; index++) {
			result.add(vals[index]);
		}

		for (; index < vals.length; index += result.size()) {
			for (int i = 0; i < result.size(); i++) {
				result.set(i, (result.get(i) + vals[index + i]));
			}
		}


		//TODO normalisation
		double total = 0;
		for (double d : result) {
			total += d;
		}

		if (total == 1) return;

		for (int i = 0; i < result.size(); i++) {
			result.set(i, (result.get(i) / total));
		}
	}
}
