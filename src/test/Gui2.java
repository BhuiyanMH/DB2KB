package test;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.RoundRectangle2D.Double;

import javax.swing.*;

import riotcmd.infer;
import scala.deprecatedOverriding;

public class Gui2 extends JFrame {
	JFrame frame = new JFrame();
	MyDrawPanel drawpanel = new MyDrawPanel();

	public static void main(String[] args) {
		Gui2 gui = new Gui2();
		gui.go();
	}

	public void go() {

		frame.getContentPane().add(drawpanel);
		// frame.addMouseListener(this);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 300);
		frame.setVisible(true);

	}

}

class MyDrawPanel extends JComponent implements MouseListener {

	Ellipse2D oval = new Ellipse2D.Double(70, 70, 100, 100);
	RoundRectangle2D rectangle2d ;

	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		this.addMouseListener(this);

		
		g2d.setPaint(Color.CYAN);
		g2d.fill(oval);
//
//		/*
//		 * Graphics2D g2;
//		double thickness = 2;
//		Stroke oldStroke = g2.getStroke();
//		g2.setStroke(new BasicStroke(thickness));
//		g2.drawRect(x, y, width, height);
//		g2.setStroke(oldStroke);*/
//		
//		g2d.setStroke(new BasicStroke(3));
//		g2d.setPaint(Color.BLACK);
//		drawArrowLine(g, 110, 110, 110, 400, 10, 10);
//		
//		g2d.setColor(Color.RED);
//		//g2d.drawRect(c, d, a, b);
//		
//		g2d.setFont(new Font("default", Font.BOLD, 16));
//		g2d.drawString("Hi", (70+100)/2, (70+100)/2);
//
//		drawArrowHead(g2d);
//		// [...]
//	
//		//draw a triangle
//		//g2d.drawPolygon(new int[] {10, 20, 30}, new int[] {100, 20, 100}, 3);
//		g2d.drawPolygon(new int[] {10, 20, 30}, new int[] {55, 70, 55}, 3);
//		
//		Polygon arrowHead = new Polygon();
//		arrowHead.addPoint(10, 55);
//		arrowHead.addPoint(20, 70);
//		arrowHead.addPoint(30, 55);
//		
//		g2d.fill(arrowHead);
//		
		
		
		rectangle2d = drawRectangle(g, 200, 200, "ABox Generation");
		drawArrow(g, 100, 200, 200, 250, Color.RED);;
	}

	private void drawArrowHead(Graphics2D g2d) {
		
		
		AffineTransform tx = new AffineTransform();
		Line2D.Double line = new Line2D.Double(0,0,100,100);

		Polygon arrowHead = new Polygon();  
		arrowHead.addPoint( 0,5);
		arrowHead.addPoint( -5, -5);
		arrowHead.addPoint( 5,-5);
		
		
	    tx.setToIdentity();
	    double angle = Math.atan2(line.y2-line.y1, line.x2-line.x1);
	    tx.translate(line.x2, line.y2);
	    tx.rotate((angle-Math.PI/2d));  

	    Graphics2D g = (Graphics2D) g2d.create();
	    g.setTransform(tx);   
	    g.fill(arrowHead);
	    g.dispose();
	    
	}
	
	private void drawArrow(Graphics graphics, int x1, int y1, int x2, int y2, Color color){
		
		Graphics2D graphics2d = (Graphics2D)graphics;
		graphics2d.setStroke(new BasicStroke(3));
		graphics.setColor(color);
		
		if(y1!=y2){
			
			if(y1<y2){
				
				graphics2d.drawLine(x1, y1, x1, y1+(y2-y1));
				graphics2d.drawLine(x1, y2, x2-10, y2);
				
			}else{
				
				graphics2d.drawLine(x1, y1, x1, y2-(y2-y1));
				graphics2d.drawLine(x1, y2, x2-10, y2);
			}
			
		}
		else{
			graphics2d.drawLine(x1, y1, x2-10, y2);
		}
		
		Polygon arrowHead = new Polygon();
		arrowHead.addPoint(x2-10, y2-10);
		arrowHead.addPoint(x2, y2);
		arrowHead.addPoint(x2-10, y2+10);
		graphics2d.fill(arrowHead);
		
	}
	
	private RoundRectangle2D drawRectangle(Graphics graphics, double x, double y, String text){
		
		Graphics2D graphics2d = (Graphics2D)graphics;
		graphics2d.setColor(Color.BLACK);
		graphics2d.setStroke(new BasicStroke(3));
		
		
		//Default Height = 100, difault width = 200
		RoundRectangle2D rectangle = new RoundRectangle2D.Double(x, y, 200, 100, 10, 10);
		graphics2d.draw(rectangle);
		
		Font font = new Font("Tahoma", Font.BOLD, 16);
		graphics2d.setFont(font);
		
		FontMetrics fontMetrics = graphics2d.getFontMetrics();
		
		// Determine the X coordinate for the text
	    int xCor = (int) (rectangle.getX() + (rectangle.getWidth() - fontMetrics.stringWidth(text)) / 2);
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int yCor = (int) (rectangle.getY() + ((rectangle.getHeight() - fontMetrics.getHeight()) / 2) + fontMetrics.getAscent());
	    // Draw the String
	    graphics2d.drawString(text, xCor, yCor);
	    
	    return rectangle;
	    
	}
	
	int cnt = 0;
	@Override
	public void mouseClicked(MouseEvent e) {
		cnt++;
		if ((e.getButton() == 1) && rectangle2d.contains(e.getX(), e.getY())) {

			//JOptionPane.showMessageDialog(null, "Inside of oval");
			
			System.out.println("Count "+cnt+" Max X "+rectangle2d.getMaxX()+" Max Y "+ rectangle2d.getMaxY());
			repaint();
		} else {
			//JOptionPane.showMessageDialog(null, "Out side of oval");
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h) {
		int dx = x2 - x1, dy = y2 - y1;
		double D = Math.sqrt(dx * dx + dy * dy);
		double xm = D - d, xn = xm, ym = h, yn = -h, x;
		double sin = dy / D, cos = dx / D;

		x = xm * cos - ym * sin + x1;
		ym = xm * sin + ym * cos + y1;
		xm = x;

		x = xn * cos - yn * sin + x1;
		yn = xn * sin + yn * cos + y1;
		xn = x;

		int[] xpoints = { x2, (int) xm, (int) xn };
		int[] ypoints = { y2, (int) ym, (int) yn };

		g.drawLine(x1, y1, x2, y2);
		g.fillPolygon(xpoints, ypoints, 3);
	}

}