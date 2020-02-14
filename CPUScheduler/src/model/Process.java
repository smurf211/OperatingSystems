package model;

/**
 * 
 * 
 * The process class is base class required for this application. All further classes require this class to create and modify processes.
 * contains all required fields for each algorithm.
 * 
 * @see <A href="../src/model/Process.java">Java
 *      sourceCode</A>
 * 
 * 
 * @author Mike Spadaro <A href="mailto:mspadaro@student.sjcny.edu">
 *         mspadaro@student.sjcny.edu </A>
 * 
 * @version v1.0, 10/31/2019
 * 
 * 
 */

public class Process implements Comparable<Process>{
	Integer burstTime;
	int waitTime;
	Integer arrivalTime = 0;
	int turnAroundTime;
	Integer priority;
	Integer processNumber;
	int quantum;
	
	private static int processNumberCounter = 0;
	
	/**
	 * class constructor, creates a process and increments STATIC process counter.
	 * @param size
	 * 
	 * 
	 */
	
	public Process() {
		processNumber= ++processNumberCounter;
		
		
	}
	
	
	public void print(String string) {
		
		System.out.println(string);
	}


	public Integer getBurstTime() {
		return burstTime;
	}


	public void setBurstTime(int burstTime) {
		this.burstTime = burstTime;
	}


	public int getWaitTime() {
		return waitTime;
	}


	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}


	public Integer getArrivalTime() {
		return arrivalTime;
	}


	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}


	public int getTurnAroundTime() {
		return turnAroundTime;
	}


	public void setTurnAroundTime(int turnAroundTime) {
		this.turnAroundTime = turnAroundTime;
	}


	public Integer getPriority() {
		return priority;
	}


	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	/**
	 * compareTo method allows the List.sort function to sort by a custom field, in this case arrival time.
	 * @param Process p
	 * @return int
	 * 
	 */
	
    @Override
    public int compareTo(Process p) {
        return this.getArrivalTime().compareTo(p.getArrivalTime());
    }


	@Override
	public String toString() {
		return "Process [burstTime=" + burstTime + ", waitTime=" + waitTime + ", arrivalTime=" + arrivalTime
				+ ", turnAroundTime=" + turnAroundTime + ", pNumber=" + processNumber+ "]";
	}


	public Integer getProcessNumber() {
		return processNumber;
	}


	public static void setProcessNumberCounter(int processNumberCounter) {
		Process.processNumberCounter = processNumberCounter;
	}


	public int getQuantum() {
		return quantum;
	}


	public void setQuantum(int quantum) {
		this.quantum = quantum;
	}
	
	



	
    
	
	

}
