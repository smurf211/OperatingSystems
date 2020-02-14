package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class RoundRobin {
	
	ArrayList<Process> processBag;
	int size;
	double averageWaitTime;
	double averageTurnAroundTime;
	int previousBurstTime;
	int quantum;

	public RoundRobin(int size, int quantum) {
		this.size = size;
		processBag = new ArrayList<Process>();

	}

	public void createProcessManual(int burstTime) {

		Process p1 = new Process();
		p1.setBurstTime(burstTime);
		
		processBag.add(p1);
	}


	public static int generateBurstTime() {

		Random random = new Random();
		int randomIntBurst = 0;

		while (randomIntBurst == 0) {
			randomIntBurst = random.nextInt(50);

		}
		return randomIntBurst;
	}

	public void runProcessesManual(ArrayList<Integer> burstTimes) {

		for (int i = 0; i < size; i++) {

			createProcessManual(burstTimes.get(i));

		}

		

		int previousBurstTime = 0;
		int quantumCounter =0;
		int currentWaitTime = 0;
		for (int i = 0; i < processBag.size(); i++) {

			previousBurstTime = processBag.get(i).getBurstTime();
			processBag.get(i).setBurstTime(processBag.get(i).getBurstTime() - quantum);
			
			if(processBag.get(i).getBurstTime()<= 0) {
				currentWaitTime += previousBurstTime;
			
			processBag.get(i).setWaitTime(currentWaitTime);
			
			}
			else {
				//i dont fucking know
			}
		}

		for (int i = 0; i < processBag.size(); i++) {

			processBag.get(i).setTurnAroundTime(processBag.get(i).getWaitTime() + processBag.get(i).getBurstTime());

		}
		calculateAverageWaitTime();
		calculateAverageTATime();
		
		sortByProcessNumber();

		System.out.println(processBag + "\n" + averageWaitTime + " " + averageTurnAroundTime);

	}

	public void sortByProcessNumber() {

		Collections.sort(processBag, new Comparator<Process>() {
			@Override
			public int compare(Process u1, Process u2) {
				return u1.getProcessNumber().compareTo(u2.getProcessNumber());
			}
		});
	}

	public void calculateAverageWaitTime() {
		double waitTime = 0;
		for (int i = 0; i < processBag.size(); i++) {

			waitTime += processBag.get(i).getWaitTime();
		}

		waitTime = waitTime / processBag.size();

		this.averageWaitTime = Math.round(waitTime);
	}

	public void calculateAverageTATime() {
		double TATime = 0;
		for (int i = 0; i < processBag.size(); i++) {

			TATime += processBag.get(i).getTurnAroundTime();
		}

		TATime = TATime / processBag.size();

		this.averageTurnAroundTime = Math.round(TATime);
	}

	public ArrayList<Process> getProcessBag() {
		return processBag;
	}
	
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
