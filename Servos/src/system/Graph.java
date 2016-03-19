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

public class Graph extends JPanel implements MouseMotionListener {

	/**
	 * Serail key
	 */
	private static final long serialVersionUID = 1L;

	private Graphics2D g2d;

	// Axes et grille
	private int hauteurTrait = 5;
	private int nbTrait = 6;
	private float coef = 51.8f;

	// Point
	private int xPoint = 260;
	private int yPoint = 260;
	private int pointSize = 20;
	private boolean laserActive = false;

	// Saved points
	private int xSavedPoint = 260;
	private int ySavedPoint = 260;
	private int savedPointSize = 20;
	private ArrayList<int[]> savedPoints = new ArrayList<int[]>();

	private boolean manualMode = false;


	public Graph() {
		addMouseMotionListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2d = (Graphics2D) g;

		drawAxis(g2d);
		drawGrid(g2d);
		drawSavedPoint(g2d);
		drawPoint(g2d);
	}	

	private void drawAxis(Graphics2D g2d) {
		Stroke stroke = new BasicStroke(3);
		g2d.setStroke(stroke);

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
	private void drawGrid(Graphics2D g2d) {
		Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{8}, 0);
		g2d.setStroke(dashed);

		for (int i=0; i<nbTrait; i++) {
			g2d.drawLine((int) (getWidth()/2+i*coef), (int) (getHeight()/2-i*coef), (int) (getWidth()/2+i*coef), (int) (getHeight()/2+i*coef));
			g2d.drawLine((int) (getWidth()/2-i*coef), (int) (getHeight()/2+i*coef), (int) (getWidth()/2+i*coef), (int) (getHeight()/2+i*coef));
			coef*=-1;
			g2d.drawLine((int) (getWidth()/2+i*coef), (int) (getHeight()/2-i*coef), (int) (getWidth()/2+i*coef), (int) (getHeight()/2+i*coef));
			g2d.drawLine((int) (getWidth()/2-i*coef), (int) (getHeight()/2+i*coef), (int) (getWidth()/2+i*coef), (int) (getHeight()/2+i*coef));
			coef*=-1;
		}
	}
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

	public void setPointPosition(int x, int y) {
		xPoint = (int) (5.2*x+260);
		yPoint = (int) (-5.2*y+260);
	}

	public void setLaserActive(boolean laserActive) {
		this.laserActive = laserActive;
	}

	public void setManualMode(boolean manualMode) {
		this.manualMode = manualMode;;
	}

	public ArrayList<int[]> getSavedPoints() {
		return savedPoints;
	}

	@Override
	public void mouseDragged(MouseEvent mouse) {
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
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {}
}
