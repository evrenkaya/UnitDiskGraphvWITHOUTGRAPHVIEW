package ca.cglab.udgapp.graphmodel;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public final class Vertex
{
	// Position components
	private double x;
	private double y;
	
	private boolean visited;
	
	// Each vertex maintains a list of its neighbors as undirected edges
	private final ArrayList<Vertex> neighbours;
	
	public Vertex(double x, double y)
	{
		this.x = x;
		this.y = y;
		
		neighbours = new ArrayList<Vertex>();
		visited = false;
	}
	
	// Getters/setters
	public void addNeighbour(Vertex v) { neighbours.add(v); }
	public void removeNeighbour(Vertex v) { neighbours.remove(v); }
	
	public ArrayList<Vertex> getNeighbours() { return neighbours; }
	
	public boolean isVisited() { return visited; }
	public void setVisited(boolean b) { visited = b; }
	
	public double getX() { return x; }
	public double getY() { return y; }
	public Point2D getPosition()
	{
		return new Point2D.Double(x, y);
	}
	
	public String toString() { return "(" + x + ", " + y + ")"; }
}
