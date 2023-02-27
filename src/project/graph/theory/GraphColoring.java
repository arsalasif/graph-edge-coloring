package project.graph.theory;

import java.io.IOException;

public class GraphColoring {
	String name;
	//	AdjacencyList class stores all edges of vertices as adjacency lists in form of linked lists. Index
	//	0 is left unused and adjacency list is traversed from 1 to n. This is due to files containing
	//	vertices starting from 1 and not 0. Instead of converting vertices from 0 to n – 1, they were just
	//	stored as 1 to n and respective arrays initialized to n + 1 length.
	AdjacencyList[] adjacencyLists;
	// Array that maintains coloring of the graph
	int[][] clr;
	int[] color;
	// Array that stores degree of each vertex
	int[] degree;
	int delta;
	int minimumDegree;
	int colorArrLength;
	int edges;

	/**
	 * Class constructor that initializes data structures
	 * @param graphName name given to this graph
	 * @param n number of vertices
	 */
	public GraphColoring(String graphName, int n) {
		name = graphName;
		adjacencyLists = new AdjacencyList[n + 1];
		for (int i = 0; i < adjacencyLists.length; i++) {
			adjacencyLists[i] = new AdjacencyList();
		}
		clr = new int[n + 1][n + 1];
		degree = new int[n + 1];
	}

	/**
	 * Adds undirected edge u-v and v-u
	 * @param u the vertex u
	 * @param v the vertex v
	 */
	public void addEdges(int u, int v) {
		if (adjacencyLists[v] == null) {
			adjacencyLists[v].vertex = v;
		}
		if (adjacencyLists[u] == null) {
			adjacencyLists[u].vertex = u;
		}
		if (!adjacencyLists[u].hasEdge(v)) {
			clr[u][v] = 0;
			adjacencyLists[u].addEdge(v);
			degree[u]++;
			edges++;
		}

		if (!adjacencyLists[v].hasEdge(u)) {
			clr[v][u] = 0;
			adjacencyLists[v].addEdge(u);
			degree[v]++;
		}

	}

	/**
	 * Removes an edge from graph randomly
	 * Useful when finding critical path in a graph
	 * @param u the vertex u
	 * @param v the vertex v
	 */
	public void removeEdges(int u, int v) {
		clr[u][v] = 0;
		clr[v][u] = 0;
		adjacencyLists[u].removeEdge(v);
		adjacencyLists[v].removeEdge(u);
		degree[v]--;
		degree[u]--;
		edges--;

	}

	/**
	 * The core of this class, used to edge color the entire graph 
	 */
	public void edgeColor() {
		setDeltaAndColors();
		// For each vertex u
		// Count color frequencies at u
		for (int u = 1; u < adjacencyLists.length; u++) {
			AdjacencyList v = adjacencyLists[u];
			// Find first repeated color k at u
			int k = firstRepeatedColor(u);
			int[] rotation = new int[colorArrLength];
			// While there is still some repeated color k
			while (k > 0) {
				// Find first missing color j at u
				int j = firstMissingColor(u);
				int i = secondMissingColor(u, j);
				// Try to recolor graph through BFS using vertex u and colors k and j
				// If recoloring fails,
				// Find second missing color i at u
				// Try to recolor graph through BFS using vertex u and colors k and i
				if (!colorBFS(u, k, j) && !colorBFS(u, k, i)) {
					// If BFS fails both times
					// Do a color rotation
					v = adjacencyLists[u];
					while (v != null) {
						if (clr[u][v.vertex] == k)
							break;
						v = v.next;
					}
					if (v != null) {
						int l = firstMissingColorForRotation(v.vertex, rotation);
						clr[u][v.vertex] = l;
						clr[v.vertex][u] = l;
						rotation[l] = 1;
					}
				}
				k = firstRepeatedColor(u);
			}
			
			// For each edge u-v, color with the first available color
			v = adjacencyLists[u];
			while (v != null) {
				if (clr[u][v.vertex] == 0 && u != v.vertex) {
					int missingColor = firstMissingColor(u);
					clr[u][v.vertex] = missingColor;
					clr[v.vertex][u] = missingColor;
				}
				v = v.next;
			}
		}
		Util.clearArray(color);
		countColors();
	}

