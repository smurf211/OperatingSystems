package model;
/**
 * 
 * 
 * OSBlock extends MemBlock
 * 
 * 
 * @see <A href="../src/model/OSBlock.java">Java sourceCode</A>
 * 
 * 
 * @author Mike <A href="mailto:mspadaro@student.sjcny.edu">
 *         mspadaro@student.sjcny.edu </A>
 * 
 * @version v1.0, 11/31/2019
 * 
 * 
 */
public class OSBlock extends MemBlock{

	public OSBlock(int size) {
		super(size);
		
	}

	@Override
	public String toString() {
		return "OSBlock [getSize()=" + getSize() + "]";
	}
	
	
	
	

}
