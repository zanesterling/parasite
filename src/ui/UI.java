package Parasite.ui;

import Parasite.sim.entity.Entity;
import Parasite.sim.Simulation;
import Parasite.ui.widget.UIWidget;
import Parasite.ui.widget.GameWidget;

import java.io.File;
import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;

public class UI extends MouseAdapter implements KeyListener {

	private static UI instance;
	public static UI getInstance() {
		if (instance == null) instance = new UI();
		return instance;
	}

	public int canvasWidth;
	public int canvasHeight;

	public Simulation sim;

	private LinkedList<UIEvent> events;
	private ArrayList<UIWidget> widgets;

	private JFrame frame;
	private Canvas screen;
	private byte[][] widgetIDs;

	private UI() {
		this.sim = sim;
		events  = new LinkedList<UIEvent>();
		widgets = new ArrayList<UIWidget>();

		if (!loadCanvasDimensions()) return;

		// initialize the screen canvas
		initScreen();

		// initialize the widgets
		initWidgets();
	}

	public boolean loadCanvasDimensions() {
		Scanner sc;
		try {
			sc = new Scanner(new File("res/display.cfg"));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		canvasWidth  = sc.nextInt();
		canvasHeight = sc.nextInt();
		sc.nextLine();

		return true;
	}

	// initialize screen canvas, frame
	public void initScreen() {
		// initialize screen
		screen = new Canvas();
		Dimension size = new Dimension(canvasWidth, canvasHeight);
		screen.setMinimumSize(size);
		screen.setMaximumSize(size);
		screen.setPreferredSize(size);
		screen.addMouseListener(this);
		screen.addMouseMotionListener(this);
		screen.addKeyListener(this);

		// initialize the frame
		frame = new JFrame("Parasite");
		frame.add(screen);
		frame.pack();
		frame.setVisible(true);

		screen.setFocusable(true);
		screen.requestFocusInWindow();

		// create buffer strategy (after showing frame)
		screen.createBufferStrategy(2);
	}

	public void setSimulation(Simulation sim) {
		this.sim = sim;
		for (UIWidget widget : widgets) {
			widget.setSimulation(sim);
		}
	}

	// initialize the widgets
	private void initWidgets() {
		widgetIDs = new byte[canvasHeight][canvasWidth];

		addWidget(new GameWidget(0, 0, canvasWidth, canvasHeight, this));
	}

	// add a widget to the UI at a particular location
	private void addWidget(UIWidget widget) {
		widgets.add(widget);
		byte id = (byte)widgets.size(); // leave 0 for unassigned

		// fill widget rect with widget id
		int wx = widget.getX();
		int wy = widget.getY();
		int hMax = Math.min(widget.getHeight(), canvasHeight - wy);
		int wMax = Math.min(widget.getWidth(),  canvasWidth  - wy);
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

	// render the UI and all widgets on it
	public void render() {
		// get draw graphics
		BufferStrategy bs = screen.getBufferStrategy();
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();

		// draw each widget
		for (UIWidget widget : widgets) {
			// translate and clip graphics into widget space
			g.translate(widget.getX(), widget.getY());
			g.setClip(0, 0, widget.getWidth(), widget.getHeight());
			widget.render(g);
			g.translate(-widget.getX(), -widget.getY());
		}

		// clean up
		bs.show();
		g.dispose();
	}

	public void mouseClicked(MouseEvent e) {
		UIWidget clickedWidget = getWidget(e);
		e.translatePoint(-clickedWidget.getX(), -clickedWidget.getY());
		if (clickedWidget != null) clickedWidget.mouseClicked(e);
	}

	public void mouseMoved(MouseEvent e) {
		UIWidget clickedWidget = getWidget(e);
		e.translatePoint(-clickedWidget.getX(), -clickedWidget.getY());
		if (clickedWidget != null) clickedWidget.mouseMoved(e);
	}

	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	public UIWidget getWidget(MouseEvent e) {
		// get id of clicked widget
		byte id = widgetIDs[e.getY()][e.getX()];
		if (id == 0) return null; // if no widget, return

		return widgets.get(id - 1);
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				addEvent(new UIEvent(EventCode.MOVE_UP));
				break;
			case KeyEvent.VK_A:
				addEvent(new UIEvent(EventCode.MOVE_LEFT));
				break;
			case KeyEvent.VK_S:
				addEvent(new UIEvent(EventCode.MOVE_DOWN));
				break;
			case KeyEvent.VK_D:
				addEvent(new UIEvent(EventCode.MOVE_RIGHT));
				break;
			case KeyEvent.VK_E:
				addEvent(new UIEvent(EventCode.ACTION));
				break;
			case KeyEvent.VK_Q:
				addEvent(new UIEvent(EventCode.POP));
		}
	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				addEvent(new UIEvent(EventCode.STOP_UP));
				break;
			case KeyEvent.VK_A:
				addEvent(new UIEvent(EventCode.STOP_LEFT));
				break;
			case KeyEvent.VK_S:
				addEvent(new UIEvent(EventCode.STOP_DOWN));
				break;
			case KeyEvent.VK_D:
				addEvent(new UIEvent(EventCode.STOP_RIGHT));
				break;
		}
	}

	public void keyTyped(KeyEvent e) {}
}
