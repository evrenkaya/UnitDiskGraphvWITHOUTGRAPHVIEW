package ca.cglab.udgapp.graphmodel;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

// An algorithm like class that traverses a UnitDiskGraph and provides some information about its traversal
public class BreadthFirstSearch
{
	private final UnitDiskGraph udg;
	
	// Keeps track of all the connected components(sub-graphs) in 'udg'
	// Each connected component is of type ArrayList<Vertex>.
	private ArrayList<ArrayList<Vertex>> allConnectedComponents;
	
	public BreadthFirstSearch(UnitDiskGraph udg)
	{
		this.udg = udg;
	}
	
	// Returns an ArrayList of the entire connected sub-graph
	// component containing startingVertex, using breadth-first-search.
	public ArrayList<Vertex> getConnectedComponentWith(Vertex startingVertex)
	{
		ArrayList<Vertex> visitedVertices = new ArrayList<Vertex>();
		Queue<Vertex> q = new ArrayDeque<Vertex>();

		startingVertex.setVisited(true);
		q.add(startingVertex);
		visitedVertices.add(startingVertex);
		
		while(!q.isEmpty())
		{
			Vertex first = q.remove();
			ArrayList<Vertex> neighbours = first.getNeighbours();
			for(int i = 0; i < neighbours.size(); i++)
			{
				Vertex second = neighbours.get(i);
				if(!second.isVisited())
				{
					second.setVisited(true);
					q.add(second);
					visitedVertices.add(second);
				}
			}
		}
		for(int i = 0; i < visitedVertices.size(); i++)
		{
			visitedVertices.get(i).setVisited(false);
		}
		return visitedVertices;
	}
	
	// Determines all the connected components(sub-graphs) within udg
	public void determineAllConnectedComponents()
	{
		allConnectedComponents = new ArrayList<ArrayList<Vertex>>();
		ArrayList<Vertex> vertices = udg.getVertices();
		// This is used to keep track of which vertices already are in a connected component
		ArrayList<Vertex> visitedVertices = new ArrayList<Vertex>(vertices.size());
		
		// Start with the first component
		ArrayList<Vertex> first = getConnectedComponentWith(vertices.get(0));
		visitedVertices.addAll(first);
		allConnectedComponents.add(first);
		
		// Go through all vertices in the unit disk graph
		// and get the connected components containing each vertex
		for(int i = 1, l = vertices.size(); i < l; i++)
		{
			Vertex v = vertices.get(i);
			// Check if v is already part of a component
			if(!visitedVertices.contains(v))
			{
				// v has not been verified as part of a component
				// so get the connected component it belongs to
				// and then add that component to the main components list
				ArrayList<Vertex> current = getConnectedComponentWith(v);
				visitedVertices.addAll(current);
				allConnectedComponents.add(current);
			}
		}
	}
	
	// Determines the largest number of vertices in a connected component
	public int getNumVerticesinLargestComponent()
	{
		int numVerticesInLargestComponent = 0;
		for(ArrayList<Vertex> component : allConnectedComponents)
		{
			if(component.size() > numVerticesInLargestComponent)
			{
				numVerticesInLargestComponent = component.size();
			}
		}
		
		return numVerticesInLargestComponent;
	}

	
	// Determines the number of connected components with >= k vertices
	public int numConnectedComponentsWithAtLeastKVertices(int k)
	{
		int num = 0;
		for(ArrayList<Vertex> component : allConnectedComponents)
		{
			if(component.size() >= k)
			{
				num++;
			}
		}
		return num;
	}
	
	
	// Determines the number of connected components that have exactly k vertices
	public int numConnectedComponentsWithKVertices(int k)
	{
		int num = 0;
		for(ArrayList<Vertex> component : allConnectedComponents)
		{
			if(component.size() == k)
			{
				num++;
			}
		}
		return num;
	}
}
