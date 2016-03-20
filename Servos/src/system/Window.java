package system;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.Timer;

public class Window extends JFrame implements ActionListener, KeyListener {
	
	private Timer timer = new Timer(1, this);
	private ArrayList<int[]> savedPoints = new ArrayList<int[]>();
	
	// Objects
	@SuppressWarnings("unused")
	private ConnectWindow connectWindow;
	private SerialComm uart = null;
	private String reading;
	private String[] acquisition = new String[3];
	
	// Menu
	private JMenuBar menuBar = new JMenuBar();
	private JMenu mnFile = new JMenu("File");
	private JMenuItem mntmConnection = new JMenuItem("Connection");
	private JMenuItem mntmExit = new JMenuItem("Exit");
	private JMenu mnHelp = new JMenu("?");
	private JMenuItem mntmHelp = new JMenuItem("About");
	
	// Buttons
	private JToggleButton tglbtnAuto = new JToggleButton("Auto");
	private JToggleButton tglbtnManuel = new JToggleButton("Manuel");
	private JToggleButton tglbtnWii = new JToggleButton("Wii");
	private JButton btnStart = new JButton("Start");
	private JButton btnReset = new JButton("Reset");
	
	// Panel
	private Graph graph = new Graph();
	
	// Combobox
	private JComboBox<String> comboBox = new JComboBox<String>();
	
	// Labels
	private JLabel lblMode = new JLabel("Mode de fonctionnement");
	private JLabel lblInfo = new JLabel("Informations");
	private JLabel lblUART = new JLabel("UART: Inconnu");
	private JLabel lblLaser = new JLabel("Laser: Inconnu");
	private JLabel lblPosition = new JLabel("Position: 0,0");
	private JLabel lblShape = new JLabel("Forme: ");
	
	// Fonts
	private Font titleFont = new Font("Trebuchet MS", Font.BOLD, 18);
	private Font globalFont = new Font("Trebuchet MS", Font.BOLD, 16);
	
	/**
	 * Serial key
	 */
	private static final long serialVersionUID = 1L;

	public Window() {
		// Window
		setSize(800, 580);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		setVisible(true);
		
		// Menu
		setJMenuBar(menuBar);
		menuBar.add(mnFile);
		mnFile.add(mntmConnection);
		mnFile.add(mntmExit);
		menuBar.add(mnHelp);
		mnHelp.add(mntmHelp);
		
		mntmConnection.addActionListener(this);
		mntmExit.addActionListener(this);
		mntmHelp.addActionListener(this);
		
		// Buttons
		tglbtnManuel.setSelected(true);
		tglbtnAuto.setEnabled(false);
		tglbtnManuel.setEnabled(false);
		tglbtnWii.setEnabled(false);
		btnStart.setEnabled(false);
		btnReset.setEnabled(false);
		
		tglbtnAuto.setBounds(25, 60, 200, 40);
		tglbtnManuel.setBounds(25, 110, 200, 40);
		tglbtnWii.setBounds(25, 160, 200, 40);
		btnStart.setBounds(47, 500, 80, 40);
		btnReset.setBounds(132, 500, 80, 40);
		
		getContentPane().add(tglbtnAuto);
		getContentPane().add(tglbtnManuel);
		getContentPane().add(tglbtnWii);
		getContentPane().add(btnStart);
		getContentPane().add(btnReset);
		
		tglbtnAuto.addActionListener(this);
		tglbtnManuel.addActionListener(this);
		tglbtnWii.addActionListener(this);
		btnStart.addActionListener(this);
		btnReset.addActionListener(this);
		
		// Panel
		savedPoints = graph.getSavedPoints();
		graph.setBackground(Color.WHITE);
		graph.setBounds(260, 20, 520, 520);
		getContentPane().add(graph);
		
		// Labels
		lblMode.setBounds(22, 20, 300, 15);
		lblInfo.setBounds(74, 240, 300, 15);
		lblUART.setBounds(30, 280, 300, 15);
		lblLaser.setBounds(30, 310, 300, 15);
		lblPosition.setBounds(30, 340, 300, 15);
		lblShape.setBounds(40, 460, 300, 15);

		lblMode.setFont(titleFont);
		lblInfo.setFont(titleFont);
		lblUART.setFont(globalFont);
		lblLaser.setFont(globalFont);
		lblPosition.setFont(globalFont);
		lblShape.setFont(globalFont);
		
		getContentPane().add(lblMode);
		getContentPane().add(lblInfo);
		getContentPane().add(lblUART);
		getContentPane().add(lblLaser);
		getContentPane().add(lblPosition);
		getContentPane().add(lblShape);
		
		// Combobox
		comboBox.setBounds(132, 450, 100, 40);
		comboBox.addItem("Square");
		comboBox.addItem("Diamond");
		comboBox.addItem("Circle");
		comboBox.addItem("Infinite");
		comboBox.addItem("Perso");
		comboBox.setEnabled(false);
		getContentPane().add(comboBox);
		comboBox.addActionListener(this);
		
		////////////
		tglbtnManuel.addKeyListener(this);
		btnStart.addKeyListener(this);
		btnReset.addKeyListener(this);
		comboBox.addKeyListener(this);
		
		timer.start();
	}
	
