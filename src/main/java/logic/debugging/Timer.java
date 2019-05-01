package main.java.logic.debugging;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * This class is designed to help do complete time testing and pretty printing of the time results.
 */
public class Timer {
    // This represents how much it indents when going into a sub-section.
    final static private String sectionIndent = "  ";
    final static private float timeToMillisecondsConversion = 1000000.0f;

    /**
     * This class represents a section of time for the timer.
     */
    private class TimeSection {
        private String sectionName;
        private long startTime = -1;
        private long endTime = -1;
        private List<TimeSection> subSections;

        /**
         * Constructor for the TimeSection class, immediately using the current time as the start
         * time for the section.
         *
         * @param sectionName The identifying string for the section.
         */
        TimeSection(String sectionName) {
            this.sectionName = sectionName;
            this.startTime = getCurrentTime();
            subSections = new ArrayList<>();
        }

        /**
         * Extra Constructor for the TimeSection class, which gets passed in a time in order to
         * maintain synchronous-ness with adjacent time sections.
         *
         * @param sectionName The string identifier of this time section.
         * @param currentTime The current time to start the time section with.
         */
        TimeSection(String sectionName, long currentTime) {
            this.sectionName = sectionName;
            this.startTime = currentTime;
            subSections = new ArrayList<>();
        }

        /**
         * Adds a subsection time section to the time section.
         *
         * @param section The sub section time section within the time section.
         */
        void add(TimeSection section) {
            subSections.add(section);
        }

        /**
         * Finishes the time section using the system's current time to finish it.
         */
        void finish() {
            endTime = getCurrentTime();
        }

        /**
         * Finishes the time section, getting passed in a current time to maintain synchronous-ness
         * between adjacent time sections.
         *
         * @param currentTime The current time to set to the end time of the time section.
         */
        void finish(long currentTime) {
            endTime = currentTime;
        }

        /**
         * Gets the string statistics report for a single TimeSection, including all of
         * its sub-sections. This includes percentages and pretty indenting.
         *
         * @param parentSectionTime The section time for the parent of the section.
         * @param index The index that this section is
         * @param indent The current indent level for the section.
         * @return
         */
        public String getStatistics(float parentSectionTime, int index, String indent) {
            float executionTime = getExecutionTime();
            float percentage = percentage(parentSectionTime);
            StringBuilder sb = new StringBuilder();
            sb.append(indent);
            sb.append(index);
            sb.append(". ");
            sb.append(sectionName);
            sb.append(" (");
            sb.append(executionTime);
            sb.append("ms");
            if (parentSectionTime != -1) {
                sb.append(", ");
                sb.append(percentage);
                sb.append("%");
            }
            sb.append("):\n");
            for (int i = 0; i < subSections.size(); i++) {
                sb.append(subSections.get(i).getStatistics(executionTime, i + 1,
                        indent + sectionIndent));
            }
            return sb.toString();
        }

        private float getExecutionTime() {
            return (float)(endTime - startTime) / timeToMillisecondsConversion;
        }

        private float percentage(float parentExecutionTime) {
            return 100.0f * (getExecutionTime() / parentExecutionTime);
        }
    }

    private TimeSection fullTimeSection;
    private Stack<TimeSection> timeSectionStack;

    /**
     * Constructor for the Timer class, simply initializes the times map.
     */
    public Timer() {
        timeSectionStack = new Stack<>();
    }

    /**
     * Starts the total timer, sets the full time section to start now, and adds a first sub section
     * for the timer.
     *
     * @param startSectionName The name of the first sub-section being timed.
     */
    public void start(String startSectionName) {
        long currentTime = getCurrentTime();
        fullTimeSection = new TimeSection("Lambda Execution", currentTime);
        pushCheckpoint(startSectionName, currentTime);
    }

    /**
     * Puts another section deeper into the time stack with a section name for it. This is used
     * primarily for initially going into a new sub section from a main section.
     *
     * @param nextSectionName The name of the next section to be timed.
     */
    public void pushCheckpoint(String nextSectionName) {
        if (fullTimeSection != null) {
            TimeSection newTimeSection = new TimeSection(nextSectionName);
            timeSectionStack.peek().add(newTimeSection);
            timeSectionStack.push(newTimeSection);
        }
    }

