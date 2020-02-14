package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.BestFit;
import model.FirstFit;
import model.Hole;
import model.WorstFit;

/**
 * 
 * 
 * The controller class contains the code which sends information from the GUI
 * to the model. All data must move through this class in order to execute.
 * Functions written here are meant to get data, run it through backend
 * algorithms, and return them back to view.
 * 
 * @see <A href="../src/model/Controller.java">Java sourceCode</A>
 * 
 * 
 * @author Mike <A href="mailto:mspadaro@student.sjcny.edu">
 *         mspadaro@student.sjcny.edu </A>
 * 
 * @version v1.0, 11/31/2019
 * 
 * 
 */

public class Controller implements Initializable {

	@FXML
	private VBox ramVbox;
	@FXML
	private ComboBox algoBox;
	ObservableList<String> algos = FXCollections.observableArrayList("First Fit", "Best Fit", "Worst Fit");
	@FXML
	private ComboBox processBox;
	ObservableList<String> processes = FXCollections.observableArrayList("P1", "P2", "P3", "P4", "P5", "P6", "P7", "P8",
			"P9", "P10");
	@FXML
	private TextField totalMemField;
	@FXML
	private TextField OSfield;
	@FXML
	private TextField processSizeField;
	@FXML
	private Text totalMemText;
	@FXML
	private AnchorPane anchorpane;
	@FXML
	private Button addBtn;
	@FXML
	private Button compactBtn;
	@FXML
	private Button removeBtn;
	FirstFit ff;
	BestFit bf;
	WorstFit wf;

	ArrayList<HBox> boxList = new ArrayList<HBox>();
	private int totalProcessMemSize;
	ArrayList<Text> processTextList = new ArrayList<Text>();
	private int totalMemSize;
	int OSsize;
	Alerts alert = new Alerts();
	String globalAlgo;

	/**
	 * Selects the current algorithm based on combo box selection.
	 * 
	 * @return void
	 */

	@FXML
	public void selectAlgo() {

		resetForNextAlgo();
		if (failInputs()) {

			return;
		}

		OSsize = Integer.valueOf(OSfield.getText());
		totalMemSize = Integer.valueOf(totalMemField.getText());

		String selection = String.valueOf(algoBox.getSelectionModel().getSelectedItem());

		if (selection.equals("First Fit")) {

			ff = new FirstFit(OSsize, totalMemSize);
			totalMemText.setText(String.valueOf(totalMemSize));

			globalAlgo = "First Fit";
			createOSBlock(globalAlgo);

			addBtn.setOnAction(e -> {

				addProcessFF();

			});

			compactBtn.setOnAction(e -> {

				compactMemory();

			});

			removeBtn.setOnAction(e -> {

				removeProcessFF();

			});

		}

		if (selection.equals("Best Fit")) {

			bf = new BestFit(OSsize, totalMemSize);
			totalMemText.setText(String.valueOf(totalMemSize));

			globalAlgo = "Best Fit";
			createOSBlock(globalAlgo);

			addBtn.setOnAction(e -> {

				addProcessBF();

			});

			compactBtn.setOnAction(e -> {

				compactMemory();

			});

			removeBtn.setOnAction(e -> {

				removeProcessBF();

			});

		}

		if (selection.equals("Worst Fit")) {

			wf = new WorstFit(OSsize, totalMemSize);
			totalMemText.setText(String.valueOf(totalMemSize));

			globalAlgo = "Worst Fit";
			createOSBlock(globalAlgo);

			addBtn.setOnAction(e -> {

				addProcessWF();

			});

			compactBtn.setOnAction(e -> {

				compactMemory();

			});

			removeBtn.setOnAction(e -> {

				removeProcessWF();

			});

		}

	}

	/**
	 * Pulls data from process size field and adds process using First Fit
	 * algorithm.
	 * 
	 * @return void
	 */

