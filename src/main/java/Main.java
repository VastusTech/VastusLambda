package main.java;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.UpdateDatabaseAction;
import org.joda.time.DateTime;

import java.util.*;

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
//        int bound = (int)Math.pow(10, 9);
//        System.out.println(bound);
//        System.out.println((new Random()).nextInt(bound));
//        String birthday = "1998-10-05";
//        DateTime dateTime = new DateTime(birthday);
//        System.out.println(dateTime.getDayOfWeek());
//        try {
//            List<DatabaseAction> databaseActionList = new ArrayList<>();
//            Map<String, DatabaseAction> databaseActionMap = new HashMap<>();
//            DatabaseAction databaseAction = new UpdateDatabaseAction("id", "itemType", "original string", new
//                    AttributeValue("attributeValue"), false, "UPDATE");
//            databaseActionList.add(databaseAction);
//            databaseActionMap.put("id", databaseAction);
//
//            System.out.println(databaseAction.updateAttributeName);
//            databaseActionList.get(0).updateAttributeName = "Changed by reference in list and read by reference in map";
//            System.out.println(databaseAction.updateAttributeName);
//            databaseActionMap.get("id").updateAttributeName = "Change by reference in map and read by reference in " +
//                    "list";
//            System.out.println(databaseAction.updateAttributeName);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
