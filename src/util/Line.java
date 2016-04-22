package Parasite.util;

public class Line {
	public Vector2d p1;
	public Vector2d dim;

	public Line(double x1, double y1, double w, double h) {
		this(new Vector2d(x1, y1), new Vector2d(w, h));
	}

	public Line(Vector2d p1, Vector2d dim) {
		this.p1 = p1;
		this.dim = dim;
	}

	// TODO: exclude endpoints
	public double intersectDist(Line that) {
		Vector2d qMinusP = that.p1.clone().sub(p1);
		double top = qMinusP.x * that.dim.y - that.dim.x * qMinusP.y;
		double bottom = dim.x * that.dim.y - dim.y * that.dim.x;
		double t = top / bottom;
		if (t < 0 || 1 < t) {
			return Double.POSITIVE_INFINITY;
		}

		return dim.length() * t;
	}

	public double length() {
		return dim.length();
	}
}
