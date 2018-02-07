/**
 * @author (Averina Ita) (1521105) for CSE 373 Assignment 4, Autumn, 2016.
 * Section BE.
 * This class is used to build a FeatureVector for the purpose of comparing how similar two pictures 
 * are based on their color scheme
 */
public class FeatureVector {

	/**
	 * FeatureVector is a class for storing the results of
	 * counting the occurrences of colors.
	 * <p>
	 * Unlike the hash table, where the information can be
	 * almost anyplace with the array(s) (due to hashing), in the FeatureVector,
	 * the colors are in their numerical order and each count
	 * is in the array position for its color.
	 * <p>
	 * Besides storing the information, the class provides methods
	 * for getting the information (getTheCounts) and for computing
	 * the similarity between two vectors (cosineSimilarity).
	 */
	long[] colorCounts;
	int bitsPerPixel;
	int keySpaceSize;

	/**
	 * Constructor of a FeatureVector.
	 * 
	 * This creates a FeatureVector instance with an array of the
	 * proper size to hold a count for every possible element in the key space.
	 * 
	 * @param bpp	Bits per pixel. This controls the size of the vector.
	 * 				The keySpace Size is 2^k where k is bpp.
	 * 
	 */
	public FeatureVector(int bpp) {
		keySpaceSize = 1 << bpp;
		colorCounts = new long[keySpaceSize];
	}

	public void getTheCounts(ColorHash ch) throws Exception {
		// You should implement this method.
		// It will go through all possible key values in order,
		// get the count from the hash table and put it into this feature vector.
		
		for(int i = 0; i < colorCounts.length; i++) {
			colorCounts[i] = ch.getCount(new ColorKey(i,bitsPerPixel));
		}
	}
	public double cosineSimilarity(FeatureVector other) throws Exception {
		// Implement this method. Use the formula given in the A3 spec,
		// which is also explained at
		// https://en.wikipedia.org/wiki/Cosine_similarity
		// where A is this feature vector and B is the other feature vector.
		// When multiplying in the dot product, convert all the long values
		// to double before doing the multiplication.

		// Hint: you may wish to write some private methods here to help
		// computing the cosine similarity.  For example, it could be
		// nice to have a dot product method and a vector magnitude method.
		if(other.keySpaceSize != keySpaceSize) {
			throw new Exception("Different Dimension!");
		}
		return (dotProduct(other)/Magnitude(other));
	}
	
	// find the dot product of the vectors
	private long dotProduct(FeatureVector other) {
		long sum = 0;
		for(int i = 0; i < colorCounts.length; i++) {
			sum = sum + colorCounts[i] * other.colorCounts[i];
		}
		return sum;
	}
	// find the sum of the magnitude of the vectors
	private double Magnitude(FeatureVector other) {
		long sum1 = 0;
		long sum2 = 0;
		for(int i = 0; i < colorCounts.length; i++) {
			sum1 = sum1 + other.colorCounts[i]*other.colorCounts[i];
			sum2 = sum2 + colorCounts[i] *colorCounts[i];
		}
		return Math.sqrt(sum1)* Math.sqrt(sum2);	
	}

	/**
	 * Optional main method for your own tests of these methods.
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
