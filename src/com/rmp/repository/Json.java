package com.rmp.repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.rmp.model.PlayerWaypoint;
import com.rmp.model.RegisteredWaypoint;
import com.rmp.waypoint.WaypointManager;

// TODO store the class in the json to have a dynamic access
// TODO create an array in waypoint.json to store multiple object
public class Json {
    private static final String ARRAY_START = "\":[";

    // some brut code from here ///////////////////////////////////
    public static void readJsonToPlayerWaypointsList(String waypointData) {
        // verified if waypointData is the data await
        if (waypointData.startsWith("{")) {
            // init the list
            List<PlayerWaypoint> playerWaypointsList = new ArrayList<PlayerWaypoint>();

            // separate each PlayerWaypoints and put it in a list

            HashMap<String, String> playerWaypointsMap = getKeyValue(waypointData);

            // mock
            List<RegisteredWaypoint> registeredWaypointsList = new ArrayList<RegisteredWaypoint>();

            // remove brut code here for the key
            UUID playerUuid = UUID.fromString(playerWaypointsMap.get("playerUuid"));
            playerWaypointsList.add(new PlayerWaypoint(
                registeredWaypointsList,
                playerUuid,
                playerWaypointsMap.get("playerName")
            ));

            WaypointManager.setList(playerWaypointsList);
        } else {
            throw new IllegalArgumentException("Data is not a json");
        }
    }
    
    /**
     * Create a json based on Field from any class
     * @param <T>
     * @param objectList
     * @return a json in a string format
     */
    public static <T> String createJsonFromString(List<T> objectList) {
        if (!objectList.isEmpty()) {
            try {
                String returnString = "";
                
                for (T t : objectList) {
                    returnString = convertObjectToString(t, returnString);
                }
                
                return returnString;
            } catch (IllegalAccessException exception) {
                throw new IllegalArgumentException("Illegal Access Exception", exception.getCause());
            }
        }
        
        // must return the last value of the json or throw an error
        return "test";
    }
    
    // TODO refactor
    private static <T> String convertObjectToString(T objectToConvert, String returnString) throws IllegalAccessException {
        List<String> fieldListString = new ArrayList<String>();
        Field[] fields = objectToConvert.getClass().getDeclaredFields();
        
        for (Field field : fields) {
            field.setAccessible(true);
            
            if (field.get(objectToConvert) instanceof List list) {
                List<String> objectList = new ArrayList<String>();
                
                for (Object object : list) {
                    List<String> fieldListString1 = new ArrayList<String>();
                    Field[] fields1 = object.getClass().getDeclaredFields();
                    
                    for (Field field1 : fields1) {
                        field1.setAccessible(true);
                        String fieldString = convertKeyValueFromString(field1.getName(), field1.get(object).toString());
                        fieldListString1.add(fieldString);
                    }
                    
                    String objectString = joinArrayToJson(fieldListString1);
                    objectList.add(objectString);
                }
                
                fieldListString.add(convertKeyValueFromArray(field.getName(), objectList.toString()));
            } else {
                String fieldString = convertKeyValueFromString(field.getName(), field.get(objectToConvert).toString());
                fieldListString.add(fieldString);
            }
        }
        
        String objectString = joinArrayToJson(fieldListString);
        return returnString + objectString;
    }

    // end brut code /////////////////////////////////////////////
    
    /**
     * Get a HashMap of key value from a json object
     * @param jsonObject
     * @return
     */
    private static HashMap<String, String> getKeyValue(String jsonObject) {
        // use the reflection to get the value of a field
        Field[] fields = com.rmp.model.PlayerWaypoint.class.getDeclaredFields();
        Bukkit.getLogger().info("Fields: " + fields.toString());

        HashMap<String, String> keyValueMap = new HashMap<>();
        
        // for each field of the class get the value with the field name
        for (Field field : fields) {
            String fieldName = field.getName();
            // TODO handle if a field name is used more than once 
            String valueAfterFieldName = jsonObject.split(fieldName)[1];

            // if an array of object
            if (isValueAnArray(valueAfterFieldName)) {
                createMapFromValueArray(valueAfterFieldName);
            }

            // a key / value in string
            if (isValueAString(valueAfterFieldName)) {
                keyValueMap.put(fieldName, getValueAfterFieldName(valueAfterFieldName));
            }
        }
        
        return keyValueMap;
    }

    /**
     * To get the value when it is a string
     * @return
     */
    private static String getValueAfterFieldName(String valueAfterFieldName) {
        String valueToReturn = valueAfterFieldName;
        // is the last element in json object
        valueToReturn = valueToReturn.split(",")[0].trim();
        valueToReturn = removeBrace(valueToReturn);
        valueToReturn = removeColon(valueToReturn);
        valueToReturn = removeDoubleQuote(valueToReturn.trim());

        return valueToReturn.trim();
    }

    private static void createMapFromValueArray(String valueAfterFieldName) {
        String valueInArray = valueAfterFieldName.split("]")[0];
        valueInArray = valueInArray.replace(ARRAY_START, "");
                
        // split the different object
        if (valueInArray.contains("},")) {
            String[] splittedJsonObject = valueInArray.split("},");

            for (String object : splittedJsonObject) {
                // String objectWithoutBrace = 
                removeBrace(object);
                // TODO continue code to handle an array of object
            }
        }
    }

    private static boolean isValueAnArray(String valueAfterFieldName) {
        return valueAfterFieldName.startsWith(ARRAY_START);
    }

    private static boolean isValueAString(String valueAfterFieldName) {
        return valueAfterFieldName.startsWith("\": \"");
    }

    /**
     * 
     * @param string
     * @return a string without any brace
     */
    private static String removeBrace(String string) {
        String resultString = string;
        
        if (resultString.contains("{") || resultString.contains("}")) {
            resultString = resultString.replace("{", "");
            resultString = resultString.replace("}", "");
        }
        
        return resultString;
    }

    private static String removeColon(String string) {
        return string.replace(":", "");
    }
    
    /**
     * Remove double quote if the string start with a double quote 
     * @param string
     * @return 
     */
    private static String removeDoubleQuote(String string) {
        if (string.startsWith("\"")) {
            return string.replace("\"", "");
        }
        
        return string;
    }
    
    private static String convertKeyValueFromString(String key, String value) {
        return "\"" + key + "\"" + ": " + "\"" + value + "\"";
    }
    
    private static String convertKeyValueFromArray(String key, String arrayValue) {
        return "\"" + key + "\"" + ": " + arrayValue;
    }

    // private static String createArray(String string) {
    //     return "[" + string +"]";
    // }
    
    private static String joinArrayToJson(List<String> listString) {
        return "{ " + String.join(", ", listString) + " }";
    }

    // 4 offset to add
    // other method to separate the string ?
    /**
     * add a line after each "," 
     * @param stringToSeparate
     * @return
     */
    // private static String addLineSeparator(String stringToSeparate) {
    //     String[] splittedString = stringToSeparate.split(",");
    //     String stringToReturn = "";

    //     for (String string : splittedString) {
    //         string = string + "," + System.lineSeparator();
    //         stringToReturn = stringToReturn + string;
    //     }

    //     return stringToReturn;
    // }
}