	@FXML
	public void addProcessFF() {

		if (algoBox.getSelectionModel().getSelectedItem() == null) {

			alert.failureAlert("Please select an algorithm");
			return;
		}

		if (failInputs()) {

			return;
		}

		int processSize = Integer.valueOf(processSizeField.getText());
		if (processSize < 50) {
			alert.failureAlert("Please enter a process size greater than 50");
			return;

		}
		int processNumber = (processBox.getSelectionModel().getSelectedIndex() + 1);

		HBox processBox = ff.addProcess(processSize, processNumber);

		if (ff.isHoleCheck()) { // functions to run if placing a process in a hole.
			String value = "";
			double blockHeightHere = 0;
			for (int i = 0; i < boxList.size(); i++) {

				blockHeightHere += boxList.get(i).getMinHeight();
				Text text = null;
				if (!boxList.get(i).getChildren().isEmpty()) {
					text = (Text) boxList.get(i).getChildren().get(0);
					value = text.getText();
				} else {
					text = (Text) processTextList.get(i - 1);
					String temp = text.getText();
					String number = "";
					String hole = "";
					number = temp.substring(4, temp.length());
					hole = temp.substring(0, 4);
					text.setText(number);

					Text text2 = new Text();
					text2.setText(text.getText());
					processTextList.set(i - 1, text2);
					anchorpane.getChildren().set(i - 1, text2);
					value = hole;

				}

				double size = ((boxList.get(i).getMinHeight() / 700) * totalMemSize);

				if (text.getText().equals("HOLE") && size >= processSize) {

					if (size == processSize) {
						ramVbox.getChildren().set(i, processBox);
						boxList.set(i, processBox);

						return;

					} else {

						HBox holeBox = ff.getHoleBox();

						ramVbox.getChildren().set(i, processBox);

						boxList.set(i, processBox);

						String oldProcessText = processTextList.get(i - 1).getText();

						int newww = Integer.valueOf(oldProcessText.substring(0, oldProcessText.length() - 1));

						String newProcessTextValue = String.valueOf(newww + processSize);

						boxList.add(i + 1, holeBox);
						ramVbox.getChildren().add(i + 1, holeBox);

						Text processText = new Text(newProcessTextValue + "K");

						String cssFont = "-fx-font-size: 11pt;\n" + "-fx-font-family: Bauhaus 93;\n"
								+ "-fx-font-weight: bold;";

						processText.setStyle(cssFont);

						if (holeBox.getMinHeight() < 12) {
							String holeText = "---------hole";
							Text holeText1 = new Text(holeText);

							String cssFont1 = "-fx-font-size: 10pt;\n" + "-fx-font-family: Bauhaus 93;\n"
									+ "-fx-font-weight: bold;";
							holeText1.setStyle(cssFont1);

							processText.setText(processText.getText());

							anchorpane.setTopAnchor(processText, (65.0 + blockHeightHere) - (holeBox.getMinHeight()));
							anchorpane.setLeftAnchor(processText, 700.0);
							anchorpane.getChildren().add(processText);
							processTextList.add(i, processText);

						}

						else if ((65.0 + blockHeightHere) < 750) {

							anchorpane.getChildren().add(processText);
							processTextList.add(i, processText);
							anchorpane.setTopAnchor(processText, (65.0 + blockHeightHere) - holeBox.getMinHeight());
							anchorpane.setLeftAnchor(processText, 470.0);
						} else {
							anchorpane.getChildren().add(processText);
							processTextList.add(processText);
							anchorpane.setTopAnchor(processText, (65.0 + blockHeightHere) - holeBox.getMinHeight());
							anchorpane.setLeftAnchor(processText, 709.0);

						}

						return;
					}
				}

			}

		} else if (processBox != null) {

			if (processBox.getMinHeight() == 999.99) {
				Alerts alert = new Alerts();
				alert.failureAlert("Process does not fit!");
				return;
			}
			ramVbox.getChildren().add(processBox);
			boxList.add(processBox);
		}

		else {
			Alerts alert = new Alerts();
			alert.failureAlert("Process already added!");
			return;
		}
		totalProcessMemSize += processSize;
		double currentBlockHeight;

		currentBlockHeight = ff.getCurrentBlockHeight();

		Text processText = new Text(String.valueOf(totalProcessMemSize) + "K");

		String cssFont = "-fx-font-size: 11pt;\n" + "-fx-font-family: Bauhaus 93;\n" + "-fx-font-weight: bold;";

		processText.setStyle(cssFont);
		if (processBox.getMinHeight() < 12) { // if size of process box is too small, place on right side

			anchorpane.getChildren().add(processText);
			processTextList.add(processText);
			anchorpane.setTopAnchor(processText, (65.0 + currentBlockHeight));
			anchorpane.setLeftAnchor(processText, 709.0);

		}

		else if ((65.0 + currentBlockHeight) < 750) {

			anchorpane.getChildren().add(processText);
			processTextList.add(processText);
			anchorpane.setTopAnchor(processText, (65.0 + currentBlockHeight));
			anchorpane.setLeftAnchor(processText, 470.0);
		} else { // if added process is too close to the end of memory, place on the right side.
			anchorpane.getChildren().add(processText);
			processTextList.add(processText);
			anchorpane.setTopAnchor(processText, (65.0 + currentBlockHeight));
			anchorpane.setLeftAnchor(processText, 709.0);

		}

	}

