package com.rmp.repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.rmp.model.PlayerWaypoint;

// TODO on server enable get the data in file and init the list WaypointManager
public class WaypointRepository {
    private final String FILENAME = "waypoint.json";
    private Logger logger = null;

    public WaypointRepository(Logger logger) {
        this.logger = logger;
        createFile();
    }

    public void writeFile(List<PlayerWaypoint> list) {
        try {
            FileWriter fileWriter = new FileWriter(FILENAME);
                
            String stringToWrite = Json.createJsonFromString(list);

            fileWriter.write(stringToWrite);
            
            fileWriter.close();
        } catch (IOException exception) {
            logger.log(Level.WARNING, "Failed to write in file " + FILENAME);
        }
    }

    // handle exception
    public String getFileData() {
        try {
            File file = new File(FILENAME);
            Scanner scanner = new Scanner(file);
            String fileData = "";
            
            while (scanner.hasNextLine()) {
                fileData = fileData + scanner.nextLine();
            }
            scanner.close();
            
            return fileData;
        } catch (FileNotFoundException exception) {
            throw new IllegalArgumentException(exception.getMessage());
        }
    }
    
    /**
     * 
     * @return true if the file as been created otherwise return false
     */
    private boolean createFile() {
        try {
            File file = new File(FILENAME);
            return file.createNewFile();
        } catch (IOException exception) {
            logger.log(Level.WARNING, "Failed to create waypoint.json file, waypoint will not be saved after a server restart!");
            return false;
        }
    }
}
