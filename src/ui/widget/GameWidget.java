package Parasite.ui.widget;

import Parasite.ui.UI;
import Parasite.sim.entity.Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class GameWidget extends UIWidget {

	public GameWidget(int x, int y, int width, int height, UI parent) {
		super(x, y, width, height, parent);
	}

	public void mouseClicked(MouseEvent e) {}
	public void render(Graphics2D g) {
		if (sim != null);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		for (Entity e : sim.entities) {
			g.translate(e.x, -e.y);
			e.render(g);
			g.translate(-e.x, e.y);
		}
	}
}
