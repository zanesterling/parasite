package Parasite.ui;

import Parasite.sim.Simulation;
import Parasite.ui.widget.UIWidget;
import Parasite.ui.widget.GameWidget;

import java.util.ArrayList;
import java.util.LinkedList;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class UI implements MouseListener,KeyListener {

	private static final int CANVAS_WIDTH = 640;
	private static final int CANVAS_HEIGHT = 480;

	public Simulation sim;

	private LinkedList<UIEvent> events;
	private ArrayList<UIWidget> widgets;

	private JFrame frame;
	private Canvas screen;
	private byte[][] widgetIDs;

	public UI() {
		this.sim = sim;
		events  = new LinkedList<UIEvent>();
		widgets = new ArrayList<UIWidget>();

		// initialize the screen canvas
		screen = new Canvas();
		Dimension size = new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT);
		screen.setMinimumSize(size);
		screen.setMaximumSize(size);
		screen.setPreferredSize(size);
		screen.addMouseListener(this);
		screen.addKeyListener(this);

		// initialize the frame
		frame = new JFrame("Parasite");
		frame.add(screen);
		frame.pack();
		frame.setVisible(true);

		// create buffer strategy (after showing frame)
		screen.createBufferStrategy(2);

		// initialize the widgets
		initWidgets();
	}

	public void setSimulation(Simulation sim) {
		this.sim = sim;
		for (UIWidget widget : widgets) {
			widget.setSimulation(sim);
		}
	}

	private void initWidgets() {
		widgetIDs = new byte[CANVAS_HEIGHT][CANVAS_WIDTH];

		addWidget(new GameWidget(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT, this));
	}

	private void addWidget(UIWidget widget) {
		widgets.add(widget);
		byte id = (byte)widgets.size(); // leave 0 for unassigned

		// fill widget rect with widget id
		int wx = widget.getX();
		int wy = widget.getY();
		int hMax = Math.min(widget.getHeight(), CANVAS_HEIGHT - wy);
		int wMax = Math.min(widget.getWidth(),  CANVAS_WIDTH  - wy);
		for (int i = 0; i < hMax; i++) {
			for (int j = 0; j < wMax; j++) {
				widgetIDs[i + wy][j + wx] = id;
			}
		}
	}

	public UIEvent getEvent() {
		return events.poll();
	}

	public void addEvent(UIEvent uie) {
		events.add(uie);
	}

	public void render() {
		// get draw graphics
		BufferStrategy bs = screen.getBufferStrategy();
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();

		// draw each widget
		for (UIWidget widget : widgets) {
			// translate graphics into widget space
			g.translate(widget.getX(), widget.getY());
			widget.render(g);
			g.translate(-widget.getX(), -widget.getY());
		}

		// clean up
		bs.show();
		g.dispose();
	}

	public void mouseClicked(MouseEvent e) {
		// get id of clicked widget
		byte id = widgetIDs[e.getY()][e.getX()];
		if (id == 0) return; // if no widget, return

		// translate event into clickedWidget's context
		UIWidget clickedWidget = widgets.get(id - 1);
		e.translatePoint(-clickedWidget.getX(), -clickedWidget.getY());

		// pass event to clickedWidget
		clickedWidget.mouseClicked(e);
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				addEvent(new UIEvent(UIEvent.MOVE_UP));
				break;
			case KeyEvent.VK_A:
				addEvent(new UIEvent(UIEvent.MOVE_LEFT));
				break;
			case KeyEvent.VK_S:
				addEvent(new UIEvent(UIEvent.MOVE_DOWN));
				break;
			case KeyEvent.VK_D:
				addEvent(new UIEvent(UIEvent.MOVE_RIGHT));
				break;
		}
	}

	public void keyReleased(KeyEvent e) {
		UIEvent uie;
		switch (e.getKeyChar()) {
			case 'w':
				addEvent(new UIEvent(UIEvent.STOP_UP));
				break;
			case 'a':
				addEvent(new UIEvent(UIEvent.STOP_LEFT));
				break;
			case 's':
				addEvent(new UIEvent(UIEvent.STOP_DOWN));
				break;
			case 'd':
				addEvent(new UIEvent(UIEvent.STOP_RIGHT));
				break;
		}
	}
}
