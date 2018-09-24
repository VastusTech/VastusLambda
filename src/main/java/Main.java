package main.java;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

enum Ay {
    LMAO
}
public class Main {
    public static void main(String[] args) {
        // This throws an error
        // main.java.Ay ay = main.java.Ay.valueOf("LMAONOT");
        // float a = Float.parseFloat("0.0f");
//        Set<String> stringSet = new HashSet<>();
//        stringSet.add("1");
//        stringSet.add("3");
//        stringSet.add("2");
//        String[] stringArray = stringSet.toArray(new String[]{"4"});
//        for (String s : stringArray) {
//            System.out.println(s);
        // }
        // System.out.println(stringArray);
        // System.out.println("Hello World!");
        int bound = (int)Math.pow(10, 9);
        System.out.println(bound);
        System.out.println((new Random()).nextInt(bound));
    }
}
