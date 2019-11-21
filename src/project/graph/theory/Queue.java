package project.graph.theory;

public class Queue {
	int vertex;
    Queue next;
    int color;
    Queue prev;
    
    // Pushes vertex on Queue
    public void enqueue(int v, int c, Queue p)
    {
    		// if is empty, assign value to front and return func
    		if(isEmpty())
    		{
    			vertex = v;
    			color = c;
    			prev = p;
    			return;
    		}
    		
    		// else create a new node and add it to last of list
    		Queue last = new Queue();
    		last.vertex = v;
    		last.next = null;
    		last.color = c;
    		last.prev = p;
    		
    		Queue ptr = this;
    		while(ptr.next != null)
    		{
    			ptr = ptr.next;
    		}
    		ptr.next = last;
    }
    
    // Dequeues vertex from Queue
    public Queue dequeue()
    {
    		Queue q = new Queue();
    		q.vertex = vertex;
    		q.color = color;
    		q.prev = prev;
    		
    		// if next node exists, make next front
    		if(next != null)
    		{
        		vertex = next.vertex;
        		color = next.color;
        		prev = next.prev;
        		next = next.next;
    		}
    		// else empty queue
    		else
    		{
    			vertex = 0;
    			color = 0;
    			prev = null;
    		}
    		return q;
    }
    
    // Checks if Queue is empty
    public boolean isEmpty()
    {
    		if(vertex == 0)
    		{
    			return true;
    		}
    		return false;
    }
}
