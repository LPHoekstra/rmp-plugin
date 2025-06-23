package com.rmp.repository;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import com.rmp.model.TestObject;
import com.rmp.utils.FileHandler;
import com.rmp.utils.Json;

public class TestObjectRepository implements Repository<TestObject> {
    
    public boolean saveAll() {
        List<TestObject> testObjects = new ArrayList<TestObject>();

        testObjects.add(new TestObject("tom", "Hoekstra", "23", "Bénouville"));
        testObjects.add(new TestObject("Arthur", "Jacquemin", "25", "Poitiers"));

        new FileHandler("waypoint.json").writeFile(Json.createJsonFromList(testObjects));

        return true;
    }

    public List<TestObject> findAll() {
        String fileData = new FileHandler("waypoint.json").getFileData();

        List<TestObject> list = Json.readJsonToList(TestObject.class.getName(), fileData);

        Bukkit.getLogger().info(list.toString());

        return list;
    }
}
