/**
 * @author (Averina Ita) (1521105) for CSE 373 Assignment 4, Autumn, 2016.
 * Section BE.
 * This class is used to compare pictures and produce reports based on it
 */
public class ComparePaintings {

	public ComparePaintings(){}; // constructor.
	private int bpp; // bits per pixel
	String collisionMethod; // preferred collision method
	private int collision; // count the no of collisions
	// Load the image, construct the hash table, count the colors.
	ColorHash countColors(String filename, int bitsPerPixel) throws Exception {
		// Implement this.
		ImageLoader picture = new ImageLoader(filename);
		bpp = bitsPerPixel;
		ColorHash hashtable = new ColorHash(3, bpp, collisionMethod, 0.5);
		collision = 0;
		// goes through each pixels and get the colorKey for each of them
		for(int i = 0; i < picture.getWidth(); i++) { 
			for(int j = 0; j < picture.getHeight();j++) {
				ColorKey key = picture.getColorKey(i, j, bitsPerPixel);
				ResponseItem res = hashtable.increment(key);
				collision = collision + res.nCollisions; // count collisions
			}
		}		
		return hashtable; 
	}

//	Starting with two hash tables of color counts, compute a measure of similarity based on the cosine distance of two vectors.
	double compare(ColorHash painting1, ColorHash painting2) throws Exception {
		// Implement this.
		FeatureVector vector1 = new FeatureVector(painting1.bpp);
		FeatureVector vector2 = new FeatureVector(painting2.bpp);
		vector1.getTheCounts(painting1); // get the key values from hashtable
		vector2.getTheCounts(painting2); // get the key values from hashtable
		return vector1.cosineSimilarity(vector2); 
	}

	//	A basic test for the compare method: S(x,x) should be 1.0, so you should compute the similarity of an image with itself and print out the answer. If it comes out to be 1.0, that is a good sign for your implementation so far.
	void basicTest(String filename) throws Exception {
		collisionMethod = "linear probing";
		ColorHash picture1 = countColors(filename, 3); // get the key values from hashtable
		ColorHash picture2 = countColors(filename, 3); // get the key values from hashtable
		System.out.println("Basic Test Results:" + compare(picture1, picture2));
	}

	//	Using the three given painting images and a variety of bits-per-pixel values, compute and print out a table of collision counts in the following format:
	void CollisionTests() throws Exception {
		// output the table header	
		System.out.println("Bits Per Pixel   C(Mona,linear)  C(Mona,quadratic)  C(Starry,linear) C(Starry,quadratic) C(Christina,linear) C(Christina,quadratic)");
		int i = 24;
		while(i >= 3){ // output collision report for each bpp
			// count collisions for linear probing
			collisionMethod = "linear probing";
			countColors("MonaLisa.jpg", i);
			int collisionMonaLin = collision;
			countColors("StarryNight.jpg", i);
			int collisionStarryLin = collision;
			countColors("ChristinasWorld.jpg", i);
			int collisionChrisLin = collision;
			
			// count collisions for quadratic probing
			collisionMethod = "quadratic probing";
			countColors("MonaLisa.jpg", i);
			int collisionMonaQua = collision;
			countColors("StarryNight.jpg", i);
			int collisionStarryQua = collision;
			countColors("ChristinasWorld.jpg", i);
			int collisionChrisQua = collision;
			
			// output the result
			System.out.print(i + "			" + collisionMonaLin + "		" + collisionMonaQua + "		  " + collisionStarryLin 
			+ "		  " + collisionStarryQua);
			if(i <= 15) {
				System.out.print("			  " + collisionChrisLin + "		   " +  collisionChrisQua);
			} else {
				System.out.print("		  " + collisionChrisLin + "		   " +  collisionChrisQua);
			}
			System.out.println();
			i = i - 3;
		}
	}
		
// This simply checks that the images can be loaded, so you don't have 
// an issue with missing files or bad paths.
	void imageLoadingTest() {
		ImageLoader mona = new ImageLoader("MonaLisa.jpg");
		ImageLoader starry = new ImageLoader("StarryNight.jpg");
		ImageLoader christina = new ImageLoader("ChristinasWorld.jpg");
		System.out.println("It looks like we have successfully loaded all three test images.");
	}
	
	// testing for cosine values
	void fullSimilarityTests() throws Exception {
		//output table header
		System.out.println("Bits Per Pixel       S(Mona,Starry)    S(Mona,Christina)     S(Starry,Christina)");
		int i = 24;
		while(i >= 3) {
			collisionMethod = "quadratic probing";
			// build hashtable for each image
			ColorHash mona = countColors("MonaLisa.jpg", i); 
			ColorHash starry = countColors("StarryNight.jpg", i);
			ColorHash chris = countColors("ChristinasWorld.jpg", i);
			// calculate cosine values and round it five decimal places
			double compareMona = Math.round(compare(mona,starry)*100000.0)/100000.0;
			double compareStarry = Math.round(compare(mona,chris)*100000.0)/100000.0;
			double compareChris = Math.round(compare(starry,chris)*100000.0)/100000.0;
			// print out the results
			System.out.println(i + "       		" + compareMona + "       	" + compareStarry+ "       	       " + compareChris);
			i -= 3;
		}
	}
	/**
	 * This is a basic testing function, and can be changed.
	 * @throws Exception 
	 */
	public static void main(String[] args)  {
		ComparePaintings cp = new ComparePaintings();
		cp.imageLoadingTest(); 
		try {
			cp.CollisionTests(); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			cp.fullSimilarityTests();
			cp.basicTest("MonaLisa.jpg");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
