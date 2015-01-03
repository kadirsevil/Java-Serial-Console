import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.application.Platform;

public class Main extends Application {

	public RXTX rxtx = new RXTX();
	public Pane pane = new Pane();

	String a;

	@Override
	public void start(Stage stage) {

		stage.setTitle("Serial Communication Console- 0.9.1");
		Scene scene = new Scene(pane, 600, 350);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();

		// Refresh port list
		new Thread(() -> {
			while (true) {
				refresh();
				Platform.runLater(() -> {
					pane.cbox_com.getItems().clear();
					pane.cbox_com.getItems().addAll(rxtx.portlist);
				});
				try {
					Thread.sleep(200);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}).start();

		// Connection button
		pane.bt_connect.setOnMouseClicked(e -> {
			try {
				if (pane.cbox_baudrates.getValue() != null)
					rxtx.connect(pane.cbox_com.getValue(),
							Integer.parseInt(pane.cbox_baudrates.getValue()),
							pane);

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		});

		// Send button
		pane.bt_send.setOnMouseClicked(e -> {
			rxtx.SerialWriter(pane.text_send.getText());
			pane.text_send.setText("");
			pane.text_send.setPromptText("Write here");

		});

		// Disconnection button
		pane.bt_disconnect.setOnMouseClicked(e -> rxtx.disconnect());

	}

	public static void main(String[] arg0) {
		Application.launch(arg0);
	}

	// Refresh port names
	public void refresh() {
		Enumeration enum_portlist = CommPortIdentifier.getPortIdentifiers();

		rxtx.portlist.clear();
		while (enum_portlist.hasMoreElements()) {
			CommPortIdentifier portId = (CommPortIdentifier) (enum_portlist
					.nextElement());
			rxtx.portlist.add(portId.getName());
		}
	}
}
