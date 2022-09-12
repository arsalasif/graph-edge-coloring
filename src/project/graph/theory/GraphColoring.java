package project.graph.theory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GraphColoring {
	String name;
	AdjacencyList[] adjacencyLists;
	int[][] clr;
	int[] color;
	int[] degree;
	int delta;
	int minimumDegree;
	int colorArrLength;
	int edges;
	int more_colors;
	
	// Initializes graph
	public GraphColoring(String graphName, int n, int c)
	{
		name = graphName;
		adjacencyLists = new AdjacencyList[n+1];
		for(int i = 0; i < adjacencyLists.length; i++) {
			adjacencyLists[i] = new AdjacencyList();
		}
		clr = new int[n+1][n+1];
		degree = new int[n+1];
		more_colors = c;
	}

	// Adds edge u-v and v-u
	public void addEdges(int u, int v)
	{
		if(adjacencyLists[v] == null)
		{
			adjacencyLists[v].vertex = v;
		}
		if(adjacencyLists[u] == null)
		{
			adjacencyLists[u].vertex = u;
		}
		if(!adjacencyLists[u].hasEdge(v))
		{
			clr[u][v] = 0;
			adjacencyLists[u].addEdge(v);
			degree[u]++;
			edges++;
		}

		if(!adjacencyLists[v].hasEdge(u))
		{
			clr[v][u] = 0;
			adjacencyLists[v].addEdge(u);
			degree[v]++;
		}
		
	}
	
	// Removes an edge from graph randomly
	public void removeEdges(int u, int v)
	{
		clr[u][v] = 0;
		clr[v][u] = 0;
		adjacencyLists[u].removeEdge(v);
		adjacencyLists[v].removeEdge(u);
		degree[v]--;
		degree[u]--;
		edges--;
		
	}
	
	// Color switching using BFS
	public boolean colorBFS(int u, int k, int j)
	{
		// color k is repeated at vertex u, color j is missing at u
		// initialize scanQ with u and all adjacent vertices of color k
		int[] visited = new int[adjacencyLists.length];
		Queue scanQ = new Queue();
		visited[u] = 1;
		scanQ.enqueue(u, 0, null);
		Queue uQueue = scanQ.dequeue();
		AdjacencyList nodeV = adjacencyLists[u];
		while(nodeV != null)
		{
			if(clr[u][nodeV.vertex] == k)
			{
				visited[nodeV.vertex] = 1;
				scanQ.enqueue(nodeV.vertex, k, uQueue);
			}
			nodeV = nodeV.next;
		}
		
		// Do BFS search
		while(!scanQ.isEmpty())
		{
			Queue v = scanQ.dequeue();
			AdjacencyList w = adjacencyLists[v.vertex];
			int[] colorAtV = new int[colorArrLength];
			while(w != null)
			{
				colorAtV[clr[v.vertex][w.vertex]]++;
				w = w.next;
			}
			if(v.color == k)
			{
				if(isMissingColor(colorAtV, j) || v.vertex > u)
				{
					// Backtrack and create a u-v path for color switching
					Queue fwd = v;
					Queue prev = fwd.prev;
					while(prev != null)
					{
						if(fwd.color == j)
						{
							clr[fwd.vertex][prev.vertex] = k;
							clr[prev.vertex][fwd.vertex]= k;
						}
						else if(fwd.color == k)
						{
							clr[fwd.vertex][prev.vertex] = j;
							clr[prev.vertex][fwd.vertex] = j;
						}
						fwd = prev;
						prev = fwd.prev; 
					}
					return true;
				}
				else
				{
					AdjacencyList vNext = adjacencyLists[v.vertex];
					while(vNext != null)
					{
						if(visited[vNext.vertex] == 0 && clr[v.vertex][vNext.vertex] == j)
						{
							visited[vNext.vertex] = 1;
							scanQ.enqueue(vNext.vertex, j, v);
						}
						vNext = vNext.next;
					}
				}
			}
			else
			{
				if(isMissingColor(colorAtV, k) || v.vertex > u)
				{
					// Backtrack and create a u-v path for color switching
					Queue fwd = v;
					Queue prev = fwd.prev;
					while(prev != null)
					{
						if(fwd.color == j)
						{
							clr[fwd.vertex][prev.vertex] = k;
							clr[prev.vertex][fwd.vertex] = k;
						}
						else if(fwd.color == k)
						{
							clr[fwd.vertex][prev.vertex] = j;
							clr[prev.vertex][fwd.vertex] = j;
						}
						fwd = prev;
						prev = fwd.prev; 
					}
					return true;
				}
				else
				{

					AdjacencyList vNext = adjacencyLists[v.vertex];
					while(vNext != null)
					{
						if(visited[vNext.vertex] == 0 && clr[v.vertex][vNext.vertex] == k)
						{
							visited[vNext.vertex] = 1;
							scanQ.enqueue(vNext.vertex, k, v);
						}
						vNext = vNext.next;
					}
				}
			}
		}
		return false;
	}
	
	// Edge color graph
	public void edgeColor() throws IOException
	{
		Integer[] vertices = new Integer[adjacencyLists.length-1];
		for(int i=0; i < vertices.length; i++) {
			vertices[i] = i+1;
		}

		List<Integer> intList = Arrays.asList(vertices);

		Collections.shuffle(intList);

		intList.toArray(vertices);

		System.out.println("Shuffled vertices:" + Arrays.toString(vertices));
		
		setDeltaAndColors();
		for (int counter = 0; counter < vertices.length; counter++) {
			int u = vertices[counter];
			AdjacencyList v = adjacencyLists[u];
			int k = firstRepeatedColor(u);
			int[] rotation = new int[colorArrLength];
			while(k > 0)
			{
				int j = firstMissingColor(u);
				int i = secondMissingColor(u, j);
				if(!colorBFS(u, k, j) && !colorBFS(u, k, i))
				{
						v = adjacencyLists[u];
						while(v != null)
						{
							if(clr[u][v.vertex] == k)
								break;
							v = v.next;
						}
						if(v != null)
						{
							int l = firstMissingColorForRotation(v.vertex, rotation);
							clr[u][v.vertex] = l;
							clr[v.vertex][u] = l;
							rotation[l] = 1;
						}
				}
				k = firstRepeatedColor(u);
			}

			v = adjacencyLists[u];
			while(v != null)
			{
				if(clr[u][v.vertex] == 0 && u != v.vertex)
				{
					int missingColor = firstMissingColor(u);//, v.vertex);
					clr[u][v.vertex] = missingColor;
					clr[v.vertex][u] = missingColor;
				}
				v = v.next;
			}
		}
		Util.clearArray(color);
		countColors();
	}
	
	// Print this graph
	public void printGraph()
	{
		for (int i = 1; i < adjacencyLists.length; i++) {
				AdjacencyList n = adjacencyLists[i];
				while(n != null)
				{
					System.out.print(n.vertex + " ");
					n = n.next;
				}
				System.out.println();
    		}
	}
	
	// Set delta and initialize colors array
	public void setDeltaAndColors()
	{
		
		delta = Util.findMax(degree);
		minimumDegree = Util.findMin(degree);
		colorArrLength = delta+2;
		colorArrLength += more_colors;
		color = new int[colorArrLength];
	}

	// Find first repeated color
	public int firstRepeatedColor(int u)
	{

		AdjacencyList v = adjacencyLists[u];
		int[] colorAtU = new int[colorArrLength];
		while(v != null)
		{
			colorAtU[clr[u][v.vertex]]++;
			v = v.next;
		}
		
		
		for(int k = 1; k < colorAtU.length; k++)
			if(colorAtU[k] > 1) 
				return k;
		return 0;
	}

	// Find first missing color
	public int firstMissingColor(int u)
	{
		AdjacencyList v = adjacencyLists[u];
		int[] colorAtU = new int[colorArrLength];
		while(v != null)
		{
			colorAtU[clr[u][v.vertex]]++;
			v = v.next;
		}
		
		for(int j = 1; j < colorAtU.length; j++)
		{
			if(colorAtU[j] == 0) 
			{
				return j;
			}
			
		}
		return 0;
	}

	// find first missing color at v
	public int firstMissingColor(int u, int vVertex)
	{
		AdjacencyList v = adjacencyLists[u];
		int[] colorAtU = new int[colorArrLength];
		while(v != null)
		{
			colorAtU[clr[u][v.vertex]]++;
			v = v.next;
		}

		AdjacencyList w = adjacencyLists[vVertex];
		int[] colorAtV = new int[colorArrLength];
		while(w != null)
		{
			colorAtV[clr[vVertex][w.vertex]]++;
			w = w.next;
		}
		
		for(int j = 1; j < colorAtU.length; j++)
		{
			if(colorAtU[j] == 0) 
			{	
					if(colorAtV[j] == 0)
						return j;
			}
			
		}
		return 0;
	}
	
	
	// Find first missing color for rotation
	public int firstMissingColorForRotation(int u, int[] rotation)
	{
		AdjacencyList v = adjacencyLists[u];
		int[] colorAtU = new int[colorArrLength];
		while(v != null)
		{
			colorAtU[clr[u][v.vertex]]++;
			v = v.next;
		}
		
		for(int j = 1; j < colorAtU.length; j++)
		{
			if(colorAtU[j] == 0 && rotation[j] == 0) 
			{
				return j;
			}
			
		}
		return 0;
	}
	
	// Find second missing color
	public int secondMissingColor(int u, int j)
	{
		AdjacencyList v = adjacencyLists[u];
		int[] colorAtU = new int[colorArrLength];
		while(v != null)
		{
			colorAtU[clr[u][v.vertex]]++;
			v = v.next;
		}
		
		for(int i = 1; i < colorAtU.length; i++)
		{
			if(colorAtU[i] == 0 && i != j) 
			{
				return i;
			}
		}
		return 0;
	}
	

	// check if its a missing color
	public boolean isMissingColor(int[] col, int j)
	{
		if(col[j] == 0)
			return true;
		return false;
	}
	
	// count colors used in graph and fill Colors array
	public void countColors()
	{
		for(int i = 1; i < clr.length; i++)
		{
			for(int j = 1; j < clr[i].length; j++)
			{
				if(i != j && clr[i][j] != 0)
					color[clr[i][j]]++;
			}
		}
		
		for(int i = 0; i < color.length; i++)
			color[i] = color[i]/2;
	}
	

	// check if there is any color repetition at u
	public boolean anyRepetitions(int u)
	{
		AdjacencyList v = adjacencyLists[u];
		int[] colorAtU = new int[colorArrLength];
		while(v != null)
		{

			System.out.print(clr[u][v.vertex] + "-(" + u +"," + v.vertex+  "), ");
			colorAtU[clr[u][v.vertex]]++;
			v = v.next;
		}
		System.out.println();
		
		for(int j = 1; j < colorAtU.length; j++)
		{
			System.out.print(colorAtU[j] + ", ");
			if(colorAtU[j] > 1) 
			{
				System.out.println();
				return true;
			}
			
		}
		System.out.println();
		return false;
	}
	
	// check if all edges in graph G are colored
	public boolean allEdgesColored()
	{
		for(int u = 1; u < adjacencyLists.length; u++)
		{
			AdjacencyList v = adjacencyLists[u].next;
			while(v != null)
			{
				if(clr[u][v.vertex] == 0)
					return false;
				v = v.next;
			}
		}
		
		return true;
	}
	
	// remove color that occurs min number of times
	public void removeMinOccuringColor()
	{
		int minOccColor = Util.findIndexOfMin(color);
		for(int i = 1; i < clr.length; i++)
		{
			for(int j = 1; j < clr[i].length; j++)
			{
				if(clr[i][j] == minOccColor)
					clr[i][j] = 0;
			}
		}

		Util.clearArray(color);
		countColors();
	}
	
	// check if its a proper coloring
	public boolean isProperColoring()
	{
		for(int i = 1; i < adjacencyLists.length; i++)
    			if(anyRepetitions(i))
    			{
    				System.out.println("HAS REPETITIONS");
    				return false;
    			}
		if(!allEdgesColored())
			return false;
		return true;
	}
}
