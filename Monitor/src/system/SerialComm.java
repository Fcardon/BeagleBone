package system;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

/**
 * La classe {@code SerialComm} permet de configurer un port série où l'on pourra lire et écrire en "UART" sur le port sélectionné
 * @author Fabien CARDON
 */
public class SerialComm {
	@SuppressWarnings("rawtypes")
	/** Enumération des ports */
	private Enumeration portList;
	/** Port indentifier */
	private CommPortIdentifier portId;
	/** Port serie sélectionné */
	private SerialPort serialPort;
	/** Buffer sortant de l'UART */
	private OutputStream outputStream;
	/** Buffer entrant de l'UART */
	private InputStream inputStream;

	/** Objet permettant de gérer le timeout */
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	
	/** Nom de l'UART sélectionné */
	private String UARTName = "Inconnu";
	/** Datas des données reçu de l'UART */
	private String datas;

	/**
	 * Constructeur de la classe {@code SerialComm} permettant de configurer et de se connecter à un port série
	 * @param portName Nom du port série
	 * @param baudrate Vitesse de communication
	 * @param nbData Nombre de bits de data
	 * @param nbStop Nombre de bits de stop
	 * @param parity Parité du port série
	 */
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

	/**
	 * Ecrit une donné sur l'UART
	 * @param data Donnée à envoyer
	 */
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

	/** Lit une donné sur l'UART */
	public String read() {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final Future<String> handler = executor.submit(new Callable() {
			@Override
			public String call() throws Exception {
				return readWithTimeout();
			}
		});
		
		try {
			return handler.get(100, TimeUnit.MILLISECONDS); // Timeout de 100ms
		} catch (TimeoutException | InterruptedException | ExecutionException e) {
			handler.cancel(true);
			return null;
		}
	}
	
	/** Lit une donné sur l'UART cette méthodes et executé avec un timeout qui permet de ne pas bloquer l'IHM */
	private String readWithTimeout() {
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

	/** @return Nom de l'UART sélectionné */
	public String getUARTName() {
		return UARTName;
	}
}