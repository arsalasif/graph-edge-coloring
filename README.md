# Graph-Edge-Coloring
An implementation of a Graph Edge Coloring algorithm. Colors the graph in delta or delta + 1 max colors. Read the graph-theory-report-final.pdf for more information.

Graph must be a connected graph for the algorithm to work.


To add your test graphs:
- You can put them in `test_graphs` folder
- The code works for only one graph at a time, so you will manually update the `fileName` in the Main class for each graph, and run them one by one.

The format for graphs must be like this:
```
graphName
numberOfVertices
Edges
0
```

For example,
```
Simple
4 <- number of vertices
-1 2 3 4 <- edges from vertex 1
-2 3 <- edges from vertex 2
-3 4 <- edges from vertex 3
0
```
Note that it must include a `0` at the last row, and all rows describing edges must begin with a `-`
