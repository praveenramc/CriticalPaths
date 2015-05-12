import java.util.LinkedList;


/**
	 * Class to represent a vertex of a graph
	 * 
	 *
	 */
	public class Vertex implements Comparable<Vertex> {
		public int name; // name of the vertex
		public boolean seen; // flag to check if the vertex has already been
								// visited
		public Vertex parent; // parent of the vertex
		public int weight; // field for storing int attribute of vertex
		public int indegree; // stores InDegree of vertex
		public long maxWeight; //stores the maximum weight of the vertex from source.
		public LinkedList<Edge> Adj; // adjacency list

		/**
		 * Constructor for the vertex
		 * 
		 * @param n
		 *            : int - name of the vertex
		 */
		Vertex(int n) {
			name = n;
			seen = false;
			weight = 0;
			maxWeight = 0;
			parent = null;
			indegree = 0;
			Adj = new LinkedList<Edge>();
		}

		/**
		 * Method used to order vertices, based on algorithm
		 */
		public int compareTo(Vertex v) {
			return this.weight - v.weight;
		}

		/**
		 * Method to represent a vertex by its name
		 */
		public String toString() {
			return Integer.toString(name);
		}
	}