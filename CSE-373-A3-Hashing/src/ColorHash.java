/**
 * @author (Averina Ita) (1521105) for CSE 373 Assignment 3, Autumn, 2016.
 * Section BE.
 * This class is used to build a hash table to store colorKeys along with the corresponding values
 */

public class ColorHash {
	// Implement this class, including whatever data members you need and all
	// of the public methods below.  You may create any number of private methods
	// if you find them to be helpful.
	private ColorKey[] colorKeysCol; // hashtable for keys
	private long[] valuesCol; // hashtable to store values
	private int counter; // count the no of items in the table
	private String collisionMethod; // store the preferred collision method
	private double loadFactor; // the limit capacity of the table 
	private int collision; // count the no of collision occur during finding/insertion
	int bpp; // store the image's bits per pixel

	public ColorHash(int tableSize, int bitsPerPixel, String collisionResolutionMethod, double rehashLoadFactor) throws Exception{
		colorKeysCol = new ColorKey[tableSize]; 
		valuesCol = new long[tableSize];  
		counter = 0; 
		collisionMethod = collisionResolutionMethod; 
		loadFactor = rehashLoadFactor; 
		bpp = bitsPerPixel; 
		// check if load factor is within the range for the preferred probing method. throws exception if it doesn't
		if((loadFactor > 1 && collisionMethod.toLowerCase().equals("linear probing")) || 
		 (loadFactor > 0.5 && collisionMethod.toLowerCase().equals("quadratic probing"))) {
			throw new Exception("loadFactor exceeds acceptable range");
		}
		// check if the tablesize is prime for quadratic probing. throws exception if it doesn't
		/*if(collisionMethod.toLowerCase().equals("quadratic probing") && !IsPrime.IsPrime(tableSize)) {
			throw new Exception("table size must be prime for quadratic probing!");
		}*/
		// check if user input the valid probing method
		if(!collisionMethod.toLowerCase().equals("quadratic probing") && !collisionMethod.toLowerCase().equals("linear probing")) {
			throw new Exception("invalid probing method!");
		}
	}
	// find key and insert the associated value, or if it doesn't exists, insert new key and 
	// the value associated with it. Use probing in case of collision. 
	// Return ResponseItem upon calling
	public ResponseItem colorHashPut(ColorKey key, long value){
		int assign = key.hashCode()%colorKeysCol.length;
		collision = 0;
		
		// check if assigned tableindex is empty and capacity does not exceeds load factor
		if(colorKeysCol[assign] == null && (counter+1.0)/colorKeysCol.length < loadFactor){
			counter++;
			colorKeysCol[assign] = key;
			valuesCol[assign] = value;
			return new ResponseItem(value,collision,false,false);
		}
		
		// check if assigned tableindex contains the equivalent current key
		if(key.equals(colorKeysCol[assign])) { 
			valuesCol[assign] = value;
			return new ResponseItem(value,collision,false,true);
		}

		// solve collision and find the key
		if(collisionMethod.toLowerCase().equals("linear probing")) { // use linear probing to solve collision
			return solveLinearCollision(key, value);
		} else { // use quadratic probing to solve collision
			return solveQuadraticCollision(key, value);
		}		
	}

	
	// find key and increment the value by one. If key not found, insert new key and put 1 as the value
	// Use probing in case of collision. Return ResponseItem upon calling
	public ResponseItem increment(ColorKey key){
		int assign = key.hashCode()%colorKeysCol.length;
		collision = 0;

		// check if assigned tableindex contains the equivalent current key and increment the value by 1
		if(key.equals(colorKeysCol[assign])) { 
			valuesCol[assign]++; 
			return new ResponseItem(valuesCol[assign],collision,false,true);
		}
		
		// check if assigned tableindex is empty and capacity does not exceeds load factor
		// if it is the case assign value as 1
		if(colorKeysCol[assign] == null && (counter+1.0)/colorKeysCol.length < loadFactor){
			counter++;
			colorKeysCol[assign] = key;
			valuesCol[assign] = 1;
			return new ResponseItem(1,collision,false,false);
		}
		
		// solve collision and find the key
		if(collisionMethod.toLowerCase().equals("linear probing")) { // use linear probing to solve collision
			return solveLinearCollision(key, -1);
		} else { // use quadratic probing to solve collision
			return solveQuadraticCollision(key, -1);
		}
	}
	
