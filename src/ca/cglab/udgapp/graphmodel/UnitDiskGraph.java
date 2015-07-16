package ca.cglab.udgapp.graphmodel;

import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Random;

public final class UnitDiskGraph
{
	private final ArrayList<Vertex> vertices;
	private final ArrayList<Edge> edges;
	private final ArrayList<Edge> freeEdges;
	private final ArrayList<Path2D> allPaths;
	
	// Random number generator for vertex positions
	private final Random random;
	
	private int numVertices;
	private int numEdges;
	private int numSuperFreeEdges;
	
	// Current distance threshold
	private double maxDistance;
	
	private int numIntersectingEdges;
	
	public UnitDiskGraph(int numVertices)
	{
		this.numVertices = numVertices;
		
		vertices = new ArrayList<Vertex>(numVertices);
		edges = new ArrayList<Edge>(numVertices);
		freeEdges = new ArrayList<Edge>(numVertices);
		allPaths = new ArrayList<Path2D>();
		
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
			//System.out.println(v);
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
				double distance = distanceBetweenVertices(first, second);
				if(distance <= maxDistance)
				{
					// Make both vertices see each other as neighbors
					first.addNeighbour(second);
					second.addNeighbour(first);
					//System.out.println("Edge: ");
					//System.out.println(first.toString() + " " + second.toString());
					edges.add(new Edge(first, second, distance));
					numEdges++;
				}
			}
		}
		//System.out.println("Edges: " + numEdges);

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
		freeEdges.clear();
		numEdges = numIntersectingEdges = numSuperFreeEdges = 0;
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
			else
			{
				freeEdges.add(edges.get(i));
			}
		}
		
		determineSuperFreeEdges();
	}
	
	public void determineSuperFreeEdges()
	{
		Path2D path = new Path2D.Double();
		numSuperFreeEdges = 0;
		for(int i = 0; i < freeEdges.size(); i++)
		{
			Edge freeEdge = freeEdges.get(i);
			boolean isSuperFree = false;
			Vertex v1 = freeEdge.getFirst();
			Vertex v2 = freeEdge.getSecond();
			// Angle relative to the horizontal
			double angle1 = Math.acos(Math.abs(v1.getX() - v2.getX()) / freeEdge.getWeight());
			double angle2 = Math.PI - (Math.PI / 2) - angle1;
			double angle3 = (Math.PI / 2) - angle2;
			double xDiff = maxDistance * Math.sin(angle3);
			double yDiff = maxDistance * Math.cos(angle3);
			double x1 = v1.getX();
			double y1 = v1.getY();
			double x2 = v2.getX();
			double y2 = v2.getY();
			
			if(y1 <= y2)
			{
				if(x1 <= x2)
				{
					path.moveTo(x1 - xDiff, y1 + yDiff);
					path.lineTo(x2 - xDiff, y2 + yDiff);
					path.lineTo(x2 + xDiff, y2 - yDiff);
					path.lineTo(x1 + xDiff, y1 - yDiff);
				}
				else if(x2 <= x1)
				{
					path.moveTo(x2 + xDiff, y2 + yDiff);
					path.lineTo(x1 + xDiff, y1 + yDiff);
					path.lineTo(x1 - xDiff, y1 - yDiff);
					path.lineTo(x2 - xDiff, y2 - yDiff);
				}
			}
			else if(y2 <= y1)
			{
				if(x1 <= x2)
				{
					path.moveTo(x1 + xDiff, y1 + yDiff);
					path.lineTo(x2 + xDiff, y2 + yDiff);
					path.lineTo(x2 - xDiff, y2 - yDiff);
					path.lineTo(x1 - xDiff, y1 - yDiff);
				}
				else if(x2 <= x1)
				{
					path.moveTo(x2 - xDiff, y2 + yDiff);
					path.lineTo(x1 - xDiff, y1 + yDiff);
					path.lineTo(x1 + xDiff, y1 - yDiff);
					path.lineTo(x2 + xDiff, y2 - yDiff);
				}
			}
			
			allPaths.add(path);
			for(int j = 0; j < vertices.size(); j++)
			{
				Vertex other = vertices.get(j);
				if(v1 != other && v2 != other)
				{
					if(path.contains(other.getX(), other.getY()))
					{
						isSuperFree = false;
						break;
					}
				}
				isSuperFree = true;
			}
			path = new Path2D.Double();
			if(isSuperFree)
			{
				numSuperFreeEdges++;
			}
		}
		
	}
	
	// Getters/setters
	public void setMaxDistance(double value) { maxDistance = value; }
	public void setNumVertices(int n) { numVertices = n; }
	
	public ArrayList<Vertex> getVertices() { return vertices; }
	public ArrayList<Edge> getEdges() { return edges; }
	public ArrayList<Path2D> getAllPaths() { return allPaths; }
	public double getMaxDistance() { return maxDistance; }
	public int getNumVertices() { return numVertices; }
	public int getNumEdges() { return numEdges; }
	public int getNumFreeEdges() { return getNumEdges() - numIntersectingEdges; }
	public int getNumIntersectingEdges() { return numIntersectingEdges; }
	public int getNumSuperFreeEdges() { return numSuperFreeEdges; }
}
