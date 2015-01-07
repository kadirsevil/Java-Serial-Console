package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class SerialConsole extends Application {
	private String version = "Serial Communication Console- 0.9.2";

	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource(
					"UITemplate.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle(version);
			stage.show();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
