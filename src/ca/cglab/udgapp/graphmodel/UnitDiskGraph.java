package ca.cglab.udgapp.graphmodel;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

public final class UnitDiskGraph
{
	private final ArrayList<Vertex> vertices;
	private final ArrayList<Edge> edges;
	
	// Random number generator for vertex positions
	private final Random random;
	
	private int numVertices;
	private int numEdges;
	
	// Current distance threshold
	private double maxDistance;
	
	private int numIntersectingEdges;
	
	public UnitDiskGraph(int numVertices)
	{
		this.numVertices = numVertices;
		
		vertices = new ArrayList<Vertex>(numVertices);
		edges = new ArrayList<Edge>(numVertices);
		
		random = new Random();
	}
	
	// Adds 'numVertices' vertices to the vertex list in random locations.
	// The x,y coordinates are bounded between 0 and 1.
	public void createNewRandomVertices()
	{
		for(int i = 0; i < numVertices; i++)
		{
			Vertex v = new Vertex(random.nextDouble(), random.nextDouble());
			vertices.add(v);
		}
	}
	
	// Compares the distance between each vertex.
	// Adds an edge between two vertices if the Euclidean distance
	// between them is at most the distance threshold.
	public void createNewConnectedEdges()
	{
		for(int i = 0; i < numVertices; i++)
		{
			Vertex first = vertices.get(i);
			// Start j at i + 1 so that vertices are not compared with themselves
			for(int j = i + 1; j < numVertices; j++)
			{
				Vertex second = vertices.get(j);
				if(distanceBetweenVertices(first, second) <= maxDistance)
				{
					// Make both vertices see each other as neighbors
					first.addNeighbour(second);
					second.addNeighbour(first);
					edges.add(new Edge(first, second));
					numEdges++;
				}
			}
		}
		System.out.println("Edges: " + numEdges);
	}
	// Removed all vertices and edges in the graph
	public void removeAllVertices()
	{
		vertices.clear();
		removeAllEdges();
	}
	
	// Only removes the edges in the graph
	public void removeAllEdges()
	{
		for(int i = 0, l = vertices.size(); i < l; i++)
		{
			vertices.get(i).getNeighbours().clear();
		}
		edges.clear();
		numEdges = 0;
		numIntersectingEdges = 0;
	}
	
	// Helper method
	public static double distanceBetweenVertices(Vertex first, Vertex second)
	{
		double differenceX = second.getX() - first.getX();
		double differenceY = second.getY() - first.getY();
		
		return Math.sqrt((differenceX * differenceX) + (differenceY * differenceY));
	}
	
	public void determineIntersectingEdges()
	{
		numIntersectingEdges = 0;

		for(int i = 0, l = edges.size(); i < l; i++)
		{
			Edge first = edges.get(i);
			for(int j = i + 1; j < l; j++)
			{
				Edge second = edges.get(j);
				
				if(first.getFirst() == second.getFirst() || first.getFirst() == second.getSecond()
				|| first.getSecond() == second.getFirst() || first.getSecond() == second.getSecond())
				{
					// Don't count this as an intersection
					continue;
				}
				
				if(Line2D.linesIntersect(first.getFirst().getX(), first.getFirst().getY(),
										 first.getSecond().getX(), first.getSecond().getY(),
										 second.getFirst().getX(), second.getFirst().getY(),
										 second.getSecond().getX(), second.getSecond().getY()))
				{
					first.setIntersecting(true);
					second.setIntersecting(true);
				}
			}
		}

		// Now go through the list of edges and count how many are intersecting
		for(int i = 0, l = edges.size(); i < l; i++)
		{
			if(edges.get(i).isIntersecting())
			{
				numIntersectingEdges++;
				edges.get(i).setIntersecting(false);
			}
		}
	}
	
	// Getters/setters
	public void setMaxDistance(double value) { maxDistance = value; }
	public void setNumVertices(int n) { numVertices = n; }
	
	public ArrayList<Vertex> getVertices() { return vertices; }
	public int getNumVertices() { return numVertices; }
	public int getNumEdges() { return numEdges; }
	public int getNumFreeEdges() { return getNumEdges() - numIntersectingEdges; }
	public int getNumIntersectingEdges() { return numIntersectingEdges; }
}
