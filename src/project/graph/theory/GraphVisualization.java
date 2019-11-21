package project.graph.theory;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

public class GraphVisualization {
	
	// Visualize graph G
	public void visualize(GraphColoring g, int[][] clr)
	{
		String[] CSS_COLOR_NAMES = {"AliceBlue","AntiqueWhite","Aqua","Aquamarine","Azure","Beige","Bisque","Black","BlanchedAlmond","Blue","BlueViolet","Brown","BurlyWood","CadetBlue","Chartreuse","Chocolate","Coral","CornflowerBlue","Cornsilk","Crimson","Cyan","DarkBlue","DarkCyan","DarkGoldenRod","DarkGray","DarkGrey","DarkGreen","DarkKhaki","DarkMagenta","DarkOliveGreen","Darkorange","DarkOrchid","DarkRed","DarkSalmon","DarkSeaGreen","DarkSlateBlue","DarkSlateGray","DarkSlateGrey","DarkTurquoise","DarkViolet","DeepPink","DeepSkyBlue","DimGray","DimGrey","DodgerBlue","FireBrick","FloralWhite","ForestGreen","Fuchsia","Gainsboro","GhostWhite","Gold","GoldenRod","Gray","Grey","Green","GreenYellow","HoneyDew","HotPink","IndianRed","Indigo","Ivory","Khaki","Lavender","LavenderBlush","LawnGreen","LemonChiffon","LightBlue","LightCoral","LightCyan","LightGoldenRodYellow","LightGray","LightGrey","LightGreen","LightPink","LightSalmon","LightSeaGreen","LightSkyBlue","LightSlateGray","LightSlateGrey","LightSteelBlue","LightYellow","Lime","LimeGreen","Linen","Magenta","Maroon","MediumAquaMarine","MediumBlue","MediumOrchid","MediumPurple","MediumSeaGreen","MediumSlateBlue","MediumSpringGreen","MediumTurquoise","MediumVioletRed","MidnightBlue","MintCream","MistyRose","Moccasin","NavajoWhite","Navy","OldLace","Olive","OliveDrab","Orange","OrangeRed","Orchid","PaleGoldenRod","PaleGreen","PaleTurquoise","PaleVioletRed","PapayaWhip","PeachPuff","Peru","Pink","Plum","PowderBlue","Purple","Red","RosyBrown","RoyalBlue","SaddleBrown","Salmon","SandyBrown","SeaGreen","SeaShell","Sienna","Silver","SkyBlue","SlateBlue","SlateGray","SlateGrey","Snow","SpringGreen","SteelBlue","Tan","Teal","Thistle","Tomato","Turquoise","Violet","Wheat","White","WhiteSmoke","Yellow","YellowGreen"};

		Graph graph = new SingleGraph(g.name);
		
		for(int i = 1; i < g.adjacencyLists.length; i++)
		{
			char u = (char) (i+'A'-1);
			graph.addNode(u + "");
		} 
		
		for(int i = 1; i < g.adjacencyLists.length; i++)
		{
			AdjacencyList n = g.adjacencyLists[i];
			String u = "" + (char) (i+'A'-1);
			while(n != null)
			{
				String v = "" + (char) (n.vertex +'A' -1);
				String s = u + v;
				try {
					graph.addEdge(s, u, v);
				}
				catch(Exception e)
				{
					
				}
				n = n.next;
			}
		}
		
		
		for(Node n:graph) {
	        char id = n.getId().charAt(0);
			int u = id - 'A' + 1;
			for(int i = 1; i < clr.length; i++)
			{
				if(i != u && clr[u][i] != 0)
				{
					String v = "" + (char) (i +'A' - 1);
			        Edge e = n.getEdgeToward(v);
			        String color = CSS_COLOR_NAMES[clr[u][i]*10 % CSS_COLOR_NAMES.length];
			        color.toLowerCase();
			    		e.addAttribute("ui.style", "size: 5px; fill-color: "+color+";");
				}
			}
	    }
		graph.display();
	}
}
