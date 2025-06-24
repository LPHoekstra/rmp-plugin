package com.rmp.repository;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import com.rmp.model.TestObject;
import com.rmp.model.TestObject2;
import com.rmp.utils.FileHandler;
import com.rmp.utils.Json;

public class TestObjectRepository implements Repository<TestObject> {
    
    @Override
    public boolean saveAll() {
        List<TestObject> testObjects = new ArrayList<TestObject>();

        List<TestObject2> gameList = new ArrayList<TestObject2>();
        gameList.add(new TestObject2("-50", "60", "10"));
        gameList.add(new TestObject2("-100", "70", "120"));

        testObjects.add(new TestObject("tom", "Hoekstra", "23", gameList));
        testObjects.add(new TestObject("Arthur", "Jacquemin", "25", gameList));

        new FileHandler("waypoint.json").writeFile(Json.createJsonFromList(testObjects));

        return true;
    }

    @Override
    public List<TestObject> findAll() {
        String fileData = new FileHandler("waypoint.json").getFileData();

        List<TestObject> list = Json.readJsonToList(TestObject.class.getName(), fileData);

        Bukkit.getLogger().info(list.toString());

        return list;
    }
}
