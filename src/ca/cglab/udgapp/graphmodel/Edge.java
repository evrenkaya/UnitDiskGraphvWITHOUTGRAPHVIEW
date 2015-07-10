package ca.cglab.udgapp.graphmodel;

public final class Edge
{
	private final Vertex first;
	private final Vertex second;
	
	private boolean intersecting;
	
	private double weight;
	
	public Edge(Vertex first, Vertex second, double weight)
	{
		this.first = first;
		this.second = second;
		this.weight = weight;
		intersecting = false;
	}
	
	// Getters/setters
	public void setIntersecting(boolean b) { intersecting = b; }
	public boolean isIntersecting() { return intersecting; }
	
	public Vertex getFirst() { return first; }
	public Vertex getSecond() { return second; }
	
	public double getWeight() { return weight; }
	
	public boolean equals(Object o)
	{
		if(!(o instanceof Edge))
		{
			return false;
		}
		Edge e = (Edge)o;
		return (this.first == e.getFirst() && this.second == e.getSecond())
			|| (this.first == e.getSecond() && this.second == e.getFirst());
	}
}
