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
	public void mouseMoved(MouseEvent e) {
		Entity focusedEntity = sim.getFocusedEntity();
		double dx = e.getX() - width  / 2;
		double dy = e.getY() - height / 2;
		focusedEntity.heading = Math.atan2(dy, dx);
	}

	public void render(Graphics2D g) {
		if (sim != null);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		// center on the focused entity
		Entity focusedEntity = sim.getFocusedEntity();
		g.translate(width / 2, height / 2);
		g.translate(-focusedEntity.x, focusedEntity.y);

		for (Entity e : sim.entities) {
			g.translate(e.x, -e.y);
			e.render(g);
			g.translate(-e.x, e.y);
		}

		g.translate(focusedEntity.x, -focusedEntity.y);
		g.translate(-width / 2, - height / 2);
	}
}
