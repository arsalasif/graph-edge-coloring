package project.graph.theory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		
		GraphColoring graph;
		try
		{
			String fileName = "simple.txt";
			String path = Paths.get("").toAbsolutePath().toString()+"/test_graphs/" + fileName;
			Scanner fin;
			fin = new Scanner(new File(path));
            String name = fin.nextLine();
            int n = Integer.parseInt(fin.nextLine());
            graph = new GraphColoring(name, n);
            
            // Read file and fill in graph linked list
			while(fin.hasNext())
            {
				String adjacencyList = fin.nextLine();
				if(adjacencyList.equals("0"))
				{
					break;
				}
				String[] split = adjacencyList.substring(1).split(" ");
				int v = Integer.parseInt(split[0]);
				for (int i = 1; i < split.length; i++) {
					int u = Integer.parseInt(split[i]);
					graph.addEdges(u, v);
			    }
            }
            fin.close();
            System.out.println("File: " + fileName);
            graph.setDeltaAndColors();
            System.out.println();
            System.out.println("Delta (Max Degree): "+graph.delta);
            System.out.println("Edges: " + graph.edges);
            System.out.println("Vertices: " + (graph.adjacencyLists.length-1));
            boolean numOfColorsChange = false;
            for(int i = 0; i < 10; i++)
            		graph.edgeColor();
            
            int prevNumOfColors = Util.nonEmptyIndices(graph.color); 

            // Print whether delta or delta colors were used
            int colorsUsed = Util.nonEmptyIndices(graph.color);
            System.out.print("Colors Used: " + colorsUsed);
            if(colorsUsed > graph.delta)
            {
            		System.out.println(" (Delta+1)");
            }
            else System.out.println(" (Delta)");

            System.out.println("Is proper coloring? " + graph.isProperColoring());
            
            // Run graph through a loop ten times to color
            for(int i = 0; i < 10; i++)
            {
            		// Remove color that occurs min number of times
                graph.removeMinOccuringColor();
                graph.edgeColor();
                int numOfColors = Util.nonEmptyIndices(graph.color); 
                if(numOfColors != prevNumOfColors)
                {
            			numOfColorsChange = true;
            			prevNumOfColors = numOfColors;
                }
            	
            }
            
            // Output the number of colors used and color array

            Util.printArray("Colors: ", graph.color);
            colorsUsed = Util.nonEmptyIndices(graph.color);
            System.out.print("Colors Used: " + colorsUsed);
            if(colorsUsed > graph.delta)
            {
            		System.out.println(" (Delta+1)");
            }
            else System.out.println(" (Delta)");
            System.out.println("Is proper coloring after loop? " + graph.isProperColoring());
            int majorVertices = 0;
            for(int i = 1; i < graph.adjacencyLists.length; i++)
            {
            		if(graph.degree[i] == graph.delta)
            		{
            			majorVertices++;
            		}
            }
            
            // Test and print Class 2 graphs
            
            System.out.println("Minimum Degree: " + graph.minimumDegree);
            System.out.println("Number of major vertices: " + majorVertices);
            System.out.println("e > [v/2] Δ(G) ?: " + (graph.edges > (((n*(n-1))/2) * graph.delta)));
            System.out.println("Atleast three major vertices?: " + (majorVertices >= 3));
            System.out.println("Atleast delta G - min degree G + 2 major vertices?: " + (majorVertices >= (graph.delta - graph.minimumDegree + 2)));
            System.out.println("Atleast 2v/Δ major vertices?: " + (majorVertices >= ((2 * n)/graph.delta)));

           /*
            * Visualization of graph
            * Uncomment this block of code to find edge-critical edges
            */
//            GraphVisualization graphStream = new GraphVisualization();
//            graphStream.visualize(graph, graph.clr);

            numOfColorsChange = false;
            
            /*
             * 
             * Uncomment this block of code to find edge-critical edges
             * 
             */
//            int edgeCriticalEdges = 0;
//            // Remove each edge, re-color, and test
//            for(int i = 1; i < graph.adjacencyLists.length; i++)
//            {
//            		AdjacencyList adj = graph.adjacencyLists[i].next;
//            		int initialV = adj.vertex;
//            		do {
//            			if(adj.vertex > i)
//            			{
//	                		graph.removeEdges(i, adj.vertex);
//	                		graph.clr = new int[n+1][n+1];
//	                		graph.edgeColor();
//	                		int numOfColors = Util.nonEmptyIndices(graph.color);
//	                		// if delta colors were used it is an edge critical edge
//	                		if(numOfColors == graph.delta)
//	                		{
//		                    		numOfColorsChange = true;
//			            			edgeCriticalEdges++;
//			            	}
//	                		graph.addEdges(i, adj.vertex);
//            			}
//            			adj = adj.next;
//            		} while(adj != null && adj.vertex != initialV);
//            }
//
//            System.out.println("Number of edge critical edges: " + edgeCriticalEdges);
//    			graph.edgeColor();

            System.out.println("Colors change by Edge removal: " + numOfColorsChange);
            
            
		}
		catch (FileNotFoundException ex) {
            System.out.println("File not found.\nExiting program.");
            System.exit(0);
        }
		
	}

}
