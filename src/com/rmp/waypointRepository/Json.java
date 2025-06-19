package com.rmp.waypointRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Json {

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

                // returnString = addLineSeparator(returnString);
                
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
                        String fieldString = convertKeyValueInJsonString(field1.getName(), field1.get(object).toString());
                        fieldListString1.add(fieldString);
                    }

                    String objectString = joinArrayToJson(fieldListString1);
                    objectList.add(objectString);
                }

                // String array = createArrayString(objectList.toString());
                fieldListString.add(convertKeyValueInJsonArray(field.getName(), objectList.toString()));
            } else {
                String fieldString = convertKeyValueInJsonString(field.getName(), field.get(objectToConvert).toString());
                fieldListString.add(fieldString);
            }
        }

        String objectString = joinArrayToJson(fieldListString);
        return returnString + objectString;
    }

    private static String convertKeyValueInJsonString(String key, String value) {
        return "\"" + key + "\"" + ": " + "\"" + value + "\"";
    }

    private static String convertKeyValueInJsonArray(String key, String value) {
        return "\"" + key + "\"" + ": " + value;
    }

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
