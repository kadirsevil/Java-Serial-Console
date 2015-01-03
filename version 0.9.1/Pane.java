import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class Pane extends GridPane {

	ComboBox<String> cbox_com = new ComboBox<String>();
	ComboBox<String> cbox_baudrates = new ComboBox<String>();

	final String[] str_baudrates = { "300", "600", "1200", "2400", "4800",
			"9600", "14400", "19200", "28800", "38400", "57600", "115200" };

	Button bt_connect = new Button("Connect");
	Button bt_disconnect = new Button("Disconnect");
	Button bt_send = new Button("Send");

	final TextArea texta_console = new TextArea();

	TextField text_send = new TextField();

	public Pane() {
		this.setVgap(10);
		this.setHgap(10);
		this.setPadding(new Insets(15, 15, 15, 15));

		cbox_com.setPrefSize(125, 15);
		cbox_com.setPromptText("Port name");

		cbox_baudrates.setPrefSize(120, 15);
		cbox_baudrates.setPromptText("Baudrate");
		cbox_baudrates.getItems().addAll(str_baudrates);

		ScrollPane scrollpane = new ScrollPane(texta_console);

		text_send.setPromptText("Write here");

		this.add(text_send, 0, 0, 6, 1);// 1. row
		this.add(bt_send, 6, 0);

		this.add(scrollpane, 0, 1, 6, 1);// 2. row

		this.add(cbox_com, 1, 2);// 3. row
		this.add(cbox_baudrates, 3, 2);
		this.add(bt_connect, 4, 2);
		this.add(bt_disconnect, 5, 2);

	}

	public void writeConsole(String buffer) {
		texta_console.appendText(buffer + "\n");
	}

	public void clearConsole() {
		texta_console.clear();
	}
}