	// find the key and return the ResponseItem. throws exception if not found
	public ResponseItem colorHashGet(ColorKey key)  throws Exception{
		    int assign = key.hashCode()%colorKeysCol.length;
		    // check if key at the assign value is equal to the key
		    if(key.equals(colorKeysCol[assign])) { 
				return new ResponseItem(valuesCol[assign],0,false,false);
			}
			int i = 1;
	    	collision = 1; 
	    	// use linear probing to find the key
		    if(collisionMethod.toLowerCase().equals("linear probing")) {
		    	while( colorKeysCol[assign] != null) {
					 assign = (key.hashCode()+i)% colorKeysCol.length; // Calculate the index position
					 if(key.equals(colorKeysCol[assign]) ) { // check if key at the assign value is equal to the key
							return new ResponseItem(valuesCol[assign],collision,false,false);
					 }
					 i++;
					 collision++;
				}
			} else { // use quadratic probing to find the key
		    	while( colorKeysCol[assign] != null ) {
					 assign = (key.hashCode()+i*i)% colorKeysCol.length; // Calculate the index position
					 if(key.equals(colorKeysCol[assign]) ) { // check if key at the assign value is equal to the key
							return new ResponseItem(valuesCol[assign],collision,false,false);
					 }
					 i++;
					 collision++;
				}
			}
		    collision--;
		    throw new Exception("MissingKeyException");
	}
	
	// find the key and return the associated value with it. If not found, return 0
	public long getCount(ColorKey key){
		int assign = key.hashCode()%colorKeysCol.length;
		  // check if key at the assign value is equal to the key
		    if(key.equals(colorKeysCol[assign])) { 
				return valuesCol[assign];
			}
			int i = 1;
	    	collision = 1; 
	    	// use linear probing to find the key
		    if(collisionMethod.toLowerCase().equals("linear probing")) {
		    	while( colorKeysCol[assign] != null) {
					 assign = (key.hashCode()+i)% colorKeysCol.length; // Calculate the index position
					 if(key.equals(colorKeysCol[assign])) { // check if key at the assign value is equal to the key
						return valuesCol[assign];
					 }
					 i++;
					 collision++;
				}
			} else { // use quadratic probing to find the key
		    	while( colorKeysCol[assign] != null && i <= colorKeysCol.length) {
					 assign = (key.hashCode()+i*i)% colorKeysCol.length; // Calculate the index position
					 if(key.equals(colorKeysCol[assign]) ) { // check if key at the assign value is equal to the key
						return valuesCol[assign];
					 }
					 i++;
					 collision++;
				}
			}
		    collision--;
	    return 0;
	}
	
	// return the key at the tableIndex
	public ColorKey getKeyAt(int tableIndex){
		return colorKeysCol[tableIndex];
	}
	
	// return the value at the tableIndex
	public long getValueAt(int tableIndex){
		return valuesCol[tableIndex];
	}
	
	// return the loadFactor
	public double getLoadFactor(){
		return loadFactor;
	}
	
	// return the tablesize
	public int getTableSize(){
		return colorKeysCol.length ;
	}
	
	// resize the hash table
	public void resize(){
		int j = colorKeysCol.length*2; 
		while (!IsPrime.IsPrime(j)) { // find a prime number for the new table size
			j++;
		}
		ColorKey[] tempkey = new ColorKey[j]; // create temporary hash table for the keys
		long[] tempvalues = new long[j]; // create temporary hash table for the values
		
		for(int i = 0; i < colorKeysCol.length; i++ ){ 
			if(colorKeysCol[i] != null) { // check if key exists in the current position
				int assign = colorKeysCol[i].hashCode()%tempkey.length; // calculate the assign value using hashcode
				if(tempkey[assign] == null) { // check if the current position in new table is empty
					tempkey[assign] = colorKeysCol[i];
					tempvalues[assign] = valuesCol[i];
				} else { // if collide...
					if(collisionMethod.toLowerCase().equals("linear probing")) {  // use linear probing to find next available space
						findSpaceLinear(colorKeysCol[i], valuesCol[i], tempkey,tempvalues);
					} else { // use quadratic probing to find next available space
						findSpaceQuadratic(colorKeysCol[i], valuesCol[i], tempkey,tempvalues);
					}
				}
			}
		}
		colorKeysCol = tempkey;
		valuesCol = tempvalues;
	}
	// find nearest empty space using linear probing
	private void findSpaceLinear(ColorKey key, long value, ColorKey[] keys, long[] values){
		int assign = key.hashCode()%keys.length;
		int x = 1;
		while( keys[assign] != null ) { // find a position that is empty
			 assign = (key.hashCode()+x)% keys.length;
			 x++;
			 collision++;
		}
		keys[assign] = key;
		values[assign] = value;
	}
	
