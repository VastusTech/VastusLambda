package main.java;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.Logic.ItemType;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.UpdateDatabaseAction;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.*;

public class Main {
    public static void main(String[] args) {
         System.out.println(DynamoDBHandler.generateRandomID("Client"));
         String ay = null;
         AttributeValue av = new AttributeValue(ay);
         System.out.println(av.getS());
//        String postType = "newEvent";
//        boolean ifType = ItemType.ifItemType(postType);
//        boolean ifNewType = (postType.substring(0, 3).equals("new")) && ItemType.ifItemType(postType.substring(3));
//        if (!ifType && !ifNewType) {
//            System.out.println("Error!");
//        }
//        String startDate = "2018-11-18T21:41:00-05:00";
//        String endDate = "2018-11-21T"
        // System.out.println(new DateTime().toString());
//        ItemType itemType = ItemType.valueOf("Event");
//        System.out.println(itemType);
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