	/**
	 * Pulls data from process size field and adds process using Best Fit algorithm.
	 * 
	 * @return void
	 */

	@FXML
	public void addProcessBF() {

		if (algoBox.getSelectionModel().getSelectedItem() == null) {

			alert.failureAlert("Please select an algorithm");
			return;
		}

		if (failInputs()) {

			return;
		}

		int processSize = Integer.valueOf(processSizeField.getText());
		int processNumber = (processBox.getSelectionModel().getSelectedIndex() + 1);

		HBox processBox = bf.addProcess(processSize, processNumber);

		if (bf.isHoleCheck()) {
			String value = "";
			double blockHeightHere = 0;
			for (int i = 0; i < boxList.size(); i++) {

				blockHeightHere += boxList.get(i).getMinHeight();
				Text text = null;
				if (!boxList.get(i).getChildren().isEmpty()) {
					text = (Text) boxList.get(i).getChildren().get(0);
					value = text.getText();
				} else {
					text = (Text) processTextList.get(i - 1);
					String temp = text.getText();
					String number = "";
					String hole = "";
					number = temp.substring(4, temp.length());
					hole = temp.substring(0, 4);
					text.setText(number);

					Text text2 = new Text();
					text2.setText(text.getText());
					processTextList.set(i - 1, text2);
					anchorpane.getChildren().set(i - 1, text2);
					value = hole;

				}
				double size = ((boxList.get(i).getMinHeight() / 700) * totalMemSize);

				if (value.equals("HOLE") && i == bf.getSmallestHoleIndex()) { // where code differs from FF, must remove
																				// place in smallest hole

					if (size == processSize) {
						ramVbox.getChildren().set(i, processBox);
						boxList.set(i, processBox);

						return;

					} else {

						HBox holeBox = bf.getHoleBox();

						ramVbox.getChildren().set(i, processBox);
						boxList.set(i, processBox);

						String oldProcessText = processTextList.get(i - 1).getText();

						int newww = Integer.valueOf(oldProcessText.substring(0, oldProcessText.length() - 1));

						String newProcessTextValue = String.valueOf(newww + processSize);

						boxList.add(i + 1, holeBox);
						ramVbox.getChildren().add(i + 1, holeBox);

						Text processText = new Text(newProcessTextValue + "K");

						String cssFont = "-fx-font-size: 11pt;\n" + "-fx-font-family: Bauhaus 93;\n"
								+ "-fx-font-weight: bold;";

						processText.setStyle(cssFont);
						if (holeBox.getMinHeight() < 12) {
							String holeText = "---------hole";
							Text holeText1 = new Text(holeText);

							String cssFont1 = "-fx-font-size: 10pt;\n" + "-fx-font-family: Bauhaus 93;\n"
									+ "-fx-font-weight: bold;";
							holeText1.setStyle(cssFont1);

							processText.setText(processText.getText());

							anchorpane.setTopAnchor(processText, (65.0 + blockHeightHere) - (holeBox.getMinHeight()));
							anchorpane.setLeftAnchor(processText, 700.0);
							anchorpane.getChildren().add(processText);
							processTextList.add(i, processText);

						}

						else if ((65.0 + blockHeightHere) < 750) {

							anchorpane.getChildren().add(processText);
							processTextList.add(i, processText);
							anchorpane.setTopAnchor(processText, (65.0 + blockHeightHere) - holeBox.getMinHeight());
							anchorpane.setLeftAnchor(processText, 470.0);
						} else {
							anchorpane.getChildren().add(processText);
							processTextList.add(i, processText);
							anchorpane.setTopAnchor(processText, (65.0 + blockHeightHere) - holeBox.getMinHeight());
							anchorpane.setLeftAnchor(processText, 709.0);

						}

						return;
					}
				}

			}

		} else if (processBox != null) {
			if (processBox.getMinHeight() == 999.99) {
				Alerts alert = new Alerts();
				alert.failureAlert("Process does not fit!");
				return;
			}
			ramVbox.getChildren().add(processBox);
			boxList.add(processBox);
		}

		else {
			Alerts alert = new Alerts();
			alert.failureAlert("Process already added!");
			return;
		}
		totalProcessMemSize += processSize;
		double currentBlockHeight;

		currentBlockHeight = bf.getCurrentBlockHeight();

		Text processText = new Text(String.valueOf(totalProcessMemSize) + "K");

		String cssFont = "-fx-font-size: 11pt;\n" + "-fx-font-family: Bauhaus 93;\n" + "-fx-font-weight: bold;";

		processText.setStyle(cssFont);
		if (processBox.getMinHeight() < 12) {

			anchorpane.getChildren().add(processText);
			processTextList.add(processText);
			anchorpane.setTopAnchor(processText, (65.0 + currentBlockHeight));
			anchorpane.setLeftAnchor(processText, 709.0);

		}

		else if ((65.0 + currentBlockHeight) < 750) {

			anchorpane.getChildren().add(processText);
			processTextList.add(processText);
			anchorpane.setTopAnchor(processText, (65.0 + currentBlockHeight));
			anchorpane.setLeftAnchor(processText, 470.0);
		}

		else {
			anchorpane.getChildren().add(processText);
			processTextList.add(processText);
			anchorpane.setTopAnchor(processText, (65.0 + currentBlockHeight));
			anchorpane.setLeftAnchor(processText, 709.0);

		}

	}