	/**
	 * Helper method to print out this graph
	 */
	public void printGraph() {
		for (int i = 1; i < adjacencyLists.length; i++) {
			AdjacencyList n = adjacencyLists[i];
			while (n != null) {
				System.out.print(n.vertex + " ");
				n = n.next;
			}
			System.out.println();
		}
	}

	/**
	 * Finds the max degree in the graph and sets it as the delta variable value
	 * Then initialize the color array based on that delta
	 */
	public void setDeltaAndColors() {
		delta = Util.findMax(degree);
		minimumDegree = Util.findMin(degree);
		colorArrLength = delta + 2;
		color = new int[colorArrLength];
	}

	/**
	 * Remove color that occurs min number of times
	 */
	public void removeMinOccuringColor() {
		int minOccColor = Util.findIndexOfMin(color);
		for (int i = 1; i < clr.length; i++) {
			for (int j = 1; j < clr[i].length; j++) {
				if (clr[i][j] == minOccColor)
					clr[i][j] = 0;
			}
		}

		Util.clearArray(color);
		countColors();
	}

	/**
	 * Remove color that occurs min number of times
	 * @return  true or false based on whether this graph has a proper coloring
	 */
	public boolean isProperColoring() {
		for (int i = 1; i < adjacencyLists.length; i++)
			if (anyRepetitions(i))
				return false;
		if (!allEdgesColored())
			return false;
		return true;
	}

