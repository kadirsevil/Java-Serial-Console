import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javafx.application.Platform;

public class RXTX {

	public ArrayList<String> portlist = new ArrayList<String>();
	public String connnectedPortName = "";

	private SerialPort serialPort;
	private Thread th_in;

	private BufferedWriter out;

	Pane paneClass = null;

	void connect(String portName, int baudrate, Pane paneClass)
			throws Exception {

		if (portName == null || connnectedPortName.equals(portName))
			return;

		this.paneClass = paneClass;
		CommPortIdentifier portIdentifier = CommPortIdentifier
				.getPortIdentifier(portName);

		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(),
					2000);

			if (commPort instanceof SerialPort) {
				serialPort = (SerialPort) commPort;

				serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				BufferedReader in = new BufferedReader(new InputStreamReader(
						serialPort.getInputStream()));
				out = new BufferedWriter(new OutputStreamWriter(
						serialPort.getOutputStream()));

				th_in = new Thread(new SerialReader(in));
				th_in.start();

				System.out.println("Connected: " + portIdentifier.getName());

				connnectedPortName = portName;
			}

			else
				System.out
						.println("Error: Only serial ports are handled by this example.");
		}
	}

	public class SerialReader implements Runnable {
		BufferedReader in;
		String buffer = "";

		public SerialReader(BufferedReader in) {
			this.in = in;
		}

		public void run() {

			while (buffer != null) {
				try {
					while ((buffer = in.readLine()) != null) {

						Platform.runLater(() -> {
							paneClass.writeConsole(buffer);
						});

					}
				} catch (IOException e) {
				}
			}
		}
	}

	public void disconnect() {
		if (!connnectedPortName.equals("")) {
			th_in.stop();
			serialPort.close();
			System.out.println("Disconnected: " + connnectedPortName);
			connnectedPortName = "";
			paneClass.clearConsole();
		}
	}

	public void SerialWriter(String buffer) {
		if (out == null)
			return;
		try {
			out.write(buffer, 0, buffer.length());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}