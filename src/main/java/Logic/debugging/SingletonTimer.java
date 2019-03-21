package main.java.Logic.debugging;

/**
 * Wraps the {@link Timer} class in a singleton class wrapper so that we can use the same timer for
 * the whole program.
 */
public class SingletonTimer extends Timer {
    private static SingletonTimer instance;

    /**
     * Singleton pattern getter for the class.
     * @return The global instance for the SingletonTimer.
     */
    public static synchronized SingletonTimer get() {
        if (instance == null) {
            instance = new SingletonTimer();
        }
        return instance;
    }

    /**
     * Private constructor for the singleton pattern.
     */
    private SingletonTimer() { super(); }
}
