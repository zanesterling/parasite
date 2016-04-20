package Parasite.util;

/**
 * 2d vector class.
 * All functions which change the vector return it,
 * so they can be chained.
 */
public class Vector2d {
	public double x, y;

	public Vector2d() { this(0, 0); }

	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2d(double[] array) {
		x = array[0];
		y = array[1];
	}

	public Vector2d(Vector2d vec) {
		this(vec.x, vec.y);
	}

	public double angle() {
		return Math.atan2(y, x);
	}

	public double dot(Vector2d that) {
		return x * that.x + y * that.y;
	}

	public double length() {
		return Math.sqrt(x * x + y * y);
	}

	public double lengthSquared() {
		return x * x + y * y;
	}

	public Vector2d normalize() {
		double length = length();
		x /= length;
		y /= length;
		return this;
	}

	public Vector2d add(Vector2d that) {
		x += that.x;
		y += that.y;
		return this;
	}

	public Vector2d sub(Vector2d that) {
		x -= that.x;
		y -= that.y;
		return this;
	}

	public Vector2d negate() {
		scale(-1, -1);
		return this;
	}

	public Vector2d scale(double s) { return scale(s, s); }
	public Vector2d scale(double x, double y) {
		this.x *= x;
		this.y *= y;
		return this;
	}

	public Vector2d set(Vector2d that) { return set(that.x, that.y); }
	public Vector2d set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Vector2d clone() {
		return new Vector2d(this);
	}
}
