package view;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

/**
 * 
 * 
 * Main class to initialize the GUI.
 * 
 * @see <A href="../src/model/Main.java">Java
 *      sourceCode</A>
 * 
 * 
 * @author Mike Spadaro <A href="mailto:mspadaro@student.sjcny.edu">
 *         mspadaro@student.sjcny.edu </A>
 * 
 * @version v1.0, 11/31/2019
 * 
 * 
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("MainView.fxml"));
			Scene scene = new Scene(root,800,800);
			scene.getStylesheets().add(getClass().getResource("mike.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("acmappy.gif")));
			primaryStage.setTitle("Mem Man");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