	// find nearest empty space using quadratic probing
	private void findSpaceQuadratic(ColorKey key, long value, ColorKey[] keys, long[] values){
		int assign = key.hashCode()%keys.length;
		int x = 1;
		while( keys[assign] != null ) { // find a position that is empty
			 assign = (key.hashCode()+x*x)% keys.length;
			 x++;
			 collision++;
		}
		keys[assign] = key; 
		values[assign] = value;
	}
	
	// find if key exists in the table using linear probe and if it isn't, find new space and insert the key
	// if value < 0, it indicates that increment is the function that calls this function,
	// hence value is incremented instead of being overwritten with new value
	private ResponseItem solveLinearCollision(ColorKey key, long value){
		int assign = key.hashCode()%colorKeysCol.length;
		collision = 1;
		int x = 1;
		while( colorKeysCol[assign] != null ) { // find key until found or stop when empty space is found
			 assign = (key.hashCode()+x)% colorKeysCol.length;
			 if(key.equals(colorKeysCol[assign])) {
					if(valuesCol[assign] < 0) { //check if value come from increment function
						valuesCol[assign]++;
						return new ResponseItem(valuesCol[assign],collision,false,true);
					} else {
						valuesCol[assign] = value;
						return new ResponseItem(value,collision,false,true);
					}
				 }
			 x++;
			 collision++;
		 }
		 collision--;
		 counter++;
		 if(value < 0) { //check if value come from increment function
			 value = 1;
		 }
		 if(counter+0.0/colorKeysCol.length >= loadFactor ) {  // check if capacity exceeds load factor
			resize();
			assign = key.hashCode()%colorKeysCol.length; // recalculate the assigned key after resizing the table
			if(colorKeysCol[assign] != null) { // if new assigned position is not empty...
				 findSpaceLinear(key, value, colorKeysCol,valuesCol); // find new space
				 return new ResponseItem(value,collision,true,false);
			}
			// if new assigned position is empty...
			colorKeysCol[assign] = key;
			valuesCol[assign] = value;
			return new ResponseItem(value,collision,true,false);
		 }
		// if empty slot is found in the old table and capacity does not exceed loadfactor...
		 colorKeysCol[assign] = key;
		 valuesCol[assign] = value;
		 return new ResponseItem(value,collision,false,false);
	}
	
	// find if key exists in the table using Quadratic probe and if it isn't, find new space and insert the key
	// if value < 0, it indicates that increment is the function that calls this function,
	// hence value is incremented instead of being overwritten with new value
	private ResponseItem solveQuadraticCollision(ColorKey key, long value){
		int assign = key.hashCode()%colorKeysCol.length;
		int i = 1;
		collision = 1;
		while( colorKeysCol[assign] != null ) {
			 assign = (key.hashCode()+i*i)% colorKeysCol.length; 
			 if(key.equals(colorKeysCol[assign])) {
				if(valuesCol[assign] < 0) { //check if value come from increment function
					valuesCol[assign]++;
					return new ResponseItem(valuesCol[assign],collision,false,true);
				} else {
					valuesCol[assign] = value;
					return new ResponseItem(value,collision,false,true);
				}
			 }
			 i++;
			 collision++;
		 }
		 collision--;
		 counter++;
		 if(value < 0 ) { //check if value come from increment function
			 value = 1;
		 }
		 if((counter+0.0)/colorKeysCol.length >= loadFactor ) {  // check if capacity exceeds load factor
			resize();
			assign = key.hashCode()%colorKeysCol.length; // recalculate the assigned key after resizing the table
			if(colorKeysCol[assign] != null) { // if new assigned value is not empty...
				 findSpaceQuadratic(key, value, colorKeysCol,valuesCol); // find new space
				 return new ResponseItem(value,collision,true,false);
			}
			// if new assigned value is empty...
			colorKeysCol[assign] = key;
			valuesCol[assign] = value;
			return new ResponseItem(value,collision,true,false);
		 }
		 // if empty slot is found in the old table and capacity does not exceed loadfactor...
		 colorKeysCol[assign] = key;
		 valuesCol[assign] = value;
		 return new ResponseItem(value,collision,false,false);
	}
}
