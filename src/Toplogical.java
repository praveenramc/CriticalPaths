import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Topological class helps us to generate the topological
 * ordering of the vertex. 
 * 
 * To compute the topological ordering of vertices we use the
 * algorithm that is based on indegree of a vertex. 
 * 
 * @author Praveen
 *
 */
public class Toplogical implements Iterable<Vertex> {

	/**
	 * Constructor method
	 * @param graph
	 */
	public Toplogical(Graph graph) {
		vertices = new Vertex[graph.V.length];
		graph.computeInDegreeOfEachVertex();
	}

	
	/**
	 * First step in finding topological order.
	 * Find all the vertex with zero indegree and add it to a queue
	 * 
	 * @param graph Graph g
	 * @return returns a queue of vertices
	 */
	private Queue<Vertex> findZeroIndegrees(Graph graph) {
		Queue<Vertex> vertices = new LinkedList<Vertex>();

		for (Vertex v : graph) {
			if (v.indegree == 0) {
				vertices.add(v);
			}
		}
		
		//If there is no vertices in the queue then the
		//graph has cycles. So no topological ordering is possible
		if(vertices.size() == Globals.ZERO) {
			throw new UnsupportedOperationException("Graph has cycles");
		}
		
		return vertices;
	}

	/**
	 * depthfirstorder computes the topology of graph and
	 * stores the vertices in sorted order based on indegree 
	 * 
	 * @param graph
	 */
	public void depthFirstOrder(Graph graph) {
		Queue<Vertex> zeroDegreeVertex  = findZeroIndegrees(graph);
		int index = 1;

		while (!zeroDegreeVertex.isEmpty()) {
			Vertex vertex = zeroDegreeVertex.remove();

			for (Edge edge : vertex.Adj) {
				Vertex adjacentVertex = edge.otherEnd(vertex);
				adjacentVertex.indegree -= 1;

				if (adjacentVertex.indegree == 0) {
					zeroDegreeVertex.add(adjacentVertex);
				}
			}

			vertices[index++] = vertex;
		}

		// We need make sure all the verrtices are in the toplogical sorted
		// graph
		
		if(isAllVertciesInTopologicalOrder(index, graph.V.length)) {
			vertices = null;
		}
	}

	private boolean isAllVertciesInTopologicalOrder(int count, int length) {
		return count != length;
	}
	
	//====================================================//
	//For debugging purpose
	//====================================================//
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
    	String printStatements = "";
    	
    	for (Vertex vertex : this) {
    		printStatements += vertex + " ";
    	}
    	return printStatements;
    }
	
	/* 
	 * Returns a iterator that iterated the vertex from 0 to n
	 */
	public Iterator<Vertex> iterator() {
    	
    	return new VertexIterator<Vertex>(vertices, vertices.length-1);
    }
	
	/* 
	 * Returns a iterator that iterated the vertex from n to 0
	 */
	public Iterator<Vertex> reverseIterator() {
    	
    	return new ReverseIterator<Vertex>(vertices, vertices.length-1);
    }

	
	/**
	 * Vertices array to store the vertices in toplogical order
	 */
	Vertex[] vertices;
}
