package system;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class SerialComm {
	@SuppressWarnings("rawtypes")
	private Enumeration portList;
	private CommPortIdentifier portId;
	private SerialPort serialPort;
	private OutputStream outputStream;
	private InputStream inputStream;

	private String UARTName = "Inconnu";
	private String datas;

	public SerialComm(String portName, int baudrate, int nbData, int nbStop, int parity) {
		UARTName = portName;
		portList = CommPortIdentifier.getPortIdentifiers();

		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals(portName)) {
					try {
						serialPort = (SerialPort) portId.open("BeagleBone", 2000);
					} catch (PortInUseException e) {
						e.printStackTrace();
					}

					try {
						serialPort.setSerialPortParams(baudrate, nbData, nbStop, parity);
					} catch (UnsupportedCommOperationException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void write(String data) {
		System.out.println("Writing data: "+data);
		data+="\r";
		try {
			outputStream = serialPort.getOutputStream();
			outputStream.write(data.getBytes());
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String read() {
		char data;
		datas = "";

		try {
			inputStream = serialPort.getInputStream();

			while (true) {
				data = (char) inputStream.read();
				if (data == 13 || data == 0) {
					System.out.println("Datas: "+datas);
					return datas;
				}
				datas+=data;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getUARTName() {
		return UARTName;
	}
}