package main.java.Logic.debugging;

import java.util.LinkedHashMap;
import java.util.Map;

public class Timer {
    private Map<String, Long> times;
    private long startTime = -1;
    private long endTime = -1;


    public Timer() {
        times = new LinkedHashMap<>();
    }

    public void start(String startSectionName) {
        startTime = checkpoint(startSectionName);
    }

    public long checkpoint(String nextSectionName) {
        long time = System.currentTimeMillis();
        times.put(nextSectionName, time);
        return time;
    }

    public void finish() {
        endTime = checkpoint("end");
    }

    @Override
    public String toString() {
        if (startTime == -1 || endTime == -1) {
            return "Timer not finished yet!";
        }
        long executionTime = endTime - startTime;
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
                long elapsedTime = entry.getValue() - previousTime;
                sb.append(elapsedTime);
                float percentage = (float)elapsedTime / (float)executionTime;
                sb.append("ms \n    Percentage: ");
                sb.append(percentage);
                sb.append('\n');
                sb.append('\n');
            }
            previousSection = entry.getKey();
            previousTime = entry.getValue();
            i++;
        }
        return sb.toString();
    }
}
