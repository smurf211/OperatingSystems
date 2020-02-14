package controller;


import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import model.FCFS;
import model.Priority;
import model.SJF;
import model.Process;

/**
 * 
 * 
 * The controller class contains the code which sends information from the GUI to the model. 
 * All data must move through this class in order to execute. Functions written here are meant to get data, 
 * run it through backend algorithms, and return them back to view.
 * 
 * @see <A href="../src/model/Controller.java">Java
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




public class Controller implements Initializable {
	FCFS fcfs;
	SJF sjf;
	Priority priority;
	int size;
	ObservableList<Integer> processNumbers = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	ObservableList<String> schedulers = FXCollections.observableArrayList("FCFS", "SJF", "PRIORITY");
	ObservableList<Integer> quantumNumbers = FXCollections.observableArrayList(5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
			16, 17, 18, 19, 20);
	@FXML
	private Button calculateButton;

	@FXML
	private ComboBox processSelector;
	@FXML
	private ComboBox schedulerSelector;
	@FXML
	private ComboBox quantumCombo;
	@FXML
	private TextField processBox1, processBox2, processBox3, processBox4, processBox5, processBox6, processBox7,
			processBox8, processBox9, processBox10;
	@FXML
	private TextField burstBox1, burstBox2, burstBox3, burstBox4, burstBox5, burstBox6, burstBox7, burstBox8, burstBox9,
			burstBox10;
	@FXML
	private TextField waitBox1, waitBox2, waitBox3, waitBox4, waitBox5, waitBox6, waitBox7, waitBox8, waitBox9,
			waitBox10;
	@FXML
	private TextField TABox1, TABox2, TABox3, TABox4, TABox5, TABox6, TABox7, TABox8, TABox9, TABox10;
	@FXML
	private TextField arrivalBox1, arrivalBox2, arrivalBox3, arrivalBox4, arrivalBox5, arrivalBox6, arrivalBox7,
			arrivalBox8, arrivalBox9, arrivalBox10;
	@FXML
	Rectangle rect1, rect2, rect3, rect4, rect5, rect6, rect7, rect8, rect9, rect10;
	@FXML
	private TextField belowRectText;
	@FXML
	private TextField gantt1, gantt2, gantt3, gantt4, gantt5, gantt6, gantt7, gantt8, gantt9, gantt10, gantt11;
	@FXML
	private TextField colorBox1, colorBox2, colorBox3, colorBox4, colorBox5, colorBox6, colorBox7, colorBox8, colorBox9,
			colorBox10;
	@FXML
	private TextField priorityBox1, priorityBox2, priorityBox3, priorityBox4, priorityBox5, priorityBox6, priorityBox7,
			priorityBox8, priorityBox9, priorityBox10;
	@FXML
	private TextField averageWaitBox, averageTABox, quantumBox;
	@FXML
	private MenuItem exitItem, infoItem;

	ArrayList<TextField> processBoxList = new ArrayList<TextField>();
	ArrayList<TextField> burstBoxList = new ArrayList<TextField>();
	ArrayList<TextField> waitBoxList = new ArrayList<TextField>();
	ArrayList<TextField> TABoxList = new ArrayList<TextField>();
	ArrayList<TextField> arrivalBoxList = new ArrayList<TextField>();
	ArrayList<TextField> priorityBoxList = new ArrayList<TextField>();
	ArrayList<Rectangle> rectList = new ArrayList<Rectangle>();
	ArrayList<TextField> ganttList = new ArrayList<TextField>();
	ArrayList<TextField> colorBoxList = new ArrayList<TextField>();
	
	/**
	 * Pulls all burst time data from GUI, first performing a check for empty values and incorrect inputs such as letters or symbols.
	 * Runs burst times through FCFS class. Then returns calculated values as well as generated arrival times to the view.
	 * Gantt chart is then created based off the data returned.
	 * @return void
	 */

	@FXML
	public void runCPUFCFS() {
		

		if (failInputs()) {   //check burst times for "" or for letters and symbols. if TRUE return.
			return;
		}

		Process.setProcessNumberCounter(0);  //must reset STATIC variable numbercounter so when a new algorithm is chosen this variable is reset.

		fcfs = new FCFS(size);
		ArrayList<Integer> burstTimes = new ArrayList<Integer>();
		
		//get burst times from GUI
		for (int i = 0; i < size; i++) {

			burstTimes.add(Integer.valueOf(burstBoxList.get(i).getText()));

		}
		//runs main algorithm in FCFS
		fcfs.runProcessesManual(burstTimes);
		
		//return calculated data to the view
		for (int i = 0; i < size; i++) {
			waitBoxList.get(i).setText(String.valueOf(fcfs.getProcessBag().get(i).getWaitTime()));
			TABoxList.get(i).setText(String.valueOf(fcfs.getProcessBag().get(i).getTurnAroundTime()));
			arrivalBoxList.get(i).setText(String.valueOf(fcfs.getProcessBag().get(i).getArrivalTime()));

		}
		
		//begin creating gantt chart
		gantt1.setText("0");

		for (int i = 0; i < fcfs.getProcessBag().size(); i++) {

			colorBoxList.get(i).setText("P" + fcfs.getProcessBag().get(i).getProcessNumber());

			ganttList.get(i + 1).setText(String.valueOf(fcfs.getGanttTimes().get(i)));
		}
		
		//return averages to view
		averageWaitBox.setText(String.valueOf(fcfs.getAverageWaitTime()));
		averageTABox.setText(String.valueOf(fcfs.getAverageTurnAroundTime()));

	}
	
	/**
	 * Pulls all burst time data from GUI, first performing a check for empty values and incorrect inputs such as letters or symbols.
	 * Runs burst times through SJF class. Then returns calculated values as well as generated arrival times to the view.
	 * Gantt chart is then created based off the data returned.
	 * @return void
	 */


	@FXML
	public void runCPUSJF() {

		if (failInputs()) { //check burst times for "" or for letters and symbols. if TRUE return.
			return;
		}

		Process.setProcessNumberCounter(0);  //must reset STATIC variable numbercounter so when a new algorithm is chosen this variable is reset.

		sjf = new SJF(size);
		ArrayList<Integer> burstTimes = new ArrayList<Integer>();
		//get burst times from GUI
		for (int i = 0; i < size; i++) {

			burstTimes.add(Integer.valueOf(burstBoxList.get(i).getText()));
		}
		//runs main algorithm in FCFS
		sjf.runProcessesManual(burstTimes);

		//return calculated data to the view
		for (int i = 0; i < size; i++) {
			waitBoxList.get(i).setText(String.valueOf(sjf.getProcessBag().get(i).getWaitTime()));
			TABoxList.get(i).setText(String.valueOf(sjf.getProcessBag().get(i).getTurnAroundTime()));
			arrivalBoxList.get(i).setText(String.valueOf(sjf.getProcessBag().get(i).getArrivalTime()));

		}
		
		//resort array to correct gantt chart display
		sjf.sortByJobLength();
		
		//begin creating gantt chart
		gantt1.setText("0");

		for (int i = 0; i < sjf.getProcessBag().size(); i++) {

			colorBoxList.get(i).setText("P" + sjf.getProcessBag().get(i).getProcessNumber());

			ganttList.get(i + 1).setText(String.valueOf(sjf.getProcessBag().get(i).getTurnAroundTime()));
		}

		//return averages to view
		averageWaitBox.setText(String.valueOf(sjf.getAverageWaitTime()));
		averageTABox.setText(String.valueOf(sjf.getAverageTurnAroundTime()));

	}
	
	/**
	 * Pulls all burst time data from GUI, first performing a check for empty values and incorrect inputs such as letters or symbols.
	 * Runs burst times through Priority class. Then returns calculated values as well as generated arrival times to the view.
	 * Gantt chart is then created based off the data returned.
	 * @return void
	 */


	@FXML
	public void runCPUPriority() {

		if (failInputs()) { //check burst times for "" or for letters and symbols. if TRUE return.
			return;
		}

		Process.setProcessNumberCounter(0);  //must reset STATIC variable numbercounter so when a new algorithm is chosen this variable is reset.

		priority = new Priority(size);
		ArrayList<Integer> burstTimes = new ArrayList<Integer>();
		//get burst times from GUI
		for (int i = 0; i < size; i++) {

			burstTimes.add(Integer.valueOf(burstBoxList.get(i).getText()));
		}

		ArrayList<Integer> priorityList = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {

			priorityList.add(Integer.valueOf(priorityBoxList.get(i).getText()));
		}
		
		//runs main algorithm in Priority
		priority.runProcessesManual(burstTimes, priorityList);

		//return calculated data to the view

		for (int i = 0; i < size; i++) {
			waitBoxList.get(i).setText(String.valueOf(priority.getProcessBag().get(i).getWaitTime()));
			TABoxList.get(i).setText(String.valueOf(priority.getProcessBag().get(i).getTurnAroundTime()));
			arrivalBoxList.get(i).setText(String.valueOf(priority.getProcessBag().get(i).getArrivalTime()));

		}
		
		//resort to correct gantt display order
		priority.sortByPriority();

		//begin creating gantt chart
		gantt1.setText("0");

		for (int i = 0; i < priority.getProcessBag().size(); i++) {

			colorBoxList.get(i).setText("P" + priority.getProcessBag().get(i).getProcessNumber());
			ganttList.get(i + 1).setText(String.valueOf(priority.getProcessBag().get(i).getTurnAroundTime()));
		}
		
		//return averages to view
		averageWaitBox.setText(String.valueOf(priority.getAverageWaitTime()));
		averageTABox.setText(String.valueOf(priority.getAverageTurnAroundTime()));

	}
	
	/**
	 * Selects the scheduler and modifies the action of the calculate button.
	 * @return void
	 */


	@FXML
	public void selectScheduler() {
		// clear button

		String selection = String.valueOf(schedulerSelector.getSelectionModel().getSelectedItem());

		if (selection.equals("FCFS")) {

			for (int i = 0; i < priorityBoxList.size(); i++) {

				priorityBoxList.get(i).setDisable(true);
			}

			calculateButton.setOnAction(e -> {

				runCPUFCFS();

			});

		} else if (selection.equals("SJF")) {

			for (int i = 0; i < priorityBoxList.size(); i++) {

				priorityBoxList.get(i).setDisable(true);
			}

			calculateButton.setOnAction(e -> {

				runCPUSJF();

			});

		}

		else if (selection.equals("PRIORITY")) {

			for (int i = 0; i < priorityBoxList.size(); i++) {

				priorityBoxList.get(i).setDisable(false);
			}

			calculateButton.setOnAction(e -> {

				runCPUPriority();

			});

		}

	}
	
	/**
	 * Selects the number of processes and disables unneeded fields.
	 * @return void
	 */


	@FXML
	public void selectProcesses() {

		String selection = String.valueOf(processSelector.getSelectionModel().getSelectedItem());
		size = Integer.valueOf(selection);

		for (int i = 0; i < burstBoxList.size(); i++) {

			burstBoxList.get(i).clear();
			waitBoxList.get(i).clear();
			TABoxList.get(i).clear();
			arrivalBoxList.get(i).clear();
			priorityBoxList.get(i).clear();
		}

		if (selection.equals("1")) {
			size = 1;

			for (int i = 1; i < rectList.size(); i++) {

				processBoxList.get(i).setVisible(false);
				burstBoxList.get(i).setVisible(false);
				waitBoxList.get(i).setVisible(false);
				TABoxList.get(i).setVisible(false);
				arrivalBoxList.get(i).setVisible(false);
				priorityBoxList.get(i).setVisible(false);
				ganttList.get(i + 1).setVisible(false);

			}

			for (int i = 1; i < rectList.size(); i++) {

				rectList.get(i).setVisible(false);
				colorBoxList.get(i).setVisible(false);

			}

		}

		if (selection.equals("2")) {
			size = 2;
			for (int i = 0; i < 2; i++) {

				rectList.get(i).setVisible(true);
				colorBoxList.get(i).setVisible(true);
				processBoxList.get(i).setVisible(true);
				burstBoxList.get(i).setVisible(true);
				waitBoxList.get(i).setVisible(true);
				TABoxList.get(i).setVisible(true);
				arrivalBoxList.get(i).setVisible(true);
				ganttList.get(i + 1).setVisible(true);
				priorityBoxList.get(i).setVisible(true);

			}

			for (int i = 2; i < rectList.size(); i++) {

				rectList.get(i).setVisible(false);
				colorBoxList.get(i).setVisible(false);
				processBoxList.get(i).setVisible(false);
				burstBoxList.get(i).setVisible(false);
				waitBoxList.get(i).setVisible(false);
				TABoxList.get(i).setVisible(false);
				arrivalBoxList.get(i).setVisible(false);
				ganttList.get(i + 1).setVisible(false);
				priorityBoxList.get(i).setVisible(false);

			}

		} else if (selection.equals("3")) {
			size = 3;
			for (int i = 0; i < 3; i++) {

				rectList.get(i).setVisible(true);
				colorBoxList.get(i).setVisible(true);
				processBoxList.get(i).setVisible(true);
				burstBoxList.get(i).setVisible(true);
				waitBoxList.get(i).setVisible(true);
				TABoxList.get(i).setVisible(true);
				arrivalBoxList.get(i).setVisible(true);
				ganttList.get(i + 1).setVisible(true);
				priorityBoxList.get(i).setVisible(true);

			}

			for (int i = 3; i < rectList.size(); i++) {

				rectList.get(i).setVisible(false);
				colorBoxList.get(i).setVisible(false);
				processBoxList.get(i).setVisible(false);
				burstBoxList.get(i).setVisible(false);
				waitBoxList.get(i).setVisible(false);
				TABoxList.get(i).setVisible(false);
				arrivalBoxList.get(i).setVisible(false);
				ganttList.get(i + 1).setVisible(false);
				priorityBoxList.get(i).setVisible(false);

			}

		} else if (selection.equals("4")) {
			size = 4;
			for (int i = 0; i < 4; i++) {

				rectList.get(i).setVisible(true);
				colorBoxList.get(i).setVisible(true);
				processBoxList.get(i).setVisible(true);
				burstBoxList.get(i).setVisible(true);
				waitBoxList.get(i).setVisible(true);
				TABoxList.get(i).setVisible(true);
				arrivalBoxList.get(i).setVisible(true);
				ganttList.get(i + 1).setVisible(true);
				priorityBoxList.get(i).setVisible(true);

			}

			for (int i = 4; i < rectList.size(); i++) {

				rectList.get(i).setVisible(false);
				colorBoxList.get(i).setVisible(false);
				processBoxList.get(i).setVisible(false);
				burstBoxList.get(i).setVisible(false);
				waitBoxList.get(i).setVisible(false);
				TABoxList.get(i).setVisible(false);
				arrivalBoxList.get(i).setVisible(false);
				ganttList.get(i + 1).setVisible(false);
				priorityBoxList.get(i).setVisible(false);

			}

		} else if (selection.equals("5")) {
			size = 5;
			for (int i = 0; i < 5; i++) {

				rectList.get(i).setVisible(true);
				colorBoxList.get(i).setVisible(true);
				processBoxList.get(i).setVisible(true);
				burstBoxList.get(i).setVisible(true);
				waitBoxList.get(i).setVisible(true);
				TABoxList.get(i).setVisible(true);
				arrivalBoxList.get(i).setVisible(true);
				ganttList.get(i + 1).setVisible(true);
				priorityBoxList.get(i).setVisible(true);

			}

			for (int i = 5; i < rectList.size(); i++) {

				rectList.get(i).setVisible(false);
				colorBoxList.get(i).setVisible(false);
				processBoxList.get(i).setVisible(false);
				burstBoxList.get(i).setVisible(false);
				waitBoxList.get(i).setVisible(false);
				TABoxList.get(i).setVisible(false);
				arrivalBoxList.get(i).setVisible(false);
				ganttList.get(i + 1).setVisible(false);
				priorityBoxList.get(i).setVisible(false);

			}

		} else if (selection.equals("6")) {
			size = 6;
			for (int i = 0; i < 6; i++) {

				rectList.get(i).setVisible(true);
				colorBoxList.get(i).setVisible(true);
				processBoxList.get(i).setVisible(true);
				burstBoxList.get(i).setVisible(true);
				waitBoxList.get(i).setVisible(true);
				TABoxList.get(i).setVisible(true);
				arrivalBoxList.get(i).setVisible(true);
				ganttList.get(i + 1).setVisible(true);
				priorityBoxList.get(i).setVisible(true);

			}

			for (int i = 6; i < rectList.size(); i++) {

				rectList.get(i).setVisible(false);
				colorBoxList.get(i).setVisible(false);
				processBoxList.get(i).setVisible(false);
				burstBoxList.get(i).setVisible(false);
				waitBoxList.get(i).setVisible(false);
				TABoxList.get(i).setVisible(false);
				arrivalBoxList.get(i).setVisible(false);
				ganttList.get(i + 1).setVisible(false);
				priorityBoxList.get(i).setVisible(false);

			}

		} else if (selection.equals("7")) {
			size = 7;
			for (int i = 0; i < 7; i++) {

				rectList.get(i).setVisible(true);
				colorBoxList.get(i).setVisible(true);
				processBoxList.get(i).setVisible(true);
				burstBoxList.get(i).setVisible(true);
				waitBoxList.get(i).setVisible(true);
				TABoxList.get(i).setVisible(true);
				arrivalBoxList.get(i).setVisible(true);
				ganttList.get(i + 1).setVisible(true);
				priorityBoxList.get(i).setVisible(true);

			}

			for (int i = 7; i < rectList.size(); i++) {

				rectList.get(i).setVisible(false);
				colorBoxList.get(i).setVisible(false);
				processBoxList.get(i).setVisible(false);
				burstBoxList.get(i).setVisible(false);
				waitBoxList.get(i).setVisible(false);
				TABoxList.get(i).setVisible(false);
				arrivalBoxList.get(i).setVisible(false);
				ganttList.get(i + 1).setVisible(false);
				priorityBoxList.get(i).setVisible(false);

			}

		} else if (selection.equals("8")) {
			size = 8;
			for (int i = 0; i < 8; i++) {

				rectList.get(i).setVisible(true);
				colorBoxList.get(i).setVisible(true);
				processBoxList.get(i).setVisible(true);
				burstBoxList.get(i).setVisible(true);
				waitBoxList.get(i).setVisible(true);
				TABoxList.get(i).setVisible(true);
				arrivalBoxList.get(i).setVisible(true);
				ganttList.get(i + 1).setVisible(true);
				priorityBoxList.get(i).setVisible(true);

			}

			for (int i = 8; i < rectList.size(); i++) {

				rectList.get(i).setVisible(false);
				colorBoxList.get(i).setVisible(false);
				processBoxList.get(i).setVisible(false);
				burstBoxList.get(i).setVisible(false);
				waitBoxList.get(i).setVisible(false);
				TABoxList.get(i).setVisible(false);
				arrivalBoxList.get(i).setVisible(false);
				ganttList.get(i + 1).setVisible(false);
				priorityBoxList.get(i).setVisible(false);

			}

		} else if (selection.equals("9")) {
			size = 9;
			for (int i = 0; i < 9; i++) {

				rectList.get(i).setVisible(true);
				colorBoxList.get(i).setVisible(true);
				processBoxList.get(i).setVisible(true);
				burstBoxList.get(i).setVisible(true);
				waitBoxList.get(i).setVisible(true);
				TABoxList.get(i).setVisible(true);
				arrivalBoxList.get(i).setVisible(true);
				ganttList.get(i + 1).setVisible(true);
				priorityBoxList.get(i).setVisible(true);

			}

			for (int i = 9; i < rectList.size(); i++) {

				rectList.get(i).setVisible(false);
				colorBoxList.get(i).setVisible(false);
				processBoxList.get(i).setVisible(false);
				burstBoxList.get(i).setVisible(false);
				waitBoxList.get(i).setVisible(false);
				TABoxList.get(i).setVisible(false);
				arrivalBoxList.get(i).setVisible(false);
				ganttList.get(i + 1).setVisible(false);
				priorityBoxList.get(i).setVisible(false);

			}

		} else if (selection.equals("10")) {
			size = 10;
			for (int i = 0; i < rectList.size(); i++) {

				rectList.get(i).setVisible(true);
				colorBoxList.get(i).setVisible(true);
				processBoxList.get(i).setVisible(true);
				burstBoxList.get(i).setVisible(true);
				waitBoxList.get(i).setVisible(true);
				TABoxList.get(i).setVisible(true);
				arrivalBoxList.get(i).setVisible(true);
				ganttList.get(i + 1).setVisible(true);
				priorityBoxList.get(i).setVisible(true);

			}

		}

	}

	/**
	 * calls static method to generate random burst times from FCFS.
	 * @return void
	 */


	@FXML
	public void randomBurstTimes() {

		for (int i = 0; i < burstBoxList.size(); i++) {
			burstBoxList.get(i).setText(String.valueOf(FCFS.generateBurstTime()));
		}

	}
	
	/**
	 * Checks for failed inputs either "", capital or lower case letters as well as symbols.
	 * @return void
	 */


	public boolean failInputs() {
		Alerts alerts = new Alerts();

		String capitals = "[A-Z]+";

		String lowercase = "[a-z]+";
		String symbols = "[!\\\"#$%&'()*+,-./:;<=>?@[\\\\]^_`{|}~]+";
		Pattern patternCapitals = Pattern.compile(capitals);
		Pattern patternLowercase = Pattern.compile(lowercase);

		Pattern patternSymbols = Pattern.compile(symbols);

		Matcher matcherCapitals;
		Matcher matcherLowercase;

		Matcher matcherSymbols;

		for (int i = 0; i < size; i++) {

			String text = burstBoxList.get(i).getText();

			matcherCapitals = patternCapitals.matcher(text);
			matcherLowercase = patternLowercase.matcher(text);
			matcherSymbols = patternSymbols.matcher(text);

			if (text.length() < 1 || matcherCapitals.find() || matcherLowercase.find() || matcherSymbols.find()) {

				alerts.failureAlert("Please enter a number in all burst fields!");

				return true;

			} else if (Integer.valueOf(text) > 100 || Integer.valueOf(text) == 0) {

				alerts.failureAlert("Please enter a number between 1-99");
				return true;

			}

		}

		if (!priorityBoxList.get(0).isDisabled()) {

			for (int i = 0; i < size; i++) {

				String text = priorityBoxList.get(i).getText();

				matcherCapitals = patternCapitals.matcher(text);
				matcherLowercase = patternLowercase.matcher(text);
				matcherSymbols = patternSymbols.matcher(text);

				if (text.length() < 1 || matcherCapitals.find() || matcherLowercase.find() || matcherSymbols.find()) {

					alerts.failureAlert("Please enter a number in all priority fields!");

					return true;

				} else if (Integer.valueOf(text) > 20) {

					alerts.failureAlert("Please enter a number between 0-9");
					return true;

				}

			}
		}

		return false;

	}
	
	/**
	 * allows user to toggle a help menu item
	 * @return void
	 */

	@FXML
	public void helpInfo() {
		Alerts alerts = new Alerts();
		alerts.SuccessAlert("Welcome to Mike's CPU Scheduler Simulator!\nBurst times should be within 1-100.\n"
				+ "Arrival Times are generated only for FCFS and will preserve the logical process order.\n"
				+ "Priority numbers are ranked LOW to HIGH. I.E. 0 has the highest priority.\n" + "Enjoy!");

	}
	
	/**
	 * controls menu item to exit program
	 * @return void
	 */

	@FXML
	public void exit() {
		System.exit(0);
	}
	
	/**
	 * allows for customization of initial values, loads all arrays with GUI objects from scenebuilder.
	 * @return void
	 */

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		processSelector.setItems(processNumbers);
		processSelector.getSelectionModel().select(9);
		schedulerSelector.setItems(schedulers);
		schedulerSelector.getSelectionModel().select(0);
		quantumCombo.setItems(quantumNumbers);
		quantumCombo.getSelectionModel().select(0);
		quantumCombo.setVisible(false);
		quantumBox.setVisible(false);
		size = 10;

		processBoxList.add(processBox1);
		processBoxList.add(processBox2);
		processBoxList.add(processBox3);
		processBoxList.add(processBox4);
		processBoxList.add(processBox5);
		processBoxList.add(processBox6);
		processBoxList.add(processBox7);
		processBoxList.add(processBox8);
		processBoxList.add(processBox9);
		processBoxList.add(processBox10);

		burstBoxList.add(burstBox1);
		burstBoxList.add(burstBox2);
		burstBoxList.add(burstBox3);
		burstBoxList.add(burstBox4);
		burstBoxList.add(burstBox5);
		burstBoxList.add(burstBox6);
		burstBoxList.add(burstBox7);
		burstBoxList.add(burstBox8);
		burstBoxList.add(burstBox9);
		burstBoxList.add(burstBox10);

		waitBoxList.add(waitBox1);
		waitBoxList.add(waitBox2);
		waitBoxList.add(waitBox3);
		waitBoxList.add(waitBox4);
		waitBoxList.add(waitBox5);
		waitBoxList.add(waitBox6);
		waitBoxList.add(waitBox7);
		waitBoxList.add(waitBox8);
		waitBoxList.add(waitBox9);
		waitBoxList.add(waitBox10);

		TABoxList.add(TABox1);
		TABoxList.add(TABox2);
		TABoxList.add(TABox3);
		TABoxList.add(TABox4);
		TABoxList.add(TABox5);
		TABoxList.add(TABox6);
		TABoxList.add(TABox7);
		TABoxList.add(TABox8);
		TABoxList.add(TABox9);
		TABoxList.add(TABox10);

		arrivalBoxList.add(arrivalBox1);
		arrivalBoxList.add(arrivalBox2);
		arrivalBoxList.add(arrivalBox3);
		arrivalBoxList.add(arrivalBox4);
		arrivalBoxList.add(arrivalBox5);
		arrivalBoxList.add(arrivalBox6);
		arrivalBoxList.add(arrivalBox7);
		arrivalBoxList.add(arrivalBox8);
		arrivalBoxList.add(arrivalBox9);
		arrivalBoxList.add(arrivalBox10);

		priorityBoxList.add(priorityBox1);
		priorityBoxList.add(priorityBox2);
		priorityBoxList.add(priorityBox3);
		priorityBoxList.add(priorityBox4);
		priorityBoxList.add(priorityBox5);
		priorityBoxList.add(priorityBox6);
		priorityBoxList.add(priorityBox7);
		priorityBoxList.add(priorityBox8);
		priorityBoxList.add(priorityBox9);
		priorityBoxList.add(priorityBox10);

		for (int i = 0; i < priorityBoxList.size(); i++) {

			priorityBoxList.get(i).setDisable(true);
		}

		rectList.add(rect1);
		rectList.add(rect2);
		rectList.add(rect3);
		rectList.add(rect4);
		rectList.add(rect5);
		rectList.add(rect6);
		rectList.add(rect7);
		rectList.add(rect8);
		rectList.add(rect9);
		rectList.add(rect10);

		colorBoxList.add(colorBox1);
		colorBoxList.add(colorBox2);
		colorBoxList.add(colorBox3);
		colorBoxList.add(colorBox4);
		colorBoxList.add(colorBox5);
		colorBoxList.add(colorBox6);
		colorBoxList.add(colorBox7);
		colorBoxList.add(colorBox8);
		colorBoxList.add(colorBox9);
		colorBoxList.add(colorBox10);

		ganttList.add(gantt1);
		ganttList.add(gantt2);
		ganttList.add(gantt3);
		ganttList.add(gantt4);
		ganttList.add(gantt5);
		ganttList.add(gantt6);
		ganttList.add(gantt7);
		ganttList.add(gantt8);
		ganttList.add(gantt9);
		ganttList.add(gantt10);
		ganttList.add(gantt11);
		
		helpInfo();

	}

}
