package model;
/**
 * 
 * 
 * Process extends MemBlock
 * 
 * 
 * @see <A href="../src/model/Process.java">Java sourceCode</A>
 * 
 * 
 * @author Mike <A href="mailto:mspadaro@student.sjcny.edu">
 *         mspadaro@student.sjcny.edu </A>
 * 
 * @version v1.0, 11/31/2019
 * 
 * 
 */
public class Process extends MemBlock{
	
	
	private int processNumber;
	
	
	

	public Process(int size, int processNumber) {
		super(size);
		this.processNumber = processNumber;
	}


	public int getProcessNumber() {
		return processNumber;
	}


	@Override
	public String toString() {
		return "Process [processNumber=" + processNumber + ", getSize()=" + getSize() + "]";
	}



	
	


}
