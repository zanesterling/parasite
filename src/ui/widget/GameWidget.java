package Parasite.ui.widget;

import Parasite.ui.UI;
import Parasite.ui.UIEvent;
import Parasite.ui.EventCode;
import Parasite.sim.entity.Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class GameWidget extends UIWidget {

	public GameWidget(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {
		double dx = e.getX() - width  / 2;
		double dy = e.getY() - height / 2;
		double theta = Math.atan2(dy, dx);
		UIEvent uie = new UIEvent(EventCode.LOOK, new double[]{ theta });
		UI.getInstance().addEvent(uie);
	}

	public void render(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		if (sim == null) return;

		sim.render(g);
	}
}
