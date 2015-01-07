package application;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class RXTX {

	public ArrayList<String> portlist = new ArrayList<String>();
	public String connnectedPortName = "";

	private SerialPort serialPort;
	private Thread th_in;

	private BufferedWriter out;

	TextArea txta_console = null;

	public RXTX() {

		// refresh portlist array
		new Thread(() -> {
			while (true) {
				refresh();
				try {
					Thread.sleep(170);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}).start();
	}

	void connect(String portName, int baudrate, TextArea txta_console)
			throws Exception {

		if (portName == null || connnectedPortName.equals(portName))
			return;

		this.txta_console = txta_console;

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
							writeConsole(buffer);
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
			clearConsole();
		}
	}

	public void SerialWriter(String buffer) {
		if (out == null)
			return;

		try {
			out.write(buffer);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void writeConsole(String buffer) {
		txta_console.appendText(buffer + "\n");
	}

	public void clearConsole() {
		txta_console.clear();
	}

	// Refresh port names
	public void refresh() {
		Enumeration enum_portlist = CommPortIdentifier.getPortIdentifiers();

		portlist.clear();
		while (enum_portlist.hasMoreElements()) {
			CommPortIdentifier portId = (CommPortIdentifier) (enum_portlist
					.nextElement());
			portlist.add(portId.getName());
		}
	}

}