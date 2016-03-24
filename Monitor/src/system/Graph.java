package system;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * La classe {@code Graph}, étendu de {@code JPanel}, créé un graphique représentant la position réelle du laser
 * @author Fabien CARDON
 * 
 * @see JPanel
 */
public class Graph extends JPanel implements MouseMotionListener {

	/** Serial key */
	private static final long serialVersionUID = 1L;

	/** Objet graphique qui permet de dessiner des formes (lignes, points...) */
	private Graphics2D g2d;

	// Axes et grille
	/** Hauteur des graduations des axes du graphique */
	private int hauteurTrait = 5;
	/** Nombre de graduation d'un seul axe du graphique */
	private int nbTrait = 6;
	/** Coefficients servant à définir l'espace entre les graduations des axes */
	private float coef = 51.8f;

	// Point
	/** Position du point indiquant le laser suivant l'axe des X relative au JPanel */
	private int xPoint = 260;
	/** Position du point indiquant le laser suivant l'axe des Y relative au JPanel */
	private int yPoint = 260;
	/** Taille du point indiquant le laser */
	private int pointSize = 20;
	/** Indique l'état du laser (allumé ou éteint) */
	private boolean laserActive = false;

	// Saved points
	/** Position futur du point indiquant le laser suivant l'axe des X relative au JPanel */
	private int xSavedPoint = 260;
	/** Position futur du point indiquant le laser suivant l'axe des Y relative au JPanel */
	private int ySavedPoint = 260;
	/** Taille des points futur indiquant le laser */
	private int savedPointSize = 20;
	/** Listes de la position et l'état de tout les points pour réaliser une figure personnalisé */
	private ArrayList<int[]> savedPoints = new ArrayList<int[]>();

	/** Indique si l'IHM est en mode manuel */
	private boolean manualMode = false;


	/** Constructeur de l'objet Graph */
	public Graph() {
		addMouseMotionListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2d = (Graphics2D) g;
		
		// Affichage des axes, de la grille et des points du laser
		drawAxis(g2d);
		drawGrid(g2d);
		drawSavedPoint(g2d);
		drawPoint(g2d);
	}	

	/**
	 * Dessine les axes du graphique ainsi que sa graduation
	 * @param g2d Objet graphique qui permet de dessiner des formes (lignes, points...)
	 */
	private void drawAxis(Graphics2D g2d) {
		// Type de ligne
		Stroke stroke = new BasicStroke(3);
		g2d.setStroke(stroke);

		// Axes
		g2d.drawLine(-10, getHeight()/2, getWidth()+10, getHeight()/2);// Ligne horizontal
		g2d.drawLine(getWidth()/2, -10, getWidth()/2, getHeight()+10);// Ligne vertical

		// Graduations
		for (int i=0; i<nbTrait; i++) {
			g2d.drawLine((int) (getWidth()/2+i*coef), getWidth()/2+hauteurTrait, (int) (getWidth()/2+i*coef), getWidth()/2-hauteurTrait);
			g2d.drawLine(getHeight()/2-hauteurTrait, (int) (getHeight()/2+i*coef), getHeight()/2+hauteurTrait, (int) (getHeight()/2+i*coef));
			coef*=-1;
			g2d.drawLine((int) (getWidth()/2+i*coef), getWidth()/2+hauteurTrait, (int) (getWidth()/2+i*coef), getWidth()/2-hauteurTrait);
			g2d.drawLine(getHeight()/2-hauteurTrait, (int) (getHeight()/2+i*coef), getHeight()/2+hauteurTrait, (int) (getHeight()/2+i*coef));
			coef*=-1;
		}
	}
	/**
	 * Dessine la grille du graphique
	 * @param g2d Objet graphique qui permet de dessiner des formes (lignes, points...)
	 */
	private void drawGrid(Graphics2D g2d) {
		// Type de ligne
		Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{8}, 0);
		g2d.setStroke(dashed);