	/**
	 * Pulls data from process size field and adds process using Worst Fit
	 * algorithm.
	 * 
	 * @return void
	 */

	@FXML
	public void addProcessWF() {
		if (algoBox.getSelectionModel().getSelectedItem() == null) {

			alert.failureAlert("Please select an algorithm");
			return;
		}

		if (failInputs()) {

			return;
		}

		int processSize = Integer.valueOf(processSizeField.getText());
		int processNumber = (processBox.getSelectionModel().getSelectedIndex() + 1);

		HBox processBox = wf.addProcess(processSize, processNumber);

		if (wf.isHoleCheck() && wf.getLargestHoleIndex() != boxList.size()) {

			String value = "";
			double blockHeightHere = 0;
			for (int i = 0; i < boxList.size(); i++) {

				blockHeightHere += boxList.get(i).getMinHeight();
				Text text = null;
				if (!boxList.get(i).getChildren().isEmpty()) {
					text = (Text) boxList.get(i).getChildren().get(0);
					value = text.getText();
				} else {
					text = (Text) processTextList.get(i - 1);
					String temp = text.getText();
					String number = "";
					String hole = "";
					number = temp.substring(4, temp.length());
					hole = temp.substring(0, 4);
					text.setText(number);

					Text text2 = new Text();
					text2.setText(text.getText());
					processTextList.set(i - 1, text2);
					anchorpane.getChildren().set(i - 1, text2);
					value = hole;

				}
				double size = ((boxList.get(i).getMinHeight() / 700) * totalMemSize);

				if (text.getText().equals("HOLE") && i == wf.getLargestHoleIndex()) {// where code differs from FF, must
																						// remove place in largest hole

					if (size == processSize) {
						ramVbox.getChildren().set(i, processBox);
						boxList.set(i, processBox);

						return;

					} else {

						HBox holeBox = wf.getHoleBox();

						ramVbox.getChildren().set(i, processBox);
						boxList.set(i, processBox);

						String oldProcessText = processTextList.get(i - 1).getText();

						int newww = Integer.valueOf(oldProcessText.substring(0, oldProcessText.length() - 1));

						String newProcessTextValue = String.valueOf(newww + processSize);

						boxList.add(i + 1, holeBox);
						ramVbox.getChildren().add(i + 1, holeBox);

						Text processText = new Text(newProcessTextValue + "K");

						String cssFont = "-fx-font-size: 11pt;\n" + "-fx-font-family: Bauhaus 93;\n"
								+ "-fx-font-weight: bold;";

						processText.setStyle(cssFont);
						if (holeBox.getMinHeight() < 12) {
							String holeText = "---------hole";
							Text holeText1 = new Text(holeText);

							String cssFont1 = "-fx-font-size: 10pt;\n" + "-fx-font-family: Bauhaus 93;\n"
									+ "-fx-font-weight: bold;";
							holeText1.setStyle(cssFont1);

							processText.setText(processText.getText());

							anchorpane.setTopAnchor(processText, (65.0 + blockHeightHere) - (holeBox.getMinHeight()));
							anchorpane.setLeftAnchor(processText, 700.0);
							anchorpane.getChildren().add(processText);
							processTextList.add(i, processText);

						}

						else if ((65.0 + blockHeightHere) < 750) {

							anchorpane.getChildren().add(processText);
							processTextList.add(i, processText);
							anchorpane.setTopAnchor(processText, (65.0 + blockHeightHere) - holeBox.getMinHeight());
							anchorpane.setLeftAnchor(processText, 470.0);
						} else {
							anchorpane.getChildren().add(processText);
							processTextList.add(processText);
							anchorpane.setTopAnchor(processText, (65.0 + blockHeightHere) - holeBox.getMinHeight());
							anchorpane.setLeftAnchor(processText, 709.0);

						}

						return;
					}
				}

			}

		} else if (processBox != null) {
			if (processBox.getMinHeight() == 999.99) {
				Alerts alert = new Alerts();
				alert.failureAlert("Process does not fit!");
				return;
			}
			ramVbox.getChildren().add(processBox);
			boxList.add(processBox);
		}

		else {
			Alerts alert = new Alerts();
			alert.failureAlert("Process already added!");
			return;
		}
		totalProcessMemSize += processSize;
		double currentBlockHeight;

		currentBlockHeight = wf.getCurrentBlockHeight();

		Text processText = new Text(String.valueOf(totalProcessMemSize) + "K");

		String cssFont = "-fx-font-size: 11pt;\n" + "-fx-font-family: Bauhaus 93;\n" + "-fx-font-weight: bold;";

		processText.setStyle(cssFont);
		if (processBox.getMinHeight() < 12) {

			anchorpane.getChildren().add(processText);
			processTextList.add(processText);
			anchorpane.setTopAnchor(processText, (65.0 + currentBlockHeight));
			anchorpane.setLeftAnchor(processText, 709.0);

		}

		else if ((65.0 + currentBlockHeight) < 750) {

			anchorpane.getChildren().add(processText);
			processTextList.add(processText);
			anchorpane.setTopAnchor(processText, (65.0 + currentBlockHeight));
			anchorpane.setLeftAnchor(processText, 470.0);
		} else {
			anchorpane.getChildren().add(processText);
			processTextList.add(processText);
			anchorpane.setTopAnchor(processText, (65.0 + currentBlockHeight));
			anchorpane.setLeftAnchor(processText, 709.0);

		}

	}

