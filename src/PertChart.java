import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * @author Praveen
 *
 */
public class PertChart extends Graph {

	/**
	 * @param size
	 */
	public PertChart(int size) {

		// The number of vertices should be now original vetices in the
		// graph plus two new vertices called predecessor and sucessor nodes
		super(size + 2);
		addPredecessorAndSucessor();
	}

	/**
	 * Adds two dummy nodes and connects every other node
	 * to the dummy nodes
	 */
	private void addPredecessorAndSucessor() {

		// Lets add a predeccor and sucessor node for all tasks
		for (int i = 1; i <= n; i++) {
			// predecessor node
			addEdge(N - 1, i, 0, true);

			// sucessorNode
			addEdge(i, N, 0, true); // connect all the tasks to the dummy end
									// task
		}
	}

	/**
	 * The method creates a graph based on the precedence task
	 * @param in Input scanner
	 * @param precedenceCount Number of precedence constraints
	 */
	public void computeTaskPrecedenceConstraint(Scanner in, int precedenceCount) {
		for (int i = 0; i < precedenceCount; i++) {
			addEdge(in.nextInt(), in.nextInt(), 0, true);
		}
	}

	/**
	 * 
	 * @param input Scanner
	 * @param taskCount number of Tasks
	 */
	public void computeTaskDuration(Scanner input, int taskCount) {
		for (int i = 1; i <= taskCount; i++) {
			V[i].weight = input.nextInt();
		}

	}

	/**
	 * Computes the number of critical task that has
	 * to be completed on time
	 * 
	 */
	private void computeCriticalTaskCounts() {
		criticalTaskCounter = 0;
		int n = N - 2; // We need not to iterate the two dummy nodes
		for (int i = 1; i <= n; i++) {
			if (slack[i] == 0) {
				criticalTaskCounter += 1;
			}
		}
	}


	private void computeCriticalPathLengths() {
		criticalPathLength = ec[N];
	}

	/**
	 * Computes the critical path length by finding the longest 
	 * path in the graph
	 */
	private void computeCriticalPaths() {
		Dijkstras lp = new Dijkstras(this, N - 1, N);
		criticalPath = lp.DFS();
	}

	/**
	 * Computes the clack variable.
	 * A slack value of zero would mean the task has
	 * be done by the given time
	 */
	private void computeSlack() {
		for (Vertex v : topological) {
			slack[v.name] = lc[v.name] - ec[v.name];
		}
	}

	/**
	 * Computes the LC time based on the following recurrence
	 * 
	 * Base Case:
	 * LC[t] = EC[t]
	 * 
	 * LC[u] = LC[si] - d[si]
	 * 
	 * For computing LC we use iterate the generated topological order
	 * in reverse order
	 *  
	 */
	public void lcTime() {
		long minimumTime = 0;
		Iterator<Vertex> revIter = topological.reverseIterator();
		
		//We don't want to do anything with the sucessor node that
		//we added. So just skip it
		Vertex vertex = revIter.next();

		lc[vertex.name] = ec[vertex.name];
		
		
		while (revIter.hasNext()) {
			vertex = revIter.next();
			LinkedList<Edge> sList = this.V[vertex.name].Adj;
			lc[vertex.name] = Long.MAX_VALUE;

			for (Edge e : sList) {
				Vertex sucessorTaskNodes = e.otherEnd(vertex);
				minimumTime = lc[sucessorTaskNodes.name] - sucessorTaskNodes.weight;

				if (minimumTime < lc[vertex.name]) {
					lc[vertex.name] = minimumTime;
				}
			}
		}

	}

	/**
	 * Computes the earliest completeion time based on the 
	 * recurrence relation given by
	 * 
	 * Base case: 
	 * EC[s] = 0;
	 * 
	 * EC[u] = EC[Pi]+d[u]
	 */
	public void ecTime() {
		for (Vertex v : topological) {
			LinkedList<Edge> precedenceTaskList = reveresedGraph.V[v.name].Adj;
			ec[v.name] = 0;

			for (Edge e : precedenceTaskList) {
				Vertex pTask = e.otherEnd(reveresedGraph.V[v.name]);
				if (ec[pTask.name] >= ec[v.name]) {
					ec[v.name] = ec[pTask.name];
				}
			}
			ec[v.name] += v.weight;
		}
	}

