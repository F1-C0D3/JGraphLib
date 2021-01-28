package de.jgraphlib.maths;

import java.text.DecimalFormat;

public class VectorLine2D {

    // f(t) = A + t*N

    // N - "normalized vector pointing the direction"
    private final Vector2D N;

    // A - "point, on which the direction vector stands"
    private Point2D A;

    public VectorLine2D(double x, double y, double m) {

	if (Double.isInfinite(m)) {
	    this.N = new Vector2D(0, 1);
	} else {
	    // magnitude = (1^2 + m^2)^(1/2)
	    double magnitude = Math.sqrt(1 + Math.pow(m, 2));
	    // N = <1, m> / magnitude = <1 / magnitude, m / magnitude>
	    this.N = new Vector2D(1 / magnitude, m / magnitude);
	}

	// A
	this.A = new Point2D(x, y);
    }

    public Point2D getPointInDistance(double distance) {
	return new Point2D(A.x() + (distance * N.x()), A.y() + (distance * N.y()));
    }

    @Override
    public String toString() {
	DecimalFormat decimalFormat = new DecimalFormat("#.00");
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append("A = (").append(decimalFormat.format(A.x())).append(", ")
		.append(decimalFormat.format(A.y())).append("), ");
	stringBuilder.append("N = <").append(decimalFormat.format(N.x())).append(", ")
		.append(decimalFormat.format(N.y())).append(">");
	return stringBuilder.toString();
    }
}
