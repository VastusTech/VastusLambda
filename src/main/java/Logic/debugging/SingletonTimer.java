package main.java.Logic.debugging;

public class SingletonTimer extends Timer {
    private static SingletonTimer instance;

    public static synchronized SingletonTimer get() {
        if (instance == null) {
            instance = new SingletonTimer();
        }
        return instance;
    }

    private SingletonTimer() { super(); }
}