	public void setUART(SerialComm uart) {
		this.uart = uart;
		lblUART.setText("UART: "+uart.getUARTName());
		
		tglbtnAuto.setEnabled(true);
		tglbtnManuel.setEnabled(true);
		tglbtnWii.setEnabled(true);
		
		tglbtnManuel.requestFocus();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (uart != null) {
			btnStart.setEnabled(tglbtnManuel.isSelected());
			btnReset.setEnabled(tglbtnManuel.isSelected() && comboBox.getSelectedItem().equals("Perso"));
			graph.setManualMode(tglbtnManuel.isSelected() && comboBox.getSelectedItem().equals("Perso"));
			comboBox.setEnabled(tglbtnManuel.isSelected());
		}
		// Timer
		if (e.getSource().equals(timer)) {
			if (uart != null) {
				reading = uart.read();
				if (reading != null) {
					acquisition = reading.split(",");
					try {
						lblPosition.setText("Position: "+Integer.parseInt(acquisition[0])/2+","+Integer.parseInt(acquisition[1])/2);
						
						if (acquisition[2].equals("0")) {
							lblLaser.setText("Laser: Eteint");
							lblLaser.setForeground(Color.DARK_GRAY);
							graph.setLaserActive(false);
						}
						else {
							lblLaser.setText("Laser: Allumé");
							lblLaser.setForeground(Color.RED);
							graph.setLaserActive(true);
						}
						graph.setPointPosition(Integer.parseInt(acquisition[0])/2, Integer.parseInt(acquisition[1])/2);
						graph.repaint();
					}
					catch (NumberFormatException | ArrayIndexOutOfBoundsException exp) {}
				}
			}
		}
		// Buttons
		else if (e.getSource().equals(tglbtnAuto)) {
			if (tglbtnManuel.isSelected() || tglbtnWii.isSelected()) {
				tglbtnAuto.setSelected(true);
				tglbtnManuel.setSelected(false);
				tglbtnWii.setSelected(false);
				savedPoints.clear();
				graph.repaint();
				
				if (uart != null) {
					uart.write("Auto");
					uart.write("Auto");
				}
			}
			else {
				tglbtnAuto.setSelected(true);
			}
		}
		else if (e.getSource().equals(tglbtnManuel)) {
			if (tglbtnAuto.isSelected() || tglbtnWii.isSelected()) {
				tglbtnAuto.setSelected(false);
				tglbtnManuel.setSelected(true);
				tglbtnWii.setSelected(false);
				
				if (uart != null) {
					uart.write("Manual");
					uart.write("Manual");
				}
			}
			else {
				tglbtnManuel.setSelected(true);
			}
		}
		else if (e.getSource().equals(tglbtnWii)) {
			if (tglbtnAuto.isSelected() || tglbtnManuel.isSelected()) {
				tglbtnAuto.setSelected(false);
				tglbtnManuel.setSelected(false);
				tglbtnWii.setSelected(true);
				savedPoints.clear();
				graph.repaint();
				
				if (uart != null) {
					uart.write("Wii");
					uart.write("Wii");
				}
			}
			else {
				tglbtnWii.setSelected(true);
			}
		}
		else if (e.getSource().equals(btnStart)) {
			if (uart != null) {
				uart.write("Semi-auto");
				uart.write("Semi-auto");
				
				if (comboBox.getSelectedItem().equals("Perso")) {
					for (int i=0; i<savedPoints.size(); i++) {
						uart.write(savedPoints.get(i)[0]+","+savedPoints.get(i)[1]+","+savedPoints.get(i)[2]);
					}
				}
				else {
					uart.write((String) comboBox.getSelectedItem());
				}
				uart.write("Finish");
			}
		}
		else if (e.getSource().equals(btnReset)) {
			savedPoints.clear();
			graph.repaint();
		}
		// Combobox
		else if (e.getSource().equals(comboBox)) {
			if (!comboBox.getSelectedItem().equals("Perso")) {
				savedPoints.clear();
				graph.repaint();
			}
		}
		// Menu
		else if (e.getSource().equals(mntmConnection)) {
			connectWindow = new ConnectWindow(this);
		}
		else if (e.getSource().equals(mntmExit)) {
			System.exit(0);
		}
		else if (e.getSource().equals(mntmHelp)) {
			JOptionPane.showMessageDialog(null,  "Developper : F.cardon\nLicence : Freeware\nVersion : "+ServoBeagleBone.VERSION+
					"\nContact : "+ServoBeagleBone.MAIL, "Informations", JOptionPane.NO_OPTION);
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 104) {
			uart.write("Up");
		}
		else if (e.getKeyCode() == 100) {
			uart.write("Left");
		}
		else if (e.getKeyCode() == 102) {
			uart.write("Right");
		}
		else if (e.getKeyCode() == 98) {
			uart.write("Down");
		}
		else if (e.getKeyCode() == 101) {
			uart.write("Center");
		}
		else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			uart.write("Laser");
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {}
}
