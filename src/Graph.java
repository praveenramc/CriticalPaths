import java.util.*;

/**
 * 
 * @author rbk
 * Class to represent a graph
 */

public class Graph implements Iterable<Vertex> {

	/**
	 * Constructor for Graph
	 * 
	 * @param size
	 *            : int - number of vertices
	 */
	Graph(int size) {
		N = size;
		V = new Vertex[size + 1];
		// create an array of Vertex objects
		for (int i = 1; i <= size; i++)
			V[i] = new Vertex(i);
	}

	/**
	 * Method to add an arc to the graph
	 * 
	 * @param a
	 *            : int - the head of the arc
	 * @param b
	 *            : int - the tail of the arc
	 * @param weight
	 *            : int - the weight of the arc
	 * @param isDirected
	 *            : boolean - tells us whether the graph is directed or not
	 */
	void addEdge(int a, int b, int weight, boolean isDirected) {
		Edge e = new Edge(V[a], V[b], weight);
		V[a].Adj.add(e);

		// add the reverse edge if the graph is not directed
		if (!isDirected) {
			V[b].Adj.add(e);
		}
	}

	/**
	 * Method to create an instance of VertexIterator
	 */
	public Iterator<Vertex> iterator() {
		return new VertexIterator<Vertex>(V, N);
	}

	/**
	 * Method to initialize a graph 1) Sets the parent of every vertex as null
	 * 2) Sets the seen attribute of every vertex as false 3) Sets the distance
	 * of every vertex as infinite
	 * 
	 * @param g
	 *            : Graph - The reference to the graph
	 */
	void initialize() {
		for (Vertex u : this) {
			u.seen = false;
			u.parent = null;
			u.weight = Globals.INFINITY;
		}
	}

	/**
	 * Calculates the indegree of each vertex and updates the indegree feild of
	 * each vertex
	 * 
	 */
	public void computeInDegreeOfEachVertex() {
		for (Vertex vertex : this) {
			for (Edge e : vertex.Adj) {
				e.otherEnd(vertex).indegree += 1;
			}
		}
	}

	/**
	 * Method to print the graph
	 * 
	 * @param g
	 *            : Graph - The reference to the graph
	 */
	void printGraph() {
		for (Vertex u : this) {
			System.out.print(u + ": ");
			for (Edge e : u.Adj) {
				System.out.print(e);
			}
			System.out.println();
		}
	}
	
	
	//Instance variables
	public Vertex[] V; // array of vertices
	public int N; // number of verices in the graph

}

/**
 * A Custom Iterator Class for iterating through the vertices in a graph
 * 
 *
 * @param <Vertex>
 */
class VertexIterator<Vertex> implements Iterator<Vertex> {
	protected int nodeIndex = 0;
	protected Vertex[] iterV;// array of vertices to iterate through
	protected int iterN; // size of the array

	/**
	 * Constructor for VertexIterator
	 * 
	 * @param v
	 *            : Array of vertices
	 * @param n
	 *            : int - Size of the graph
	 */
	public VertexIterator(Vertex[] v, int n) {
		nodeIndex = 0;
		iterV = v;
		iterN = n;
	}

	/**
	 * Method to check if there is any vertex left in the iteration Overrides
	 * the default hasNext() method of Iterator Class
	 */
	public boolean hasNext() {
		return nodeIndex != iterN;
	}

	/**
	 * Method to return the next Vertex object in the iteration Overrides the
	 * default next() method of Iterator Class
	 */
	public Vertex next() {
		nodeIndex++;
		return iterV[nodeIndex];
	}

	/**
	 * Throws an error if a vertex is attempted to be removed
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}
}

/**
 * A Custom Iterator Class for iterating in a reverse order through the vertices
 * in a graph
 * 
 *
 * @param <Vertex>
 */
class ReverseIterator<Vertex> extends VertexIterator<Vertex> {

	/**
	 * Constructor for VertexIterator
	 * 
	 * @param v
	 *            : Array of vertices
	 * @param n
	 *            : int - Size of the graph
	 * 
	 * 
	 */

	public ReverseIterator(Vertex[] v, int length) {
		super(v, length);
		nodeIndex = v.length;
		iterV = v;
		iterN = length;
	}

	/**
	 * Method to check if there is any vertex left in the iteration Overrides
	 * the default hasNext() method of Iterator Class
	 */
	public boolean hasNext() {
		return nodeIndex > 1;
	}

	/**
	 * Method to return the next Vertex object in the iteration Overrides the
	 * default next() method of Iterator Class
	 */

	public Vertex next() {

		nodeIndex--;
		return iterV[nodeIndex];
	}

	/**
	 * Throws an error if a vertex is attempted to be removed
	 */
	public void remove() {

		throw new UnsupportedOperationException();
	}
}

/**
 * @author Praveen
 *
 */
class ReversedGraphRepresentation extends Graph {
	ReversedGraphRepresentation(int size) {
		super(size);
	}

	ReversedGraphRepresentation(Graph g) {
		super(g.N);
		for (int i = 1; i <= this.N; i++) {
			this.V[i].weight = g.V[i].weight;
			for (Edge e : g.V[i].Adj) {
				this.addEdge(e.otherEnd(g.V[i]).name, i, 0, true);
			}
		}
	}
}
