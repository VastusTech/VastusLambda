package main.java.logic.debugging;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class is designed to help do complete time testing and pretty printing of the time results.
 */
public class Timer {
    private Map<String, Long> times;
    private long startTime = -1;
    private long endTime = -1;

    /**
     * Constructor for the Timer class, simply initializes the times map.
     */
    public Timer() {
        times = new LinkedHashMap<>();
    }

    /**
     * Starts the total timer and adds a section to the times.
     * @param startSectionName The name of the first section being timed.
     */
    public void start(String startSectionName) {
        startTime = checkpoint(startSectionName);
    }

    /**
     * Puts another section into the times map with a section name to it.
     * @param nextSectionName The name of the next section to be timed.
     * @return The current time in nanoseconds.
     */
    public long checkpoint(String nextSectionName) {
        long time = System.nanoTime();
        times.put(nextSectionName, time);
        return time;
    }

    /**
     * Ends the total timer and adds a final section to the times.
     */
    public void finish() {
        endTime = checkpoint("end");
    }

    /**
     * Gives all the diagnostic time-based information for the timer, including which sections took
     * the longest and potentially where we can cut down on execution time!
     * @return The string of the diagnostic report.
     */
    @Override
    public String toString() {
        if (startTime == -1 || endTime == -1) {
            return "Timer not finished yet!";
        }
        float executionTime = (float)(endTime - startTime) / 1000000.0f;
        StringBuilder sb = new StringBuilder();
        sb.append("===========================================\n");
        sb.append("=              EXECUTION TIME             =\n");
        sb.append("===========================================\n");
        sb.append("**** Total Time: ");
        sb.append(executionTime);
        sb.append("ms ****\n\n");
        sb.append("****           Section Times           ****\n");
        int i = 0;
        String previousSection = null;
        long previousTime = -1;
        for (Map.Entry<String, Long> entry : times.entrySet()) {
            if (i != 0) {
                sb.append("Section ");
                sb.append(i);
                sb.append(" (");
                sb.append(previousSection);
                sb.append("): \n");
                sb.append("    Time Elapsed: ");
                float elapsedTime = (float)(entry.getValue() - previousTime) / 1000000.0f;
                sb.append(elapsedTime);
                float percentage = 100.0f * elapsedTime / executionTime;
                sb.append("ms \n    Percentage: ");
                sb.append(percentage);
                sb.append("%\n\n");
            }
            previousSection = entry.getKey();
            previousTime = entry.getValue();
            i++;
        }
        return sb.toString();
    }
}
