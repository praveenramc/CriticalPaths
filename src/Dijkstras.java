import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * The class implements a modified version of Dijkstra.
 * Instead of finding the shortest path, the methods written 
 * in this class helps us to find the longest path in the graph.
 * 
 * The class extends the Super class Graph and all the instance 
 * variables are made available to the Dijkstra'c class.
 * 
 * Assumptions:
 * 	1. The edge weights are positive (Though we don't have edge 
 * 							weights in this project as weight)
 * 	2. There is neither negative cycle nor a positive cycle.
 * 	3. We check for cycles when we do a toplogical ordering of
 * 	   the vertices
 * 
 * @reference 
 * 1. http://stackoverflow.com/questions/8027180/dijkstra-for-longest-path-in-a-dag
 * 2. http://www.utdallas.edu/~chandra/documents/6363/lec10new.pdf
 * 
 * @author Praveen
 *
 */
public class Dijkstras extends Graph {

	/**
	 * @param graph Graph for which the longest path has to be found
	 * @param source
	 * @param destination
	 */
	public Dijkstras(Graph graph, int source, int destination) {
		super(graph.N);
		this.source = V[destination];
		this.destination = V[source];

		for (Vertex vertex : graph) {
			vertex.maxWeight = 0;
			V[vertex.name].weight = vertex.weight;
			V[vertex.name].maxWeight = 0;
		}
		findAllLongestPath(graph);
	}

	/**
	 * The method will find all longest paths in the graph
	 * @param graph Graph for which the longest path has to be found
	 */
	private void findAllLongestPath(Graph graph) {
		for (Vertex vertex : graph) {
			queue.add(vertex);
		}

		while (!queue.isEmpty()) {
			Vertex vertex = queue.remove();

			for (Edge edge : vertex.Adj) {
				relaxEdges(vertex, edge, queue);
			}
		}

		weight = graph.V[N].maxWeight;
	}

	/**
	 * The following condition should hold to relax edges
	 * 	
	 * RELAX(u, v, w)
	 *		if d[v] > d[u] + w(u, v)
	 *		then d[v] = d[u] + w(u, v)
	 *		d[v] = u
	 *		
	 * @param v Vertex for which we are deciding to update the weight or not
	 * @param e Edge associated with the vertex
	 * @param queue A queue that stores the adjacent vertex of v
	 */
	private void relaxEdges(Vertex v, Edge e, PriorityQueue<Vertex> queue) {
		Vertex adjacentVertex = e.otherEnd(v);
		long edgeDistance = v.maxWeight + adjacentVertex.weight;

		if (edgeDistance == adjacentVertex.maxWeight) {
			addEdge(adjacentVertex.name, v.name, Globals.ZERO, true);
		}
		if (edgeDistance > adjacentVertex.maxWeight) {
			adjacentVertex.maxWeight = edgeDistance;
			V[adjacentVertex.name].maxWeight = adjacentVertex.maxWeight;
			queue.remove(adjacentVertex);
			queue.add(adjacentVertex);
			V[adjacentVertex.name].Adj.clear();
			addEdge(adjacentVertex.name, v.name, Globals.ZERO, true);

		}
	}

	/**
	 * Calls the DFS method from the given vertex
	 * @return returns a set of longest paths stored in linklist of
	 * 		   linklist
	 */
	public LinkedList<LinkedList<Vertex>> DFS() {
		DFS(source);
		return longestPaths;
	}

	/**
	 * Calls the DFS method from the given vertex
	 * @param v vertex from which the DFS has to be performed
	 */
	private void DFS(Vertex v) {
		//To avoid recomputation
		int n = N-2;
		if ((v == destination) && (pathLength == weight)) {
			longestPaths.add(new LinkedList<Vertex>(path));
		}

		for (Edge e : v.Adj) {
			Vertex otherEnd = e.otherEnd(v);
			if (otherEnd.name <= n) {
				path.addFirst(otherEnd);
			}
			
			//add the updated weight
			pathLength = pathLength + otherEnd.weight;
			
			DFS(otherEnd);
			
			//subtract the weight to bring back to graph to initial state
			pathLength = pathLength - otherEnd.weight;

			if (otherEnd.name <= n) {
				path.removeFirst();
			}
		}
	}

	/**
	 * Source vertex from which the longest path has to be found
	 */
	Vertex source;
	/**
	 * Destination Vertex to which the longest path has to be found
	 */

	Vertex destination;
	/**
	 * weight
	 */
	Long weight;

	private LinkedList<LinkedList<Vertex>> longestPaths = new LinkedList<LinkedList<Vertex>>();

	PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>(N, new PQComparator());

	private static LinkedList<Vertex> path = new LinkedList<Vertex>();
	private static long pathLength;
}

