package com.rmp.waypointRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Json {
    /**
     * Create a json based on Field from any class
     * @param <T>
     * @param classList
     * @return a json in a string format
     */
    public static <T> String createJsonFromString(List<T> classList) {
        if (!classList.isEmpty()) {
            try {
                String returnString = "";
                
                for (T object : classList) {
                    List<String> fieldListString = new ArrayList<String>();
                    Field[] fields = object.getClass().getDeclaredFields();
                    
                    // TODO if a field is not a string it should iterate on it like here
                    for (Field field : fields) {
                        field.setAccessible(true);
                        String fieldString = "\"" + field.getName() + "\"" + ": " + "\"" + field.get(object) + "\"";
                        fieldListString.add(fieldString);
                    }

                    String objectString = "{ " + String.join(", ", fieldListString) + " }";
                    returnString = returnString + objectString;
                }

                // returnString = addLineSeparator(returnString);
                
                return returnString;
            } catch (IllegalAccessException exception) {
                
            }
        }

        return "{}";
    }

    // 4 offset to add
    // other method to separate the string ?
    /**
     * add a line after each "," 
     * @param stringToSeparate
     * @return
     */
    private static String addLineSeparator(String stringToSeparate) {
        String[] splittedString = stringToSeparate.split(",");
        String stringToReturn = "";

        for (String string : splittedString) {
            string = string + "," + System.lineSeparator();
            stringToReturn = stringToReturn + string;
        }

        return stringToReturn;
    }
}