	/**
	 * Removes process for First Fit
	 * 
	 * @return void
	 */

	@FXML
	public void removeProcessFF() { // all three remove methods are the same minus calling different classes, should
									// rework in the future to combine into one.

		if (algoBox.getSelectionModel().getSelectedItem() == null) {

			alert.failureAlert("Please select an algorithm");
			return;
		}

		int processNumber = (processBox.getSelectionModel().getSelectedIndex() + 1);

		HBox holeBox = ff.removeProcess(processNumber);

		if (holeBox == null) {
			Alerts alert = new Alerts();
			alert.failureAlert("Process not in memory!");
			return;
		} else if (holeBox.getMinHeight() == 9999.0) {
			removeProcessNoHole("P" + processNumber, ff.getNodePrior());

		}

		else if (ff.getNodePrior() != null) {

			mergeHoles("P" + processNumber, holeBox, 1);

		} else if (ff.getNodeAfter() != null) {

			mergeHoles("P" + processNumber, holeBox, 2);

		} else if (holeBox != null) {

			replaceProcessWithHole("P" + processNumber, holeBox);

		}

	}

	/**
	 * Removes process for Best Fit
	 * 
	 * @return void
	 */

	@FXML
	public void removeProcessBF() {

		if (algoBox.getSelectionModel().getSelectedItem() == null) {

			alert.failureAlert("Please select an algorithm");
			return;
		}

		int processNumber = (processBox.getSelectionModel().getSelectedIndex() + 1);

		HBox holeBox = bf.removeProcess(processNumber);

		if (holeBox == null) {
			Alerts alert = new Alerts();
			alert.failureAlert("Process not in memory!");
			return;
		} else if (holeBox.getMinHeight() == 9999.0) {
			removeProcessNoHole("P" + processNumber, bf.getNodePrior());

		} else if (bf.getNodePrior() != null) {

			mergeHoles("P" + processNumber, holeBox, 1);

		} else if (bf.getNodeAfter() != null) {

			mergeHoles("P" + processNumber, holeBox, 2);

		}

		else if (holeBox != null) {

			replaceProcessWithHole("P" + processNumber, holeBox);

		}

	}

	/**
	 * Removes process for Worst Fit
	 * 
	 * @return void
	 */

