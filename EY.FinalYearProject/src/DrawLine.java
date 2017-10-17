import java.awt.Color;
import java.awt.Point;
import java.awt.Stroke;

// Class which holds all the properties to draw a line
public class DrawLine {
	
	Point start; // start point
	Point end; // end point
	Color colour;
	int thickness;
	int personFrom; // the number person it's going from e.g man 1
	int personTo; // number of person it's going to
	boolean addLine; // whether the line should be drawn or not
	
	// constructor of the line
	public DrawLine(Point a, Point b, Color c, int t, int p, int p2, boolean add){
		start = a;
		end = b;
		colour = c;
		thickness = t;
		personFrom = p;
		personTo = p2;
		addLine = add;
	}
}
