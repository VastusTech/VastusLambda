package main.java;

import main.java.Logic.Constants;

public class Main {
    public static void main(String[] args) {
        Constants.ifTesting = true;
        // We don't want to actually connect to any database or AWS service in testing
    }
}
