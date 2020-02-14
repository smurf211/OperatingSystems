package model;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * 
 * 
 * The First Fit class contains all functions necessary to perform the
 * algorithm. The First hole or end of memory is chosen to place the process.
 * 
 * 
 * @see <A href="../src/model/FirstFit.java">Java sourceCode</A>
 * 
 * 
 * @author Mike <A href="mailto:mspadaro@student.sjcny.edu">
 *         mspadaro@student.sjcny.edu </A>
 * 
 * @version v1.0, 11/31/2019
 * 
 * 
 */

public class FirstFit {

	private int totalMemSize;
	private ArrayList<MemBlock> memBlockBag = new ArrayList<MemBlock>();
	private double currentBlockHeight;
	private double totalBlockHeight;
	boolean holeCheck;
	int nextHoleSize;
	private int sizeOfNewHole;
	private int OSsize;
	Hole nodePrior = null;
	Hole nodeAfter = null;

	public FirstFit() {

	}

	/**
	 * class constructor, pulls total memory size, os size and calculates current
	 * block height. GUI block height is determined logically, IE 512K = 512 to
	 * calculate blockHeight of the actual HBoxs used to represent
	 * processes/holes/OS the size of 512 must be converted into a size equivalent
	 * to the ratio of total memory to HBox height.
	 * 
	 * @param int
	 *            OSsize, int totalMemSize
	 * 
	 * 
	 */

	public FirstFit(int OSsize, int totalMemSize) {
		this.OSsize = OSsize;
		this.totalMemSize = totalMemSize;
		OSBlock OS = new OSBlock(OSsize);
		memBlockBag.add(OS);
		double percent = Double.valueOf(OSsize) / Double.valueOf(totalMemSize);
		double blockHeight = percent * 700;
		this.currentBlockHeight = blockHeight;

	}

	/**
	 * Adds a process for this algorithm. Must check to find smallest possible size
	 * hole, or end of memory. First hole or end of memory gets the process.
	 * 
	 * @param int
	 *            size, int processNumber
	 * @return HBox
	 * 
	 */

	public HBox addProcess(int size, int processNumber) {

		holeCheck = false;
		for (int i = 0; i < memBlockBag.size(); i++) {

			if (memBlockBag.get(i) instanceof Process) {

				Process process = (Process) memBlockBag.get(i);
				if (process.getProcessNumber() == processNumber) {

					return null;

				}

			}
		}
		int checkValue = checkSize(size); // checks if the process fits, return -1 means does not fit, 0 is good, else
											// place in hole.
		if (checkValue == -1) {
			HBox hbox = new HBox();
			hbox.setMinHeight(999.99);
			return hbox;
		} else if (checkValue == 0) {
			Process p1 = new Process(size, processNumber);

			memBlockBag.add(p1);

		} else {
			Process p1 = new Process(size, processNumber);
			if (holeCheck) {

				if (p1.getSize() == memBlockBag.get(checkValue).getSize()) {

					memBlockBag.set(checkValue, p1);

				}

				else {

					sizeOfNewHole = memBlockBag.get(checkValue).getSize() - p1.getSize();
					memBlockBag.set(checkValue, p1);

					Hole hole = new Hole(sizeOfNewHole);
					memBlockBag.add(checkValue + 1, hole);

				}

			} else {
				memBlockBag.add(checkValue, p1);
			}
		}

		double percent = Double.valueOf(size) / Double.valueOf(totalMemSize);
		double blockHeight = percent * 700;
		if (!holeCheck) {
			this.currentBlockHeight += blockHeight;
		}

		HBox blockBox = new HBox();

		String cssLayout = "-fx-border-style: hidden none solid none;" + "-fx-border-color: red;\n"
				+ "-fx-border-width: 3;\n" + "-fx-background-color:#ff6666;\n";

		blockBox.setStyle(cssLayout);
		blockBox.setMinHeight(blockHeight);
		blockBox.setMaxHeight(blockHeight);
		Text text = new Text();

		text.setText("P" + processNumber);
		String cssFont = "";
		if (blockBox.getMinHeight() < 10) {
			cssFont = "-fx-font-size: 9pt;\n" + "-fx-font-family: Segoe UI Semibold;\n" + "-fx-text-fill: black;\n"
					+ "-fx-font-weight: bold;";
			text.setStyle(cssFont);

		} else {
			cssFont = "-fx-font-size: 11pt;\n" + "-fx-font-family: Segoe UI Semibold;\n" + "-fx-text-fill: black;\n"
					+ "-fx-font-weight: bold;";
			text.setStyle(cssFont);

		}
		text.setTextAlignment(TextAlignment.CENTER);
		blockBox.getChildren().add(text);
		blockBox.setAlignment(Pos.CENTER);

		return blockBox;

	}

