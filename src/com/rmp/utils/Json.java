package com.rmp.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rmp.exception.InvalidFormatException;
import com.rmp.exception.JsonException;
import com.rmp.model.ConstructorParams;

// TODO implement the possibility to read an array of object as a value
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
            List<String> arrayList = new ArrayList<>();

            for (T t : objectList) {
                arrayList.add(convertObjectToString(t));
            }

            return joinListToJsonArray(arrayList);
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
            Object value = field.get(objectToConvert);

            // check if is a list of object
            if (value instanceof List valueList) {
                fieldListString.add(toJsonKeyValueArray(key, createJsonFromList(valueList)));
            }

            else {
                fieldListString.add(toJsonKeyValueString(key, value.toString()));
            }
            
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
            
            Class<?> classDefinition = Class.forName(className);
            List<T> instanceList = new ArrayList<>();
            // TODO must handle if a value is an array
            List<Map<String, String>> objectListInMap = getListMapFromArray(jsonString);
            
            for (Map<String, String> map : objectListInMap) {
                
                ConstructorParams constructorParams = createParam(map, classDefinition);

                T newInstance = createInstance(constructorParams, classDefinition);
                
                instanceList.add(newInstance);
            }
            
            return instanceList;
        } catch (ClassNotFoundException e) {
            throw new JsonException("Error during the reading of the json: " + e.getMessage(), e.getCause());
        }
    }

    private static ConstructorParams createParam(Map<String,String> map, Class<?> classDefinition) {
        Field[] fields = classDefinition.getDeclaredFields();

        List<String> paramList = new ArrayList<>();
        List<Class<?>> paramTypeList = new ArrayList<Class<?>>();

        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            String value = map.get(fieldName);

            paramList.add(value);
            paramTypeList.add(field.getType());
        }
        
        return new ConstructorParams(paramList, paramTypeList);
    }

    private static <T> T createInstance(ConstructorParams constructorParams, Class<?> classDefinition) {
        try {
            Class<?>[] paramTypes = constructorParams.ParamTypeList().toArray(new Class<?>[0]);
            Object[] params = constructorParams.ParamList().toArray();

            Constructor<?> constructor = classDefinition.getDeclaredConstructor(paramTypes);
            return (T) constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) 
        {
            throw new JsonException("Error during instance creation: " + e.getMessage(), e.getCause());
        }
    }
    
    /**
     * Create a list of map from a json array in string ex: [{ "test2": "test2", "test3": "test3" }, { "test4": "test4", "test5": "test5" }]
     * @param array with the object inside
     * @return the list
     */
    private static <T> List<Map<String, T>> getListMapFromArray(String array) {
        if (!array.startsWith("[{") && !array.endsWith("}]")) {
            throw new InvalidFormatException("Must be an array of object");
        }

        // TODO use the reflection to get the value for a key

        List<Map<String, T>> objectList = new ArrayList<Map<String, T>>();
        // split the object in the array
        String[] objectArray = removeHook(array).split("},");
        
        for (String object : objectArray) {
            object = removeBrace(object);
            // for each object, get the key value and put it in the objectList
            Map<String, T> objectKeyValue = getKeyValue(object.trim());
            
            objectList.add(objectKeyValue);
        }

        return objectList;
    }

    /**
     * Get a HashMap of key value from a json object
     * @param jsonObject as "location": "location", "name": "test", "playerId": "b37d3ee9-17ca-4f03-a869-7e1a9c0e4a88"
     * @return
     */
    private static <T> HashMap<String, T> getKeyValue(String jsonObject) {
        if (!jsonObject.startsWith("\"") && !jsonObject.endsWith("\"")) {
            throw new InvalidFormatException("Must be key value");
        }

        HashMap<String, T> keyValueMap = new HashMap<>();

        String[] splittedObject = jsonObject.split(",");
        
        for (String keyValue : splittedObject) {
            String[] keyValuesplitted = keyValue.split(":");
            String key = removeDoubleQuote(keyValuesplitted[0]).trim();
            T value = (T) removeDoubleQuote(keyValuesplitted[1]).trim();

            keyValueMap.put(key, value);
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

    /**
     * Transform a pair of key / value, and value is an array to a pair of json key / value  
     * @param key 
     * @param arrayValue an array in string 
     * @return the json key value like: '"exemple": [{"exemple1: exemple1", "exemple2": "exemple2"}]'
     */
    private static String toJsonKeyValueArray(String key, String arrayValue) {
        return "\"" + key + "\"" + ": " + arrayValue;
    }
    
    /**
     * Transform a pair of key / value string to a pair of json key / value
     * @param key
     * @param value
     * @return the json key value like: '"exemple": "exemple2"'
     */
    private static String toJsonKeyValueString(String key, String value) {
        return "\"" + key + "\"" + ": " + "\"" + value + "\"";
    }
    
    private static String joinListToJsonObject(List<String> listString) {
        return "{ " + String.join(", ", listString) + " }";
    }

    private static String joinListToJsonArray(List<String> listToJoin) {
        return "[" + String.join(", " + System.lineSeparator(), listToJoin) + "]";
    }
}
