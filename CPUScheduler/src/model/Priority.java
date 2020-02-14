package model;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;



/**
 * 
 * 
 * The priority class contains a processbag arraylist to store all processes needed.
 *  It also contains all methods necessary for process creation and simulation calculations for the priority algorithm
 *  this class requires additional fields of priority in order to run.
 * 
 * @see <A href="../src/model/Priority.java">Java
 *      sourceCode</A>
 * 
 * 
 * @author Mike <A href="mailto:mspadaro@student.sjcny.edu">
 *         mspadaro@student.sjcny.edu </A>
 * 
 * @version v1.0, 10/31/2019
 * 
 * 
 */

public class Priority {
	
	
	
	ArrayList<Process> processBag;
	int size;
	double averageWaitTime;
	double averageTurnAroundTime;
	int previousBurstTime;
	
	/**
	 *class constructor, creates processBag based on number of processes needed from GUI.
	 * @param size
	 * 
	 * 
	 */

	public Priority(int size) {
		this.size = size;
		processBag = new ArrayList<Process>();

	}
	

	
	/**
	 * creates a process with manual burst time and priority
	 * @param burstTime
	 * @param priority
	 * 
	 * @return  void
	 */	

	public void createProcessManualBurstPriorityOnly(int burstTime, int priority) {

		Process p1 = new Process();

		p1.setBurstTime(burstTime);
		p1.setPriority(priority);

		processBag.add(p1);
	}

	/**
	 * generates a random bursttime 1-50
	 * @return int
	 */
	public static int generateBurstTime() {

		Random random = new Random();
		int randomIntBurst = 0;

		while (randomIntBurst == 0) {
			randomIntBurst = random.nextInt(50);

		}
		return randomIntBurst;
	}
	
	/**
	 * creates all processes from burst times from GUI, either random or user input. Sorts processes 
	 * by priority also taken from GUI. Calculates wait time and turnaround time.  Runs average methods for wait and TA times.
	 * @param ArrayList<Integer> burstTimes, ArrayList<Integer> priorities
	 * @return void
	 */

	public void runProcessesManual(ArrayList<Integer> burstTimes, ArrayList<Integer> priorities) {

		for (int i = 0; i < size; i++) {

			createProcessManualBurstPriorityOnly(burstTimes.get(i), priorities.get(i));

		}

		sortByPriority();

		int previousBurstTime = 0;
		for (int i = 0; i < processBag.size(); i++) {

			processBag.get(i).setWaitTime(previousBurstTime );
			previousBurstTime += processBag.get(i).getBurstTime();
		}

		for (int i = 0; i < processBag.size(); i++) {

			processBag.get(i).setTurnAroundTime(processBag.get(i).getWaitTime() + processBag.get(i).getBurstTime());

		}
		calculateAverageWaitTime();
		calculateAverageTATime();
		
		sortByProcessNumber();

		//System.out.println(processBag + "\n" + averageWaitTime + " " + averageTurnAroundTime);

	}	
	
	/**
	 * sorts processbag by processnumber using Comparator and process number as the comparable value.
	 * @return void
	 */

	public void sortByProcessNumber() {

		Collections.sort(processBag, new Comparator<Process>() {
			@Override
			public int compare(Process u1, Process u2) {
				return u1.getProcessNumber().compareTo(u2.getProcessNumber());
			}
		});
	}
	
	/**
	 * 
	 * calculates the average wait time
	 * @return void
	 */

	public void calculateAverageWaitTime() {
		double waitTime = 0;
		for (int i = 0; i < processBag.size(); i++) {

			waitTime += processBag.get(i).getWaitTime();
		}

		waitTime = waitTime / processBag.size();

		this.averageWaitTime = Math.round(waitTime*100.0) /100.0;
	}

	/**
	 * 
	 * calculates the turnaround time
	 * @return void
	 */
	public void calculateAverageTATime() {
		double TATime = 0;
		for (int i = 0; i < processBag.size(); i++) {

			TATime += processBag.get(i).getTurnAroundTime();
		}

		TATime = TATime / processBag.size();

		this.averageTurnAroundTime = Math.round(TATime*100.0) /100.0;
	}

	public ArrayList<Process> getProcessBag() {
		return processBag;
	}
	
	/**
	 * sorts processbag by priority using Comparator and priority number as the comparable value.
	 * @return void
	 */

	
	public void sortByPriority() {

		Collections.sort(processBag, new Comparator<Process>() {
			@Override
			public int compare(Process u1, Process u2) {
				return u1.getPriority().compareTo(u2.getPriority());
			}
		});
	}

	public double getAverageWaitTime() {
		return averageWaitTime;
	}

	public double getAverageTurnAroundTime() {
		return averageTurnAroundTime;
	}
	
	


}
