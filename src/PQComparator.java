import java.util.Comparator;

/**
 * Comparator class for Priority Queue
 * 
 * @author Praveen
 *
 */
public class PQComparator implements Comparator<Vertex> {
	
	
	/* 
	 * @return 1 if u > v
	 * 		   0 if u == v
	 * 		   -1 if u < v	
	 *
	 */
	@Override
	public int compare(Vertex u, Vertex v) {
		if(u.maxWeight > v.maxWeight) {
			return 1;
		} else if(u.maxWeight < v.maxWeight) {
			return -1;
		} else {
			return 0;
		}
	}	
}