	/**
	 * Color switching using BFS
	 * @param u the vertex u
	 * @param k the color k
	 * @param j the color j
	 * @return	true or false based on whether the recoloring succeeded
	 */
	private boolean colorBFS(int u, int k, int j) {
		// color k is repeated at vertex u, color j is missing at u
		// initialize scanQ with u and all adjacent vertices of color k
		int[] visited = new int[adjacencyLists.length];
		Queue scanQ = new Queue();
		visited[u] = 1;
		scanQ.enqueue(u, 0, null);
		Queue uQueue = scanQ.dequeue();
		AdjacencyList nodeV = adjacencyLists[u];
		while (nodeV != null) {
			if (clr[u][nodeV.vertex] == k) {
				visited[nodeV.vertex] = 1;
				scanQ.enqueue(nodeV.vertex, k, uQueue);
			}
			nodeV = nodeV.next;
		}

		// Do BFS search
		while (!scanQ.isEmpty()) {
			Queue v = scanQ.dequeue();
			AdjacencyList w = adjacencyLists[v.vertex];
			int[] colorAtV = new int[colorArrLength];
			while (w != null) {
				colorAtV[clr[v.vertex][w.vertex]]++;
				w = w.next;
			}
			if (v.color == k) {
				if (isMissingColor(colorAtV, j) || v.vertex > u) {
					// Backtrack and create a u-v path for color switching
					Queue fwd = v;
					Queue prev = fwd.prev;
					while (prev != null) {
						if (fwd.color == j) {
							clr[fwd.vertex][prev.vertex] = k;
							clr[prev.vertex][fwd.vertex] = k;
						} else if (fwd.color == k) {
							clr[fwd.vertex][prev.vertex] = j;
							clr[prev.vertex][fwd.vertex] = j;
						}
						fwd = prev;
						prev = fwd.prev;
					}
					return true;
				} else {
					AdjacencyList vNext = adjacencyLists[v.vertex];
					while (vNext != null) {
						if (visited[vNext.vertex] == 0 && clr[v.vertex][vNext.vertex] == j) {
							visited[vNext.vertex] = 1;
							scanQ.enqueue(vNext.vertex, j, v);
						}
						vNext = vNext.next;
					}
				}
			} else {
				if (isMissingColor(colorAtV, k) || v.vertex > u) {
					// Backtrack and create a u-v path for color switching
					Queue fwd = v;
					Queue prev = fwd.prev;
					while (prev != null) {
						if (fwd.color == j) {
							clr[fwd.vertex][prev.vertex] = k;
							clr[prev.vertex][fwd.vertex] = k;
						} else if (fwd.color == k) {
							clr[fwd.vertex][prev.vertex] = j;
							clr[prev.vertex][fwd.vertex] = j;
						}
						fwd = prev;
						prev = fwd.prev;
					}
					return true;
				} else {

					AdjacencyList vNext = adjacencyLists[v.vertex];
					while (vNext != null) {
						if (visited[vNext.vertex] == 0 && clr[v.vertex][vNext.vertex] == k) {
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

	/**
	 * Finds the first repeated color
	 * @param u the vertex
	 * @return  the first repeated color at vertex u
	 */
	private int firstRepeatedColor(int u) {

		AdjacencyList v = adjacencyLists[u];
		int[] colorAtU = new int[colorArrLength];
		while (v != null) {
			colorAtU[clr[u][v.vertex]]++;
			v = v.next;
		}

		for (int k = 1; k < colorAtU.length; k++)
			if (colorAtU[k] > 1)
				return k;
		return 0;
	}
	
	/**
	 * Finds the first missing color
	 * @param u the vertex
	 * @return  the first missing color at vertex u
	 */
	private int firstMissingColor(int u) {
		AdjacencyList v = adjacencyLists[u];
		int[] colorAtU = new int[colorArrLength];
		while (v != null) {
			colorAtU[clr[u][v.vertex]]++;
			v = v.next;
		}

		for (int j = 1; j < colorAtU.length; j++) {
			if (colorAtU[j] == 0) {
				return j;
			}

		}
		return 0;
	}
	
	/**
	 * Find first missing color for rotation
	 * @param u the vertex
	 * @return  the first missing color at vertex u for given rotation
	 */
	private int firstMissingColorForRotation(int u, int[] rotation) {
		AdjacencyList v = adjacencyLists[u];
		int[] colorAtU = new int[colorArrLength];
		while (v != null) {
			colorAtU[clr[u][v.vertex]]++;
			v = v.next;
		}

		for (int j = 1; j < colorAtU.length; j++) {
			if (colorAtU[j] == 0 && rotation[j] == 0) {
				return j;
			}

		}
		return 0;
	}

	/**
	 * Finds the second missing color
	 * @param u the vertex
	 * @param j the first missing color
	 * @return  the second missing color at vertex u
	 */
	private int secondMissingColor(int u, int j) {
		AdjacencyList v = adjacencyLists[u];
		int[] colorAtU = new int[colorArrLength];
		while (v != null) {
			colorAtU[clr[u][v.vertex]]++;
			v = v.next;
		}

		for (int i = 1; i < colorAtU.length; i++) {
			if (colorAtU[i] == 0 && i != j) {
				return i;
			}
		}
		return 0;
	}


	/**
	 * Check if the given color is a missing color
	 * @param col the color array
	 * @param j color to check against
	 * @return  true or false based on whether this is a missing color
	 */
	private boolean isMissingColor(int[] col, int j) {
		if (col[j] == 0)
			return true;
		return false;
	}

	// 

	/**
	 * Count colors used in graph and fill Colors array
	 */
	private void countColors() {
		for (int i = 1; i < clr.length; i++) {
			for (int j = 1; j < clr[i].length; j++) {
				if (i != j && clr[i][j] != 0)
					color[clr[i][j]]++;
			}
		}

		for (int i = 0; i < color.length; i++)
			color[i] = color[i] / 2;
	}

	/**
	 * Check if there is any color repetition at u
	 * @param u the vertex u
	 * @return  true or false based on whether there is any color repetition
	 */
	private boolean anyRepetitions(int u) {
		AdjacencyList v = adjacencyLists[u];
		int[] colorAtU = new int[colorArrLength];
		while (v != null) {
			colorAtU[clr[u][v.vertex]]++;
			v = v.next;
		}

		for (int j = 1; j < colorAtU.length; j++) {
			if (colorAtU[j] > 1) {
				return true;
			}

		}
		return false;
	}

	/**
	 * Check if all edges in graph G are colored
	 * @return  true or false based on whether all edges are colored
	 */
	private boolean allEdgesColored() {
		for (int u = 1; u < adjacencyLists.length; u++) {
			AdjacencyList v = adjacencyLists[u].next;
			while (v != null) {
				if (clr[u][v.vertex] == 0)
					return false;
				v = v.next;
			}
		}

		return true;
	}
}
