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

public class ConnectWindow extends JDialog implements ActionListener {

	/**
	 * Serial key
	 */
	private static final long serialVersionUID = 1L;
	
	private SerialComm uart = null;
	private Window window;
	
	// Display
	private JLabel lblPort = new JLabel("Port: ");
	private JComboBox<String> comboBox = new JComboBox<String>();
	private JButton btnConnect = new JButton("Connect");

	private Font globalFont = new Font("Trebuchet MS", Font.BOLD, 16);

	public ConnectWindow(Window window) {
		this.window = window;
		
		setTitle("Connection");
		setSize(400, 100);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(null);
		setResizable(false);
		setVisible(true);
		
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