		// Grille
		for (int i=0; i<nbTrait; i++) {
			g2d.drawLine((int) (getWidth()/2+i*coef), (int) (getHeight()/2-i*coef), (int) (getWidth()/2+i*coef), (int) (getHeight()/2+i*coef));
			g2d.drawLine((int) (getWidth()/2-i*coef), (int) (getHeight()/2+i*coef), (int) (getWidth()/2+i*coef), (int) (getHeight()/2+i*coef));
			coef*=-1;
			g2d.drawLine((int) (getWidth()/2+i*coef), (int) (getHeight()/2-i*coef), (int) (getWidth()/2+i*coef), (int) (getHeight()/2+i*coef));
			g2d.drawLine((int) (getWidth()/2-i*coef), (int) (getHeight()/2+i*coef), (int) (getWidth()/2+i*coef), (int) (getHeight()/2+i*coef));
			coef*=-1;
		}
	}
	/**
	 * Dessine le point indiquant la position du laser
	 * @param g2d Objet graphique qui permet de dessiner des formes (lignes, points...)
	 */
	private void drawPoint(Graphics2D g2d) {
		Stroke stroke = new BasicStroke(3);
		g2d.setStroke(stroke);
		g2d.setColor(Color.RED);

		if (laserActive) {
			g2d.fillArc(xPoint-pointSize/2, yPoint-pointSize/2, pointSize, pointSize, 0, 360);
		}
		else {
			g2d.drawArc(xPoint-pointSize/2, yPoint-pointSize/2, pointSize, pointSize, 0, 360);
		}
	}
	/**
	 * Dessine les points indiquant la position futur du laser
	 * @param g2d Objet graphique qui permet de dessiner des formes (lignes, points...)
	 */
	private void drawSavedPoint(Graphics2D g2d) {
		Stroke stroke = new BasicStroke(3);
		g2d.setStroke(stroke);
		g2d.setColor(Color.BLUE);

		for (int i=0; i<savedPoints.size(); i++) {
			xSavedPoint = (int) (5.2*savedPoints.get(i)[0]+260);
			ySavedPoint = (int) (-5.2*savedPoints.get(i)[1]+260);
			if (savedPoints.get(i)[2] == 1) {
				g2d.fillArc(xSavedPoint-savedPointSize/2, ySavedPoint-savedPointSize/2, savedPointSize, savedPointSize, 0, 360);
			}
			else {
				g2d.drawArc(xSavedPoint-savedPointSize/2, ySavedPoint-savedPointSize/2, savedPointSize, savedPointSize, 0, 360);
			}
		}
	}

	/**
	 * Change la position du point du laser
	 * @param x Position du point sur l'axe des X
	 * @param y Position du point sur l'axe des Y
	 */
	public void setPointPosition(int x, int y) {
		xPoint = (int) (5.2*x+260);
		yPoint = (int) (-5.2*y+260);
	}
	/**
	 * Change l'état du point du laser (Allumé ou éteint)
	 * @param laserActive Nouvel état du point du laser
	 */
	public void setLaserActive(boolean laserActive) {
		this.laserActive = laserActive;
	}
	/**
	 * Change le mode de l'IHM
	 * @param manualMode Mettre True si le mode de l'IHM est le mode "Manuel"
	 */
	public void setManualMode(boolean manualMode) {
		this.manualMode = manualMode;;
	}
	/** @return Liste des points précédemment dessiné sur le graphique */
	public ArrayList<int[]> getSavedPoints() {
		return savedPoints;
	}

	@Override
	public void mouseDragged(MouseEvent mouse) {
		// Créé des points sur le graphique et les ajoute à une liste
		if (manualMode) {
			int realX = (int) (0.1923*mouse.getX()-50);
			int realY = (int) (-0.1923*mouse.getY()+50);
			if (SwingUtilities.isLeftMouseButton(mouse)) {
				savedPoints.add(new int[] {realX, realY, 1});
			}
			else if (SwingUtilities.isRightMouseButton(mouse)) {
				savedPoints.add(new int[] {realX, realY, 0});
			}
			repaint();
			try {
				Thread.sleep(10); // Une attente de 10ms et réalisé pour ne pas qu'il y est trop de point dessiné sur le même endroit
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {}
}
