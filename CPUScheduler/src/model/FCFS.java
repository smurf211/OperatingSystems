package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * 
 * 
 * The FCFS serve class contains a processbag arraylist to store all processes
 * needed. It also contains all methods necessary for process creation and
 * simulation calculations for the FCFS algorithm
 * 
 * @see <A href="../src/model/FCFS.java">Java sourceCode</A>
 * 
 * 
 * @author Mike <A href="mailto:mspadaro@student.sjcny.edu">
 *         mspadaro@student.sjcny.edu </A>
 * 
 * @version v1.0, 10/31/2019
 * 
 * 
 */

public class FCFS {

	ArrayList<Process> processBag;
	ArrayList<Integer> ganttTimes;
	ArrayList<Integer> burstTimes;
	int size;
	double averageWaitTime;
	double averageTurnAroundTime;
	int currentArrivalTime = 0;
	int burstTimeCounter;
	int previousBurstTime;

	/**
	 * class constructor, creates processBag based on number of processes needed
	 * from GUI.
	 * 
	 * @param size
	 * 
	 * 
	 */

	public FCFS(int size) {
		this.size = size;
		processBag = new ArrayList<Process>();
		ganttTimes = new ArrayList<Integer>();

	}

	/**
	 * creates a process with manual arrival and bursttime
	 * 
	 * @param arrivalTime
	 * @param burstTime
	 * 
	 * @return void
	 */
	public void createProcessManual(int arrivalTime, int burstTime) {

		Process p1 = new Process();
		p1.setArrivalTime(arrivalTime);
		p1.setBurstTime(burstTime);
		processBag.add(p1);
	}

	/**
	 * creates process with manual burstime and autogenerated arrivaltime
	 * 
	 * @param burstTime
	 * @return void
	 */
	public void createProcessManualBurstOnly(int burstTime) {

		burstTimeCounter += burstTime;
		Process p1 = new Process();
		generateArrivalTime();

		while (burstTimeCounter < currentArrivalTime) {

			currentArrivalTime--;

		}

		p1.setArrivalTime(currentArrivalTime);
		p1.setBurstTime(burstTime);

		processBag.add(p1);
	}

	/**
	 * generates a random arrivaltime 1-5
	 * 
	 * @return int
	 */
	public int generateArrivalTime() {
		// arrivals times now will only increase with processNumber logically,
		// as opposed to being generated randomly allows for CPU deadtime.

		Random random = new Random();
		int randomIntArrival = 0;
		while (randomIntArrival == 0) {

			randomIntArrival = random.nextInt(3);
		}
		currentArrivalTime += randomIntArrival;

		return currentArrivalTime;
	}

	/**
	 * generates a random bursttime 1-50
	 * 
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
	 * creates all processes based on burst times from GUI, either random or user
	 * input. Calculates wait and turnaround times. Runs average methods for wait
	 * and TA times.
	 * 
	 * @param burstTimes
	 * @return void
	 */
	public void runProcessesManual(ArrayList<Integer> burstTimes) {
		int totalBurst = 0;
		this.burstTimes = burstTimes;

		createProcessManual(0, burstTimes.get(0));
		previousBurstTime = burstTimes.get(0);

		for (int i = 1; i < size; i++) {

			createProcessManualBurstOnly(burstTimes.get(i));
			previousBurstTime = burstTimes.get(i);
		}

		Collections.sort(processBag);

		int previousBurstTime = 0;
		for (int i = 0; i < processBag.size(); i++) {

			processBag.get(i).setWaitTime(previousBurstTime - processBag.get(i).getArrivalTime());
			previousBurstTime += processBag.get(i).getBurstTime();
		}

		for (int i = 0; i < processBag.size(); i++) {

			processBag.get(i).setTurnAroundTime(processBag.get(i).getWaitTime() + processBag.get(i).getBurstTime());
			totalBurst += processBag.get(i).getBurstTime();
			ganttTimes.add(totalBurst);

		}
		calculateAverageWaitTime();
		calculateAverageTATime();

	}

	/**
	 * sorts processbag by processnumber using Comparator and process number as the
	 * comparable value.
	 * 
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
	 * 
	 * @return void
	 */
	public void calculateAverageWaitTime() {
		double waitTime = 0;
		for (int i = 0; i < processBag.size(); i++) {

			waitTime += processBag.get(i).getWaitTime();
		}

		waitTime = waitTime / processBag.size();

		this.averageWaitTime = Math.round(waitTime * 100.0) / 100.0;
	}

	/**
	 * 
	 * calculates the turnaround time
	 * 
	 * @return void
	 */
	public void calculateAverageTATime() {
		double TATime = 0;
		for (int i = 0; i < processBag.size(); i++) {

			TATime += processBag.get(i).getTurnAroundTime();
		}

		TATime = TATime / processBag.size();

		this.averageTurnAroundTime = Math.round(TATime * 100.0) / 100.0;
	}

	public ArrayList<Process> getProcessBag() {
		return processBag;
	}

	public double getAverageWaitTime() {
		return averageWaitTime;
	}

	public double getAverageTurnAroundTime() {
		return averageTurnAroundTime;
	}

	public ArrayList<Integer> getGanttTimes() {
		return ganttTimes;
	}

}