	/**
	 * Determines whether the block requested to be added fits in a hole, or in
	 * memory at all.
	 * 
	 * @param int
	 *            processSize
	 * 
	 * @return int
	 */

	public int checkSize(int processSize) {
		holeCheck = false;
		if (memBlockBag.size() < 1) {

			return 0;
		}

		int totalProcessesSize = 0;
		int totalHoleSize = 0;
		nextHoleSize = 0;
		int indexCounter = 0;

		for (int i = 0; i < memBlockBag.size(); i++) {

			if (memBlockBag.get(i) instanceof OSBlock) {
				totalProcessesSize += memBlockBag.get(i).getSize();
				indexCounter++;
			}

			if (memBlockBag.get(i) instanceof Process) {

				totalProcessesSize += memBlockBag.get(i).getSize();
				indexCounter++;
			}

			if (memBlockBag.get(i) instanceof Hole) {

				nextHoleSize = memBlockBag.get(i).getSize();
				totalHoleSize += nextHoleSize;

				if (nextHoleSize >= processSize) {

					holeCheck = true;
					return indexCounter;
				}
				indexCounter++;
			}

		}

		if ((totalMemSize - totalProcessesSize - totalHoleSize) >= processSize) {

			return indexCounter;
		}

		else {
			return -1;
		}

	}

	/**
	 * Removes process from memory. Checks for holes prior or after requested block
	 * to be removed, and will merge the new hole with either of those.
	 * 
	 * @param int
	 *            processNumber
	 * @return HBox
	 * 
	 */

	public HBox removeProcess(int processNumber) {

		boolean processInMem = false;
		Process p1 = null;

		int index = 0;
		Hole hole = null;
		int counter = 0;
		for (int i = 0; i < memBlockBag.size(); i++) { //counts instances of each type until it finds the requested process, 
			//the counter tells me the index to remove

			if (memBlockBag.get(i) instanceof OSBlock) {

				index++;

			}

			if (memBlockBag.get(i) instanceof Hole) {

				index++;

			}

			if (memBlockBag.get(i) instanceof Process) {

				Process process = (Process) memBlockBag.get(i);
				if (process.getProcessNumber() == processNumber) {

					processInMem = true;
					p1 = (Process) memBlockBag.get(i);
					if (checkForHoles(processNumber) == 1) {

						nodePrior = (Hole) memBlockBag.get(i - 1);

					}
					if (checkForHoles(processNumber) == 2) {

						nodeAfter = (Hole) memBlockBag.get(i + 1);

					}
					break;

				}
				index++;

			}
		}

		if (!processInMem) {
			return null;
		}

		if (index == (memBlockBag.size() - 1)) {

			double percent = Double.valueOf(memBlockBag.get(index).getSize()) / Double.valueOf(totalMemSize);
			double blockHeight = percent * 700;
			currentBlockHeight -= blockHeight;
			memBlockBag.remove(index);
			HBox test = new HBox();
			test.setMinHeight(9999.0);
			if (nodePrior != null) {
				percent = Double.valueOf(memBlockBag.get(index - 1).getSize()) / Double.valueOf(totalMemSize);
				blockHeight = percent * 700;
				currentBlockHeight -= blockHeight;
				memBlockBag.remove(index - 1);
			}
			// nodePrior = null;
			// printMemBag();
			return test;
		} else {

			hole = new Hole(p1.getSize());
			memBlockBag.set(index, hole);
		}

		if (nodePrior != null) {

			hole = new Hole(p1.getSize() + nodePrior.getSize());
			memBlockBag.set(index, hole);
			memBlockBag.remove(index - 1);

		}

		if (nodeAfter != null) {

			hole = new Hole(p1.getSize() + nodeAfter.getSize());
			memBlockBag.set(index, hole);
			memBlockBag.remove(index + 1);

		}

		double percent = Double.valueOf(hole.getSize()) / Double.valueOf(totalMemSize);
		double blockHeight = percent * 700;

		HBox blockBox = new HBox();

		String cssLayout = "-fx-border-style: hidden none solid none;" + "-fx-border-color: red;\n"
				+ "-fx-border-width: 3;\n" + "-fx-background-color: DeepSkyBlue ;\n";

		blockBox.setStyle(cssLayout);
		blockBox.setMinHeight(blockHeight);
		blockBox.setMaxHeight(blockHeight);
		Text text = new Text();

		text.setText("HOLE");

		text.setVisible(false);
		text.setTextAlignment(TextAlignment.CENTER);
		blockBox.getChildren().add(text);
		blockBox.setAlignment(Pos.CENTER);

		return blockBox;

	}

