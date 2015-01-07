package application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UIController {

	RXTX rxtx = new RXTX();

	@FXML
	Button bt_connect;

	@FXML
	Button bt_disconnect;

	@FXML
	Button bt_send;

	@FXML
	TextArea txta_console;

	@FXML
	TextField txt_send;

	@FXML
	ComboBox<String> cbox_com;

	@FXML
	ComboBox<String> cbox_baudrates;

	final String[] str_baudrates = { "300", "600", "1200", "2400", "4800",
			"9600", "14400", "19200", "28800", "38400", "57600", "115200" };

	@FXML
	void initialize() {

		assert bt_connect != null : "fx: id=\"bt_connect\"was not injected: check your FXML file 'UITemplate.fxml'.";
		assert bt_disconnect != null : "fx: id=\"bt_disconnect\"was not injected: check your FXML file 'UITemplate.fxml'.";
		assert bt_send != null : "fx: id=\"bt_send\"was not injected: check your FXML file 'UITemplate.fxml'.";
		assert txta_console != null : "fx: id=\"txta_console\"was not injected: check your FXML file 'UITemplate.fxml'.";
		assert txt_send != null : "fx: id=\"txt_send\"was not injected: check your FXML file 'UITemplate.fxml'.";
		assert cbox_com != null : "fx: id=\"cbox_com\"was not injected: check your FXML file 'UITemplate.fxml'.";
		assert cbox_baudrates != null : "fx: id=\"cbox_baudrates\"was not injected: check your FXML file 'UITemplate.fxml'.";

		cbox_com.setPromptText("Port name");

		cbox_baudrates.setPromptText("Baudrate");
		cbox_baudrates.getItems().addAll(str_baudrates);

		txt_send.setPromptText("Write here");

		// Refresh port cbox_com
		new Thread(() -> {
			while (true) {
				Platform.runLater(() -> {
					cbox_com.getItems().clear();
					cbox_com.getItems().addAll(rxtx.portlist);
				});
				try {
					Thread.sleep(200);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}).start();

		// Connection button
		bt_connect.setOnMouseClicked(e -> {
			try {
				if (cbox_baudrates.getValue() != null)
					rxtx.connect(cbox_com.getValue(),
							Integer.parseInt(cbox_baudrates.getValue()),
							txta_console);

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		});

		// Send button
		bt_send.setOnMouseClicked(e -> {
			rxtx.SerialWriter(txt_send.getText());
			txt_send.setText("");
			txt_send.setPromptText("Write here");

		});

		// Disconnection button
		bt_disconnect.setOnMouseClicked(e -> rxtx.disconnect());

	}

}
