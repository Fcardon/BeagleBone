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
	
	/** Timer permettant de cadencer la lecture du port série connecter à la Beaglebone à 1ms */
	private Timer timer = new Timer(1, this);
	/** Listes de la position et l'état de tout les points pour réaliser une figure personnalisé */
	private ArrayList<int[]> savedPoints = new ArrayList<int[]>();
	
	// Objects
	@SuppressWarnings("unused")
	/** Fenêtre permettant de se conneter à un port série */
	private ConnectWindow connectWindow;
	/** Port série que l'on désire lire et écrire pour communiquer avec la Beaglebone */
	private SerialComm uart = null;
	/** Donnée reçu sur le port série */
	private String reading;
	/** Tableau séparant les différentes données reçu sur le port série */
	private String[] acquisition = new String[3];
	
	// Menu
	/** Objet créant la barre de menus de la fenêtre */
	private JMenuBar menuBar = new JMenuBar();
	/** Menu "File" de la barre de menus */
	private JMenu mnFile = new JMenu("File");
	/** Item "Connection" du menu "File" */
	private JMenuItem mntmConnection = new JMenuItem("Connection");
	/** Item "Exit" du menu "File" */
	private JMenuItem mntmExit = new JMenuItem("Exit");
	/** Menu "?" de la barre de menus */
	private JMenu mnHelp = new JMenu("?");
	/** Item "About" du menu "?" */
	private JMenuItem mntmHelp = new JMenuItem("About");
	
	// Buttons
	/** Bouton changeant le mode de la Beaglebone en "Auto" */
	private JToggleButton tglbtnAuto = new JToggleButton("Auto");
	/** Bouton changeant le mode de la Beaglebone en "Manuel" */
	private JToggleButton tglbtnManuel = new JToggleButton("Manuel");
	/** Bouton changeant le mode de la Beaglebone en "Wii" */
	private JToggleButton tglbtnWii = new JToggleButton("Wii");
	/** Bouton servant a envoyer à la Beaglebone les actions à réaliser dans le mode "Manuel" */
	private JButton btnStart = new JButton("Start");
	/** Bouton effacant tout les points enregistré pour créer une figure personnalisée */
	private JButton btnReset = new JButton("Reset");
	
	// Panel
	/** Objet affichant la position du laser ainsi que les différents points pour réaliser une figure personnalisée */
	private Graph graph = new Graph();
	
	// Combobox
	/** Combobox de sélection de type de figure à envoyer à la Beaglebone */
	private JComboBox<String> combobox = new JComboBox<String>();
	
	// Labels
	/** Label titre pour la partie concernant les modes */
	private JLabel lblMode = new JLabel("Mode de fonctionnement");
	/** Label titre pour la partie concernant les autres informations */
	private JLabel lblInfo = new JLabel("Informations");
	/** Label indiquant le nom de l'UART connecté avec l'IHM */
	private JLabel lblUART = new JLabel("UART: Inconnu");
	/** Label indiquant l'état du laser (allumé ou éteint) */
	private JLabel lblLaser = new JLabel("Laser: Inconnu");
	/** Label indiquant la position du laser */
	private JLabel lblPosition = new JLabel("Position: 0,0");
	/** Label associé au combobox choisissant les formes */
	private JLabel lblShape = new JLabel("Forme: ");
	
	// Fonts
	/** Police de caractères des titres de la fenêtre */
	private Font titleFont = new Font("Trebuchet MS", Font.BOLD, 18);
	/** Police de caractères générale de la fenêtre */
	private Font globalFont = new Font("Trebuchet MS", Font.BOLD, 16);
	
	/** Serial key */
	private static final long serialVersionUID = 1L;

	/** Constructeur qui gére et affiche l'application générale */
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
		combobox.setBounds(132, 450, 100, 40);
		combobox.addItem("Square");
		combobox.addItem("Diamond");
		combobox.addItem("Circle");
		combobox.addItem("Infinite");
		combobox.addItem("Perso");
		combobox.setEnabled(false);
		getContentPane().add(combobox);
		combobox.addActionListener(this);
		
		// Listeners
		tglbtnManuel.addKeyListener(this);
		btnStart.addKeyListener(this);
		btnReset.addKeyListener(this);
		combobox.addKeyListener(this);
		
		timer.start();// Démarrage de la lecture du port série
	}

	/**
	 * Met à jour les infos du port série
	 * @param uart Port série connecté à l'IHM
	 */
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
			btnReset.setEnabled(tglbtnManuel.isSelected() && combobox.getSelectedItem().equals("Perso"));
			graph.setManualMode(tglbtnManuel.isSelected() && combobox.getSelectedItem().equals("Perso"));
			combobox.setEnabled(tglbtnManuel.isSelected());
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
				
				// Envoi de tout les points sur l'UART
				if (combobox.getSelectedItem().equals("Perso")) {
					for (int i=0; i<savedPoints.size(); i++) {
						uart.write(savedPoints.get(i)[0]+","+savedPoints.get(i)[1]+","+savedPoints.get(i)[2]);
					}
				}
				else {
					uart.write((String) combobox.getSelectedItem());
				}
				uart.write("Finish");
			}
		}
		else if (e.getSource().equals(btnReset)) {
			savedPoints.clear();
			graph.repaint();
		}
		// Combobox
		else if (e.getSource().equals(combobox)) {
			if (!combobox.getSelectedItem().equals("Perso")) {
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
		// Touche "8"
		if (e.getKeyCode() == 104) {
			uart.write("Up");
		}
		// Touche "4"
		else if (e.getKeyCode() == 100) {
			uart.write("Left");
		}
		// Touche "6"
		else if (e.getKeyCode() == 102) {
			uart.write("Right");
		}
		// Touche "2"
		else if (e.getKeyCode() == 98) {
			uart.write("Down");
		}
		// Touche "5"
		else if (e.getKeyCode() == 101) {
			uart.write("Center");
		}
		// Touche "Entrée"
		else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			uart.write("Laser");
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {}
}
