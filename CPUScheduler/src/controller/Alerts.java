package controller;



import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

/**
 * 
 * 
 * The alert class serves to open pop up dialogs with custom messages as needed by the GUI.
 * 
 * @see <A href="../src/model/Alerts.java">Java
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
public class Alerts {
	private static Label actionStatus = new Label("");

	public void SuccessAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Welcome");
		alert.setHeaderText("Read Me!");
		String s = message;
		
		
		
		alert.setContentText(s);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(getClass().getResourceAsStream("acmappy.gif")));
		
		
		
		stage.showAndWait();
	}

	public void failureAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Alert!");
		alert.setHeaderText("Information Alert");
		String s = message;
		alert.setContentText(s);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(getClass().getResourceAsStream("redalert.png")));
		stage.showAndWait();
	}

	public static Label getActionStatus() {
		return actionStatus;
	}

}
