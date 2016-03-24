package system;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

/**
 * La classe {@code ConnectWindow}, étendu de {@code JDialog}, créé une fenêtre de configuration concernant la connexion à un port série "UART"
 * @author Fabien CARDON
 * 
 * @see JDialog
 */
public class ConnectWindow extends JDialog implements ActionListener {

	/** Serial key */
	private static final long serialVersionUID = 1L;
	
	/** Objet servant pour la gestion de l'UART */
	private SerialComm uart = null;
	/** Fenêtre principal de l'IHM */
	private Window window;
	
	// Display
	/** Label attaché au Combobox pour la selection du port */
	private JLabel lblPort = new JLabel("Port: ");
	/** Combobox listant tout les ports non utilisés */
	private JComboBox<String> comboBox = new JComboBox<String>();
	/** Bouton de connexion au port selectionné */
	private JButton btnConnect = new JButton("Connect");

	/** Police de caractères générale des labels de la fenêtre */
	private Font globalFont = new Font("Trebuchet MS", Font.BOLD, 16);

	/**
	 * Constructeur affichant une fenêtre de connexion pour l'UART
	 * @param window Fenêtre principal de l'IHM
	 */
	public ConnectWindow(Window window) {
		this.window = window;
		
		// Fenêtre
		setTitle("Connection");
		setSize(400, 100);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(null);
		setResizable(false);
		setVisible(true);
		
		// Objet de la fenêtre
		lblPort.setFont(globalFont);
		
		lblPort.setBounds(15, 30, 170, 15);
		comboBox.setBounds(80, 16, 170, 40);
		btnConnect.setBounds(280, 21, 100, 30);
		
		btnConnect.addActionListener(this);
		
		add(lblPort);
		add(comboBox);
		add(btnConnect);
		
		setCombobixList();
	}
	
	/**
	 * Ajoute au combobox la liste des ports série disponible pour la connexion de l'IHM
	 */
	private void setCombobixList() {
		@SuppressWarnings("rawtypes")
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();
		CommPortIdentifier portId;
		while (portList.hasMoreElements()){
			portId=(CommPortIdentifier)portList.nextElement();
			comboBox.addItem(portId.getName());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Bouton
		if (e.getSource().equals(btnConnect)) {
			if (comboBox.getItemCount() != 0) {
				uart = new SerialComm((String) comboBox.getSelectedItem(), 115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				window.setUART(uart);
			}
			else {
				JOptionPane.showMessageDialog(null, "L'application ne sait connecté à aucun port série !", "Warning", JOptionPane.WARNING_MESSAGE);
			}
			dispose();
		}
	}
}
