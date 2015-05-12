import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) {
		Helper.timer();
		PertChart pc = null;
		Scanner in = null;

		if (args.length > 0) {

			File inputFile = new File(args[0]);
			try {
				in = new Scanner(inputFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			in = new Scanner(System.in);
		}

		// Make sure that we skip the comment lines
		if (in.hasNext(Pattern.compile(Globals.PATTERN))) {
			in.nextLine();
		}

		int numberOfTask = in.nextInt();
		int precedenceConstraint = in.nextInt();
		pc = new PertChart(numberOfTask);
		pc.computeTaskDuration(in, numberOfTask);
		pc.computeTaskPrecedenceConstraint(in, precedenceConstraint);
		pc.computeCriticalPaths(in);
		System.out.println(pc);
		Helper.timer();
	}
}