	/**
	 * Used for removeProcess, determines if the nodePrior or nodeAfter are a hole,
	 * if so must merge with new hole.
	 * 
	 * @param int
	 *            processSNumber
	 * @return int
	 */

	public int checkForHoles(int processNumber) {

		MemBlock nodePrior = null;
		MemBlock nodeAfter = null;
		Process p1 = null;
		int counter = 0;

		for (int i = 0; i < memBlockBag.size(); i++) {//gets nodePrior and nodeAfter

			if (memBlockBag.get(i) instanceof Process) {

				if (i > 0) {

					nodePrior = memBlockBag.get(i - 1);
				} else {
					nodePrior = null;
				}
				if (i != memBlockBag.size() - 1) {
					nodeAfter = memBlockBag.get(i + 1);
				} else {
					nodeAfter = null;
				}

				Process process = (Process) memBlockBag.get(i);
				if (process.getProcessNumber() == processNumber) {

					p1 = (Process) memBlockBag.get(i);
					break;

				}

			}

		}

		if (nodePrior instanceof Hole) {//determines if type is of Hole
			counter = 1;
		} else {
			this.nodePrior = null;
		}
		if (nodeAfter instanceof Hole) {
			counter = 2;
		} else {
			this.nodeAfter = null;
		}

		return counter;

	}

	/**
	 * Removes holes, subtracts all holes removed from blockheight.
	 * 
	 * 
	 * 
	 * @return void
	 */

	public void compactMemory() {

		for (int i = 0; i < memBlockBag.size(); i++) {

			if (memBlockBag.get(i) instanceof Hole) {

				currentBlockHeight -= getBlockHeight(memBlockBag.get(i).getSize(), totalMemSize);
				memBlockBag.remove(i);

			}

		}

	}

	/**
	 * Calculates blockHeight from GUI representation of actual height.
	 * 
	 * @param double
	 *            size, int totalMemSize
	 * 
	 * @return double
	 * 
	 * 
	 */
	public static double getBlockHeight(double size, int totalMemSize) {

		double percent = Double.valueOf(size) / Double.valueOf(totalMemSize);
		double blockHeight = percent * 700;

		return blockHeight;
	}

	/**
	 * Creates the hole HBox to be used.
	 * 
	 * @return HBox
	 * 
	 * 
	 */

	public HBox getHoleBox() {

		double percent = Double.valueOf(sizeOfNewHole) / Double.valueOf(totalMemSize);
		double blockHeight = percent * 700;

		HBox blockBox = new HBox();

		String cssLayout = "-fx-border-style: hidden none solid none;" + "-fx-border-color: red;\n"
				+ "-fx-border-width: 3;\n";

		blockBox.setStyle(cssLayout);
		blockBox.setMinHeight(blockHeight);
		blockBox.setMaxHeight(blockHeight);
		Text text = new Text();

		text.setText("HOLE");
		String cssFont = "-fx-font-size: 14pt;\n" + "-fx-font-family: Segoe UI Semibold;\n" + "-fx-text-fill: white;\n"
				+ "-fx-opacity: 1;";

		text.setStyle(cssFont);
		text.setTextAlignment(TextAlignment.CENTER);
		text.setVisible(false);
		blockBox.getChildren().add(text);
		blockBox.setAlignment(Pos.CENTER);

		return blockBox;

	}

	/**
	 * Prints contents of memory.
	 * 
	 * @return void
	 * 
	 * 
	 */

	public void printMemBag() {

		for (int i = 0; i < memBlockBag.size(); i++) {
			System.out.print(memBlockBag.get(i));

		}
		System.out.println();
	}

	public double getCurrentBlockHeight() {
		return currentBlockHeight;
	}

	public void setCurrentBlockHeight(int currentBlockHeight) {
		this.currentBlockHeight = currentBlockHeight;
	}

	public int getTotalMemSize() {
		return totalMemSize;
	}

	public void setTotalMemSize(int totalMemSize) {
		this.totalMemSize = totalMemSize;
	}

	public boolean isHoleCheck() {
		return holeCheck;
	}

	public int getSizeOfNewHole() {
		return sizeOfNewHole;
	}

	public double getTotalBlockHeight() {
		return totalBlockHeight;
	}

	public int getOSsize() {
		return OSsize;
	}

	public void setOSsize(int oSsize) {
		OSsize = oSsize;
	}

	public Hole getNodePrior() {
		return nodePrior;
	}

	public Hole getNodeAfter() {
		return nodeAfter;
	}

}
