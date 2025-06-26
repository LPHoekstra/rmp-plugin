package com.rmp.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;

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
    private static <T extends Object> String convertObjectToString(T objectToConvert) throws IllegalAccessException {
        List<String> fieldListString = new ArrayList<String>();
        Class<?> clazz = objectToConvert.getClass();
        String className = clazz.getName();
        Field[] fields = clazz.getDeclaredFields();
        
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

        fieldListString.add(classNameInKeyValue(className));
        
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
            List<Map<String, String>> objectListInMap = getListMapFromArrayOfObject(jsonString);
            Bukkit.getLogger().info(objectListInMap.toString());
            
            // TODO must handle if a value is an array
            // check if the value is a string or a list
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
     * Create a list of map from an array of object in json in string.
     * @param <T> is a list or a string
     * @param arrayOfObject
     * @return
     */
    private static <T> List<Map<String, T>> getListMapFromArrayOfObject(String arrayOfObject) {
        // TODO verification of arrayOfObject
        try {
            List<Map<String, T>> objectList = new ArrayList<Map<String, T>>();
            String className = getClassNameInArray(arrayOfObject);
            Class<?> clazz = Class.forName(className);
            
            // add double quote in class name for extra verification
            String[] objects = arrayOfObject.split(addDoubleQuote(className));
            
            // remove the last element of the array " }]". another solution ?
            if (objects[objects.length - 1].startsWith(" }]")) {
                objects = Arrays.copyOf(objects, objects.length - 1);
            }
            
            // for each object, get the key value and put it in the objectList
            for (String object : objects) {               
                Map<String, T> objectInKeyValue = getKeyValueFromJson(object.trim(), clazz);
                objectList.add(objectInKeyValue);
            }
            
            return objectList;
        } catch (ClassNotFoundException e)  {
            throw new JsonException("Class not found in array: " + e.getMessage(), e.getCause());
        }
    }
    
    /**
     * Get a HashMap of key value from a json object
     * @param <T> is a list or a string
     * @param jsonObject
     * @param clazz
     * @return
     */
    private static <T> HashMap<String, T> getKeyValueFromJson(String jsonObject, Class<?> clazz) {
        // TODO verification of jsonObject
        HashMap<String, T> keyValueMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        
        for (Field field : fields) {
            String fieldName = field.getName();
            String stringAfterSelectedKey = jsonObject.split(addDoubleQuote(fieldName))[1];
            
            // if is a string
            if (stringAfterSelectedKey.startsWith(": \"")) {
                String value = stringAfterSelectedKey.split(",")[0];
                T cleanValue = (T) cleanValue(value);
                
                keyValueMap.put(fieldName, cleanValue);
            }
            // if is an array
            else if(stringAfterSelectedKey.startsWith(": [")) {
                T arrayFromValue = (T) getListMapFromArrayOfObject(stringAfterSelectedKey);
                keyValueMap.put(fieldName, arrayFromValue);
            }
        }
        
        return keyValueMap;
    }

    /**
     * Remove double quote to get only the value string
     * @param value
     * @return
     */
    private static String cleanValue(String value) {
        return removeDoubleDot(removeDoubleQuote(value)).trim();
    }

    /**
     * Get the class name of this array passed in param
     * @param array
     * @return
     */
    private static String getClassNameInArray(String array) {
        String[] arrayOfClass = array.split("\"class_\":");
        String classNameOfThisArray = arrayOfClass[arrayOfClass.length - 1];
        classNameOfThisArray = classNameOfThisArray.split("\"")[1];
        return removeDoubleQuote(classNameOfThisArray).trim();
    }

    
    private static String removeDoubleDot(String string) {
        return string.replace(":", "");
    } 
    
    private static String addDoubleQuote(String string) {
        return "\"" + string + "\"";
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
     * 
     * @param className in string
     * @return ""class_": "exemple.class.name""
     */
    private static String classNameInKeyValue(String className) {
        return "\"class_\": " + "\"" + className + "\""; 
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
