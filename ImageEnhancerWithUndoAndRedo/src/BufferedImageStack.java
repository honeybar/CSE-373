/* BufferedImageStack.java
 * by Averina Ita (1521105) for CSE 373 Assignment 1, Autumn, 2016.
 * Section BE.
 * This class is used to store a collection of BufferedImage in form of a stack*/

import java.awt.image.BufferedImage;
import java.util.*;
public class BufferedImageStack {
	private BufferedImage [] stackofImage; // declare array to store BufferedImage
	private int counter; // indicate the current position in the array and the no of BufferedImage
	public BufferedImageStack () {
		stackofImage = new BufferedImage[2]; // Initialize the stackofImage with size of 2
		counter = 0; // initialize counter
	}
	
	public void push (BufferedImage someBufferedImage) { // add Bufferedimage to the stack
		if(counter >= getArraySize()) { // if counter is equal to or greater than the current size array...
			doubleArraySize(); // double the array size
		}
		stackofImage[counter] = someBufferedImage; // insert the bufferedimage
		counter++; 
	}
	
	public BufferedImage pop () { // delete the current bufferedImage
		if(isEmpty()){
			throw new EmptyStackException(); // check if array is empty
		} 
		--counter; 
		BufferedImage popped = stackofImage[counter]; // store the bufferedimage that is going to be deleted
		stackofImage[counter] = null; // set the position to null
		return popped; // return the deleted bufferedImage
	}
	
	public void empty(){ // empty the stack
		for(int i = 0; i < stackofImage.length; i++){ 
			stackofImage[i] = null; // set every element to null
		}
		counter = 0; // set counter back to 0
	}
	public boolean isEmpty () { // check if the stack is empty
		if(counter <= 0) {
			return true; 
		}
		return false;
	}
	
	public BufferedImage get(int index){ // get the buferedImage at the index position
		if(index >= counter || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		return stackofImage[index];
	}
	
	public int getSize() { // get the number of BufferedImage in the stack
		return counter;
	}
	
	public int getArraySize() { // get the array size
		return stackofImage.length;
	}
	
	public void doubleArraySize() { // double array size
		BufferedImage[] tempStacks = new BufferedImage[getArraySize()*2]; // create new array with double size
		for(int i = 0; i < stackofImage.length; i++) {
			tempStacks[i] = stackofImage[i]; // copy the old array to tempStacks
		}
		stackofImage = tempStacks; // Assign the new array to stackofImage
	}
	
}