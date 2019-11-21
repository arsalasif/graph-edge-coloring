package project.graph.theory;


public class AdjacencyList {
	int vertex;
    AdjacencyList next; // Adjacency list
    
    // Adds edge in adjacency list of this vertex
    public void addEdge(int v)
    {
    		AdjacencyList n = new AdjacencyList();
    		n.vertex = v;
    		n.next = null;
    		if(next == null)
    		{
        		next = n;
    		}
    		else
    		{
        		AdjacencyList ptr = next;
        		while(ptr.next != null)
        		{
        			ptr = ptr.next;
        		}
        		ptr.next = n;
    		}
    }
    
    // Removes edge in adjacency list of this vertex
    public void removeEdge(int v)
    {
    		AdjacencyList ptr = this;
		while(ptr.next != null)
		{
			if(ptr.next.vertex == v)
			{
				// Node removed from linked list -- Java takes care of garbage collection
				ptr.next = ptr.next.next;
				break;
			}
			ptr = ptr.next;
		}
    }
    
    // Checks if vertex v exists in this list
    boolean hasEdge(int v)
    {
	    	AdjacencyList ptr = this;
		while(ptr != null)
		{
			if(ptr.vertex == v) return true; 
			ptr = ptr.next;
		}
		return false;
    }
}
