package com.rmp.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.rmp.exception.InvalidFormatException;
import com.rmp.exception.JsonException;

// TODO store the class in the json to have a dynamic access
public class Json {

    //////////// method to create a json from a java object ////////////

    /**
     * Create a json based on Field from any class
     * @param <T>
     * @param objectList
     * @return a json in a string format
     */
    public static <T> String createJsonFromList(List<T> objectList) {
        try {
            List<String> testList = new ArrayList<>();

            for (T t : objectList) {
                testList.add(convertObjectToString(t));
            }

            return testList.toString();
        } catch (Exception exception) {
            throw new JsonException("Failed to create the json", exception.getCause());
        }
    }

    /**
     * convert an object to string with all field as string
     * @param <T>
     * @param objectToConvert
     * @return
     * @throws IllegalAccessException
     */
    private static <T> String convertObjectToString(T objectToConvert) throws IllegalAccessException {
        List<String> fieldListString = new ArrayList<String>();
        Field[] fields = objectToConvert.getClass().getDeclaredFields();
        
        for (Field field : fields) {
            field.setAccessible(true);
            
            String key = field.getName();
            String value = field.get(objectToConvert).toString();

            fieldListString.add(convertKeyValueFromString(key, value));
        }
        
        return joinListToJsonObject(fieldListString);
    }

    //////////// method to read a json to an java object ////////////

    /**
     * Transform a json in a string format to an java object by passing the class name in param with the json.
     * @param className corresponding to the object in json 
     * @param jsonString
     * @return a list of object corresponding to the class passed in param
     */
    public static <T> List<T> readJsonToList(String className, String jsonString) {
        try {
            if (!jsonString.startsWith("[")) {
                throw new InvalidFormatException("Data is not a json");
            }

            List<T> listToSend = new ArrayList<>();
            List<Map<String, String>> objectListInMap = getListMapFromArray(jsonString);
            Class<?> classDefinition = Class.forName(className);
            Field[] fields = classDefinition.getDeclaredFields();
            
            for (Map<String,String> map : objectListInMap) {
                List<String> constructorParamList = new ArrayList<>();
                for (Field field : fields) {
                    constructorParamList.add(map.get(field.getName()));
                }

                // TODO refactor the new instance. (create method too)
                Object[] constructorParams = constructorParamList.toArray();
                T object = (T) classDefinition.getDeclaredConstructors()[0].newInstance(constructorParams);

                listToSend.add(object);
            }
                    
            return listToSend;
        } catch (ClassNotFoundException e) {
            throw new JsonException("Error during the reading of the json: " + e.getMessage(), e.getCause());
        } catch (InstantiationException e) {
            throw new JsonException("Error during the reading of the json: " + e.getMessage(), e.getCause());
        } catch (IllegalAccessException e) {
            throw new JsonException("Error during the reading of the json: " + e.getMessage(), e.getCause());
        } catch (InvocationTargetException e) {
            throw new JsonException("Error during the reading of the json: " + e.getMessage(), e.getCause());
        } catch (SecurityException e) {
            throw new JsonException("Error during the reading of the json: " + e.getMessage(), e.getCause());
        }
    }
    
    /**
     * Create a list of map from a json array in string ex: [{ "test2": "test2", "test3": "test3" }, { "test4": "test4", "test5": "test5" }]
     * @param array with the object inside
     * @return the list
     */
    private static List<Map<String, String>> getListMapFromArray(String array) {
        if (!array.startsWith("[{") && !array.endsWith("}]")) {
            throw new InvalidFormatException("Must be an array of object");
        }

        List<Map<String, String>> objectList = new ArrayList<Map<String, String>>();
          
        String[] objectArray = removeHook(array).split("},");
        
        for (String object : objectArray) {
            object = removeBrace(object);
            // for each object, get the key value and put it in the objectList
            Map<String, String> objectKeyValue = getKeyValue(object.trim());
            
            objectList.add(objectKeyValue);
        }

        return objectList;
    }

    /**
     * Get a HashMap of key value from a json object
     * @param jsonObject as "location": "location", "name": "test", "playerId": "b37d3ee9-17ca-4f03-a869-7e1a9c0e4a88"
     * @return
     */
    private static HashMap<String, String> getKeyValue(String jsonObject) {
        if (!jsonObject.startsWith("\"") && !jsonObject.endsWith("\"")) {
            throw new InvalidFormatException("Must be key value");
        }

        HashMap<String, String> keyValueMap = new HashMap<>();

        String[] splittedObject = jsonObject.split(",");

        for (String keyValue : splittedObject) {
            String[] keyValuesplitted = keyValue.split(":");
            String key = removeDoubleQuote(keyValuesplitted[0]);
            String value = removeDoubleQuote(keyValuesplitted[1]);

            keyValueMap.put(key.trim(), value.trim());
        }

        return keyValueMap;
    }

    //////////// reused method ////////////

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
    
    private static String removeHook(String string) {
        string = string.replace("[", "");
        string = string.replace("]", "");
        return string;
    }

    /**
     * Remove double quote if the string start with a double quote 
     * @param string
     * @return 
     */
    private static String removeDoubleQuote(String string) {
        return string.replace("\"", "");
    }
    
    private static String convertKeyValueFromString(String key, String value) {
        return "\"" + key + "\"" + ": " + "\"" + value + "\"";
    }
    
    private static String joinListToJsonObject(List<String> listString) {
        return "{ " + String.join(", ", listString) + " }";
    }
}