	@FXML
	public void removeProcessWF() {

		if (algoBox.getSelectionModel().getSelectedItem() == null) {

			alert.failureAlert("Please select an algorithm");
			return;
		}

		int processNumber = (processBox.getSelectionModel().getSelectedIndex() + 1);

		HBox holeBox = wf.removeProcess(processNumber);

		if (holeBox == null) {
			Alerts alert = new Alerts();
			alert.failureAlert("Process not in memory!");
			return;
		} else if (holeBox.getMinHeight() == 9999.0) {

			removeProcessNoHole("P" + processNumber, wf.getNodePrior());

		}

		else if (wf.getNodePrior() != null) {

			mergeHoles("P" + processNumber, holeBox, 1);

		} else if (wf.getNodeAfter() != null) {

			mergeHoles("P" + processNumber, holeBox, 2);

		}

		else if (holeBox != null) {

			replaceProcessWithHole("P" + processNumber, holeBox);

		}

	}

	/**
	 * Algorithm to remove a process where a hole is not needed, IE at the end of
	 * contiguous memory.
	 * 
	 * @return void
	 */

	public void removeProcessNoHole(String processNumber, Hole nodePrior) {

		for (int i = 0; i < boxList.size(); i++) {

			HBox h1 = (HBox) ramVbox.getChildren().get(i);
			Text text2 = (Text) h1.getChildren().get(0);
			String processText2 = text2.getText();

			if (processText2.equals(processNumber)) {

				if (nodePrior != null) {
					ramVbox.getChildren().remove(i);

					boxList.remove(i);

					anchorpane.getChildren().remove(processTextList.get(i));
					processTextList.remove(i);

					HBox h2 = (HBox) ramVbox.getChildren().get(i - 1);
					ramVbox.getChildren().remove(i - 1);

					boxList.remove(i - 1);

					anchorpane.getChildren().remove(processTextList.get(i - 1));
					processTextList.remove(i - 1);

					int blocker = (int) ((h1.getMinHeight() / 700) * totalMemSize);
					blocker += (int) ((h2.getMinHeight() / 700) * totalMemSize);

					totalProcessMemSize -= blocker;

				} else {
					ramVbox.getChildren().remove(i);

					boxList.remove(i);

					anchorpane.getChildren().remove(processTextList.get(i));
					processTextList.remove(i);

					int blocker = (int) ((h1.getMinHeight() / 700) * totalMemSize);

					totalProcessMemSize -= blocker;
				}
			}
		}

	}

	/**
	 * Algorithm to merge holes when a process is removed and a hole is before or
	 * after it in memory.
	 * 
	 * @return void
	 */

	public void mergeHoles(String processNumber, HBox holeBox, int value) {

		for (int i = 0; i < boxList.size(); i++) {

			HBox h1 = (HBox) ramVbox.getChildren().get(i);
			Text text2 = (Text) h1.getChildren().get(0);
			String processText2 = text2.getText();

			if (processText2.equals(processNumber)) {

				if (value == 1) {

					ramVbox.getChildren().set(i, holeBox);
					ramVbox.getChildren().remove(i - 1);
					boxList.set(i, holeBox);
					boxList.remove(i - 1);
					anchorpane.getChildren().remove(processTextList.get(i - 1));
					processTextList.remove(i - 1);

				}
				if (value == 2) {

					ramVbox.getChildren().set(i, holeBox);
					ramVbox.getChildren().remove(i + 1);
					boxList.set(i, holeBox);
					boxList.remove(i + 1);
					anchorpane.getChildren().remove(processTextList.get(i + 1));
					processTextList.remove(i + 1);

				}

			}
		}

	}

	/**
	 * Algorithm to directly replace a process with a hole.
	 * 
	 * @return void
	 */

	public void replaceProcessWithHole(String processNumber, HBox holeBox) {

		for (int i = 0; i < boxList.size(); i++) {

			HBox h1 = (HBox) ramVbox.getChildren().get(i);
			Text text2 = (Text) h1.getChildren().get(0);
			String processText2 = text2.getText();

			if (processText2.equals(processNumber)) {

				ramVbox.getChildren().set(i, holeBox);

				boxList.set(i, holeBox);

			}
		}
	}

	/**
	 * Algorithm to compact memory. Removes all holes, shifts all GUI elements up
	 * the proper amount, subtracts hole size from labels. BackEnd for each
	 * algorithm will perform the same functions in-sync.
	 * 
	 * @return void
	 */