	/**
	 * Starts the computation process for finding 
	 * critical paths in the given project
	 * 
	 */
	public void computeCriticalPaths(Scanner in) {
		reveresedGraph = new ReversedGraphRepresentation(this);
		topological = new Toplogical(this);
		topological.depthFirstOrder(this);
		ecTime();
		lcTime();
		computeSlack();
		computeCriticalPaths();
		computeCriticalPathLengths();
		computeCriticalTaskCounts();
	}

	/*
	 * @return returns the computed critical paths
	 */
	private String getCriticalPaths() {
		StringBuilder string = new StringBuilder();
		int count = 1;

		for (LinkedList<Vertex> path : criticalPath) {
			StringBuilder criticalPathNodes = new StringBuilder();
			for (Vertex vertex : path) {
				criticalPathNodes.append(vertex.name);
				criticalPathNodes.append(Globals.WHITESPACE);
			}

			string.append(count++);
			string.append(Globals.COLON);
			string.append(criticalPathNodes);
			string.append(Globals.ENDOFLINE);
		}

		return string.toString();
	}

	/**
	 * Stored the number of vertices in graph excluding the two dummy nodes.
	 * Helps to avoid recomputation
	 */
	private final int n = N - 2;

	ReversedGraphRepresentation reveresedGraph;
	Toplogical topological;
	long[] ec = new long[super.V.length];
	long[] lc = new long[super.V.length];
	long[] slack = new long[super.V.length];
	long criticalPathLength;
	int criticalTaskCounter;

	LinkedList<LinkedList<Vertex>> criticalPath = new LinkedList<LinkedList<Vertex>>();

	/*
	 * String representation of PertChart class. Helps us to output in the
	 * desired format as mentioned the Project 4 specs
	 * 
	 * Output format: Task EC LC Slack
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		// Lets create a tabular format
		result.append(createTableData());

		return result.toString();
	}

	/**
	 * createTableData() calls two other utility function to build the table
	 * First calls the headerUtility() to build the header and then calls
	 * dataUtility() to create the data
	 * 
	 * @return Creates a table and returns the table as a String
	 * 
	 */
	private String createTableData() {
		StringBuilder sb = new StringBuilder();

		sb.append(headerUtility());
		sb.append(dataUtility());

		return sb.toString();
	}

	private String headerUtility() {
		String[] headers = { Globals.TASK, Globals.EARLIESTCOMPLETION,
				Globals.LATESTCOMPLETION, Globals.SLACK };

		StringBuilder header = new StringBuilder();

		String headerFormatter = "%-" + Globals.WIDTHOFCOLUMN + "s";

		header.append(criticalPathLength);
		header.append(Globals.WHITESPACE);
		header.append(criticalTaskCounter);
		header.append(Globals.WHITESPACE);
		header.append(criticalPath.size() + Globals.ENDOFLINE);
		header.append(getCriticalPaths() + Globals.ENDOFLINE);

		for (String headerName : headers) {

			header.append(String.format(headerFormatter, headerName));
		}

		return header.toString();
	}

	private String dataUtility() {

		StringBuilder columnValues = new StringBuilder();
		String formatter = "%-" + Globals.WIDTHOFCOLUMN + "s";
		columnValues.append(Globals.ENDOFLINE);
		for (int i = 1; i <= n; i++) {
			columnValues.append(String.format(formatter, i));
			columnValues.append(String.format(formatter, ec[i]));
			columnValues.append(String.format(formatter, lc[i]));
			columnValues.append(String.format(formatter, slack[i]));
			columnValues.append(Globals.ENDOFLINE);
		}

		return columnValues.toString();
	}
}
