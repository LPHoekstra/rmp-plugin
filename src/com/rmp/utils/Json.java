package com.rmp.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rmp.exception.InvalidFormatException;
import com.rmp.exception.JsonException;

// TODO handle different type of value, actually only String and List type as been tested
// TODO need some refactoring and comment
public class Json {
    private static final String CLASS_KEY = "class_";

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

        fieldListString.add(getClassNameInKeyValue(className));
        
        return joinListToJsonObject(fieldListString);
    }

    
    //////////// method to read a json to an java object ////////////
    
    /**
     * Transform a json in a string format to an java object by passing the class name in param with the json.
     * @param className corresponding to the object in json 
     * @param jsonString
     * @return a list of object corresponding to the class passed in param
     */
    public static <T, O> List<O> readJsonToList(String jsonString) {
        try {
            if (!jsonString.startsWith("[")) {
                throw new InvalidFormatException("Data is not a json");
            }
            
            List<Map<String, T>> objectListMap = getListMapFromArrayOfObject(jsonString);
            
            List<O> instanceList = createListObject(objectListMap);
            
            return instanceList;
        } catch (Exception e) {
            throw new JsonException("Error during the reading of the json: " + e.getMessage(), e.getCause());
        }
    }

    private static <O extends Object, T> List<O> createListObject(List<Map<String, T>> objectListMap) {
        List<O> instanceList = new ArrayList<O>();

        for (Map<String, T> map : objectListMap) {
            // create an instance of the object
            O newInstance = createObject(map);
            
            instanceList.add(newInstance);
        }

        return instanceList;
    }

    /**
     * 
     * @param <O> is an instance of a java object
     * @param <T> is the value from the key / value map
     * @param <L> is the value for the param
     * @param object
     * @param objectClass
     * @return
     */
    private static <O extends Object, T, L> O createObject(Map<String, T> object) {
        List<L> paramList = new ArrayList<L>();
        List<Class<?>> paramTypeList = new ArrayList<Class<?>>();
        Class<?> classDefinition = getObjectClass(object);
        
        Field[] fields = classDefinition.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String keyFieldName = field.getName();
            T valueField = object.get(keyFieldName);
            
            // if an object is a list of object
            if (valueField instanceof List) {
                valueField.getClass();
                List<Map<String, T>> objectList = (List<Map<String, T>>) valueField;

                List<O> instanceList = createListObject(objectList);
                paramList.add((L) instanceList);
            }

            else {
                paramList.add((L) valueField);
            }

            paramTypeList.add(field.getType());
        }

        O newInstance = createInstance(paramList, paramTypeList, classDefinition);

        return newInstance;
    }

    /**
     * Get the class name of the object stored in the object with the key "class_"
     * @param <T>
     * @param object is the map from a json object
     * @return the class
     */
    private static <T> Class<?> getObjectClass(Map<String, T> object) {
        try {
            String className = (String) object.get(CLASS_KEY);
            Class<?> classDefinition = Class.forName(className);
            return classDefinition;
        } catch (ClassNotFoundException e) {
            throw new JsonException("Class not found in object: " + e.getMessage(), e.getCause());
        }
    }
    
    private static <L, O> O createInstance(List<L> paramList, List<Class<?>> paramTypeList, Class<?> classDefinition) {
        try {
            Class<?>[] paramTypes = paramTypeList.toArray(new Class<?>[0]);
            Object[] params = paramList.toArray();
            
            Constructor<?> constructor = classDefinition.getDeclaredConstructor(paramTypes);
            return (O) constructor.newInstance(params);
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
    private static <T> Map<String, T> getKeyValueFromJson(String jsonObject, Class<?> clazz) {
        // TODO verification of jsonObject
        Map<String, T> keyValueMap = new HashMap<>();
        // put the class name if map
        keyValueMap.put(CLASS_KEY, (T) clazz.getName());
        
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
        String[] arrayOfClass = array.split("\"" + CLASS_KEY + "\":");
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
    private static String getClassNameInKeyValue(String className) {
        return "\"" + CLASS_KEY + "\": " + "\"" + className + "\""; 
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
