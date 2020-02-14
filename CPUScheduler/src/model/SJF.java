package model;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * 
 * 
 * The SJF (Shortest Job First) class contains a processbag arraylist to store
 * all processes needed. It also contains all methods necessary for process
 * creation and simulation calculations for the SJF algorithm.
 * 
 * 
 * 
 * @see <A href="../src/model/SJF.java">Java sourceCode</A>
 * 
 * 
 * @author Mike Spadaro <A href="mailto:mspadaro@student.sjcny.edu">
 *         mspadaro@student.sjcny.edu </A>
 * 
 * @version v1.0, 10/31/2019
 * 
 * 
 */
public class SJF {

	ArrayList<Process> processBag;
	int size;
	double averageWaitTime;
	double averageTurnAroundTime;
	int previousBurstTime;

	/**
	 * class constructor, creates processBag based on number of processes needed
	 * from GUI.
	 * 
	 * @param size
	 * 
	 * 
	 */

	public SJF(int size) {
		this.size = size;
		processBag = new ArrayList<Process>();

	}

	/**
	 * creates a process with manual burst time
	 * 
	 * @param burstTime
	 * @param priority
	 * 
	 * @return void
	 */

	public void createProcessManualBurstOnly(int burstTime) {

		Process p1 = new Process();

		p1.setBurstTime(burstTime);

		processBag.add(p1);
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
	 * creates all processes from burst times from GUI, either random or user input.
	 * Sorts processes by job length. Calculates wait time and turnaround time. Runs
	 * average methods for wait and TA times.
	 * 
	 * @param ArrayList<Integer>
	 *            burstTimes
	 * @return void
	 */

	public void runProcessesManual(ArrayList<Integer> burstTimes) {

		for (int i = 0; i < size; i++) {

			createProcessManualBurstOnly(burstTimes.get(i));

		}

		sortByJobLength();

		int previousBurstTime = 0;
		for (int i = 0; i < processBag.size(); i++) {

			processBag.get(i).setWaitTime(previousBurstTime);
			previousBurstTime += processBag.get(i).getBurstTime();
		}

		for (int i = 0; i < processBag.size(); i++) {

			processBag.get(i).setTurnAroundTime(processBag.get(i).getWaitTime() + processBag.get(i).getBurstTime());

		}
		calculateAverageWaitTime();
		calculateAverageTATime();

		sortByProcessNumber();

		

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

	/**
	 * sorts processbag by job length (burst time) using Comparator and burst time
	 * as the comparable value.
	 * 
	 * @return void
	 */

	public void sortByJobLength() {

		Collections.sort(processBag, new Comparator<Process>() {
			@Override
			public int compare(Process u1, Process u2) {
				return u1.getBurstTime().compareTo(u2.getBurstTime());
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