	@FXML
	public void compactMemory() {
		if (algoBox.getSelectionModel().getSelectedItem() == null) {

			alert.failureAlert("Please select an algorithm");
			return;
		}

		if (globalAlgo.equals("First Fit")) {
			ff.compactMemory();
		}

		else if (globalAlgo.equals("Best Fit")) {
			bf.compactMemory();
		} else if (globalAlgo.equals("Worst Fit")) {
			wf.compactMemory();
		}

		for (int i = 0; i < boxList.size(); i++) {

			HBox box1 = (HBox) boxList.get(i);

			Text text = (Text) box1.getChildren().get(0);
			String getText = text.getText();

			if (getText.equals("HOLE")) {

				int blocker = (int) ((box1.getMinHeight() / 700) * totalMemSize);

				totalProcessMemSize -= blocker;

				boxList.remove(i);
				ramVbox.getChildren().remove(i);

				anchorpane.getChildren().remove(processTextList.get(i));

				processTextList.remove(i);

				for (int x = i; x < processTextList.size(); x++) { // iterate through processSize labels and subtract
																	// the size of the removed process
																	// also subtract the size of the block from the
																	// position of the labels.

					int value1 = Integer.valueOf(processTextList.get(x).getText().substring(0,
							processTextList.get(x).getText().length() - 1));
					int newValue = value1 - blocker;
					processTextList.get(x).setText(newValue + "K");

					double currentAnchor = anchorpane.getTopAnchor(processTextList.get(x));

					double newAnchor = (currentAnchor - box1.getMinHeight());

					anchorpane.setTopAnchor(processTextList.get(x), newAnchor);

				}

			}

		}

	}

	/**
	 * Checks for incorrect GUI inputs.
	 * 
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

		String text = "";

		text = totalMemField.getText();

		matcherCapitals = patternCapitals.matcher(text);
		matcherLowercase = patternLowercase.matcher(text);
		matcherSymbols = patternSymbols.matcher(text);

		if (text.length() < 1 || matcherCapitals.find() || matcherLowercase.find() || matcherSymbols.find()) {

			alerts.failureAlert("Please enter a number total memory field!");

			return true;

		} else if (Integer.valueOf(text) < 100 || Integer.valueOf(text) == 0) {

			alerts.failureAlert("Please enter a larger number in total memory field!");
			return true;

		}

		text = OSfield.getText();

		matcherCapitals = patternCapitals.matcher(text);
		matcherLowercase = patternLowercase.matcher(text);
		matcherSymbols = patternSymbols.matcher(text);

		if (text.length() < 1 || matcherCapitals.find() || matcherLowercase.find() || matcherSymbols.find()) {

			alerts.failureAlert("Please enter a number in Operating System size field");

			return true;

		} else if (Integer.valueOf(text) < 100) {

			alerts.failureAlert("Please enter a larger number in Operating System size field");
			return true;

		}

		text = processSizeField.getText();

		matcherCapitals = patternCapitals.matcher(text);
		matcherLowercase = patternLowercase.matcher(text);
		matcherSymbols = patternSymbols.matcher(text);

		if (text.length() < 1 || matcherCapitals.find() || matcherLowercase.find() || matcherSymbols.find()) {

			alerts.failureAlert("Please enter a number in process size field!");

			return true;

		} else if (Integer.valueOf(text) < 1) {

			alerts.failureAlert("Please enter a larger number in process size field!");
			return true;

		}

		return false;

	}

	/**
	 * allows user to toggle a help menu item
	 * 
	 * @return void
	 */

	@FXML
	public void helpInfo() {
		Alerts alerts = new Alerts();

		alerts.SuccessAlert("Welcome to Mike's Memory Management Simulator\n\n"
				+ "Select one of the three algorithms, First Fit, Best Fit or Worst Fit.\n\n"
				+ "Select a process to add, then specify its size, add it to memory!\n\n"
				+ "Select a process to remove it from memory, and compact when you wish to remove holes.\n\n"
				+ "OS block is purple, Process blocks are redish orange, and holes or empty memory space are light blue.\n\n"
				+ "Ram size should be no more than 5 digits.\n" + "OS size should no more than 4 digits.\n\n"
				+ "Enjoy!");

	}

	/**
	 * allows user to toggle algorithm info.
	 * 
	 * @return void
	 */

	@FXML
	public void algoInfo() {
		Alerts alerts = new Alerts();

		alerts.SuccessAlert(
				"First Fit: This algorithm will place the next process in the first available block of memory. "
						+ "It spends less time analyzing analyzing memory than the other two algorithms.\n\n"
						+ "Best Fit: Analyzes all available memory blocks large enough to fit the requested process. "
						+ "It will choose the space with that will leave the smallest possible hole.\n\n"
						+ "Worst Fit: This algorithm analyzes all available memory blocks large enough to fit the requested process. "
						+ "However it will place the requested process in the memory block that will leave the largest hole.\n\n");

	}

	/**
	 * exits the program.
	 * 
	 * @return void
	 */

	@FXML
	public void exit() {
		System.exit(0);
	}

	/**
	 * Generates the OS block for the current algorithm.
	 * 
	 * @return void
	 */

