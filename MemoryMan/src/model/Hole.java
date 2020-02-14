package model;
/**
 * 
 * 
 * Hole extends MemBlock
 * 
 * 
 * @see <A href="../src/model/Hole.java">Java sourceCode</A>
 * 
 * 
 * @author Mike <A href="mailto:mspadaro@student.sjcny.edu">
 *         mspadaro@student.sjcny.edu </A>
 * 
 * @version v1.0, 11/31/2019
 * 
 * 
 */
public class Hole extends MemBlock {
	
	

	public Hole(int size) {
		super(size);
		
	}

	@Override
	public String toString() {
		return "Hole [getSize()=" + getSize() + "]";
	}
	
	
	

}