    /**
     * Puts another section deeper into the time stack with a section name for it. This is a private
     * method, which manually defines the current time to maintain synchronous-ness between adjacent
     * time sections.
     *
     * @param nextSectionName The name of the next section to be timed.
     * @param currentTime The system current time to start the checkpoint with.
     */
    private void pushCheckpoint(String nextSectionName, long currentTime) {
        TimeSection newTimeSection = new TimeSection(nextSectionName, currentTime);
        if (timeSectionStack.size() == 0) {
            fullTimeSection.add(newTimeSection);
        }
        else {
            timeSectionStack.peek().add(newTimeSection);
        }
        timeSectionStack.push(newTimeSection);
    }

    /**
     * Puts a number of sections deeper into the time stack with section names for them. This is
     * primarily used when you are starting a new big section and also smaller sub-sections, so that
     * they all start at the same time.
     *
     * @param nextSectionNames The array next section names to add onto the stack.
     */
    public void pushCheckpoints(String... nextSectionNames) {
        if (fullTimeSection != null) {
            long currentTime = getCurrentTime();
            for (String nextSectionName : nextSectionNames) {
                pushCheckpoint(nextSectionName, currentTime);
            }
        }
    }

    /**
     * Ends the current section and starts the next checkpoint. This is primarily used for when
     * there is a linear motion from one section to the next, without any sub-section action. Makes
     * sure that the end time and the start time of the two sections are equal.
     *
     * @param nextSectionName The next section name to start.
     */
    public void endAndPushCheckpoint(String nextSectionName) {
        if (fullTimeSection != null) {
            long currentTime = getCurrentTime();
            endCheckpoint(currentTime);
            pushCheckpoint(nextSectionName, currentTime);
        }
    }

    /**
     * This insane method ends a number of checkpoints and then adds a number of sections to the
     * stack. This is for when a certain section ends and you want to immediately start the next
     * one.
     *
     * @param numberOfCheckpoints The number of checkpoints to end.
     * @param nextSectionNames The next section names for the next sections.
     */
    public void endAndPushCheckpoints(int numberOfCheckpoints, String... nextSectionNames) {
        if (fullTimeSection != null) {
            long currentTime = getCurrentTime();
            for (int i = 0; i < numberOfCheckpoints; i++) {
                endCheckpoint(currentTime);
            }
            for (String nextSectionName : nextSectionNames) {
                pushCheckpoint(nextSectionName, currentTime);
            }
        }
    }

    /**
     * Ends a section and goes up the stack.
     */
    public void endCheckpoint() {
        if (fullTimeSection != null) {
            timeSectionStack.pop().finish();
        }
    }

    /**
     * Ends a number of sections and goes as far up the stack as is indicated.
     *
     * @param numberOfCheckpoints The number of checkpoints to finish at the same time.
     */
    public void endCheckpoints(int numberOfCheckpoints) {
        if (fullTimeSection != null) {
            long currentTime = getCurrentTime();
            for (int i = 0; i < numberOfCheckpoints; i++) {
                endCheckpoint(currentTime);
            }
        }
    }

    /**
     * Private method to manually end a checkpoint with a given current time.
     *
     * @param currentTime The current time to set the end time to.
     */
    private void endCheckpoint(long currentTime) {
        timeSectionStack.pop().finish(currentTime);
    }

    /**
     * Gets the current time as we are using it for the Timer. In this case, we are getting
     * nanoseconds.
     *
     * @return The long time that we are using to describe the time with.
     */
    private long getCurrentTime() {
        return System.nanoTime();
    }

    /**
     * Ends the total timer. TODO Do some checking to make sure the stack is empty? Or just end all?
     */
    public void finish() {
        fullTimeSection.finish();
    }

    /**
     * Gives all the diagnostic time-based information for the timer, including which sections took
     * the longest and potentially where we can cut down on execution time!
     *
     * @return The string of the diagnostic report.
     */
    @Override
    public String toString() {
        if (timeSectionStack.size() != 0) {
            return "Timer not finished at the end, still stack items remaining!";
        }
        if (fullTimeSection.endTime == -1) {
            return "Timer not finished at the end, must call .finish() at the end!";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("===========================================\n");
        sb.append("=              EXECUTION TIME             =\n");
        sb.append("===========================================\n");
        sb.append("**** Total Time: ");
        sb.append(fullTimeSection.getExecutionTime());
        sb.append("ms ****\n\n");
        sb.append("****           Section Times           ****\n");
        sb.append(fullTimeSection.getStatistics(-1, 1, ""));
        return sb.toString();
    }
}
