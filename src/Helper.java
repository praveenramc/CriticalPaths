import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * @author Praveen
 * Helper class that has static methods.
 *
 */
public class Helper {
	
	private static int phase = 0;
	private static long startTime, endTime, elapsedTime;

	
	/**
	 * Timer functionality. Helps us to measure the amount of time the program utilized 
	 * to produce an output
	 */
	public static void timer()
    {
        if(phase == 0) {
	    startTime = System.currentTimeMillis();
	    phase = 1;
	} else {
	    endTime = System.currentTimeMillis();
            elapsedTime = endTime-startTime;
            System.out.println("Time: " + elapsedTime + " msec.");
            memory();
            phase = 0;
        }
    }
	
	/**
	 * Displays the amount memory the program used
	 */
	public static void memory() {
        long memAvailable = Runtime.getRuntime().totalMemory();
        long memUsed = memAvailable - Runtime.getRuntime().freeMemory();
        System.out.println("Memory: " + memUsed/1000000 + " MB / " + memAvailable/1000000 + " MB.");
    }

}