	public void createOSBlock(String algo) {
		double percent = 0;
		if (algo.equals("First Fit")) { // similar method of how remove methods should be reworked.
			percent = Double.valueOf(OSsize) / Double.valueOf(ff.getTotalMemSize());
		}

		if (algo.equals("Best Fit")) {
			percent = Double.valueOf(OSsize) / Double.valueOf(bf.getTotalMemSize());
		}
		if (algo.equals("Worst Fit")) {
			percent = Double.valueOf(OSsize) / Double.valueOf(wf.getTotalMemSize());
		}

		double blockHeight = percent * 700;

		HBox OSBox = new HBox();

		String cssLayout = "-fx-border-style: hidden none solid none;" + "-fx-border-color: purple;\n"
				+ "-fx-border-width: 3;\n" + "-fx-background-color: MediumPurple ;\n";

		OSBox.setStyle(cssLayout);
		OSBox.setMinHeight(blockHeight);
		OSBox.setMaxHeight(blockHeight);
		Text text = new Text();

		text.setText("OS");
		String cssFont = "-fx-font-size: 11pt;\n" + "-fx-font-family: Segoe UI Semibold;\n" + "-fx-text-fill: black;\n"
				+ "-fx-font-weight: bold;";

		text.setStyle(cssFont);
		text.setTextAlignment(TextAlignment.CENTER);
		OSBox.getChildren().add(text);
		OSBox.setAlignment(Pos.CENTER);

		Text processText = new Text(String.valueOf(OSsize) + "K");

		String cssFont2 = "-fx-font-size: 11pt;\n" + "-fx-font-family: Bauhaus 93;\n" + "-fx-font-weight: bold;";

		processText.setStyle(cssFont2);

		anchorpane.getChildren().add(processText);
		processTextList.add(processText);
		anchorpane.setTopAnchor(processText, (65.0 + blockHeight));
		anchorpane.setLeftAnchor(processText, 470.0);

		ramVbox.getChildren().add(OSBox);
		boxList.add(OSBox);
		totalProcessMemSize += OSsize;

	}

	/**
	 * Clears all GUI fields.
	 * 
	 * @return void
	 */

	public void clearFields() {

		
		processSizeField.clear();

	}

	/**
	 * Prints the list containing all block elements. *Testing purposes*
	 * 
	 * @return void
	 */

	public void printBoxList() {

		for (int i = 0; i < boxList.size(); i++) {
			Text test = (Text) boxList.get(i).getChildren().get(0);
			System.out.print(test.getText() + " ");
		}
		System.out.println();
	}

	/**
	 * Resets the application when a different algorithm is chosen or RAM is
	 * cleared.
	 * 
	 * @return void
	 */

	public void resetForNextAlgo() {
		boxList = new ArrayList<HBox>();
		totalProcessMemSize = 0;

		for (int i = 0; i < processTextList.size(); i++) {

			anchorpane.getChildren().remove(processTextList.get(i));
		}
		processTextList = new ArrayList<Text>();
		totalMemSize = 0;
		OSsize = 0;
		ramVbox.getChildren().clear();

	}
	
	public void clearRam() {
		boxList = new ArrayList<HBox>();
		totalProcessMemSize = 0;

		for (int i = 0; i < processTextList.size(); i++) {

			anchorpane.getChildren().remove(processTextList.get(i));
		}
		processTextList = new ArrayList<Text>();
		totalMemSize = 0;
		OSsize = 0;
		ramVbox.getChildren().clear();
		
		algoBox.getSelectionModel().clearSelection();
	
	}

	/**
	 * prints the list containing size of block labels.
	 * 
	 * @return void
	 */

	public void printProcessTextList() {

		for (int i = 0; i < processTextList.size(); i++) {

			System.out.print(processTextList.get(i).getText() + " ");
		}
		System.out.println();
	}

	/**
	 * Performs functions that should be ran during initialization.
	 * 
	 * @return void
	 */

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		helpInfo();

		totalMemField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() >= 4 && newValue.length() < 6) {
				selectAlgo();

			}
			if (newValue.length() > 5) {

				alert.failureAlert("Please enter a RAM size no more than 5 digits");
			}

		});

		OSfield.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() >= 3 && newValue.length() < 5) {

				selectAlgo();

			}
			if (newValue.length() > 4) {

				alert.failureAlert("Please enter an OS size no more than 4 digits");
			}

		});

		algoBox.setItems(algos);
		processBox.setItems(processes);
		processBox.getSelectionModel().select(0);

	}

}
