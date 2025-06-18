package com.rmp.waypointRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.rmp.model.PlayerWaypoints;

// TODO on server enable get the data in file and init the list WaypointManager
// TODO on server disable save the list in WaypointManager
public class WaypointRepository {
    private final String FILENAME = "waypoint.json";
    private Logger logger = null;

    public WaypointRepository(Logger logger) {
        this.logger = logger;
        createFile();
    }

    public void writeFile(List<PlayerWaypoints> list) {
        try {
            FileWriter fileWriter = new FileWriter(FILENAME);
                
            String stringToWrite = Json.createJsonFromString(list);

            fileWriter.write(stringToWrite);
            
            fileWriter.close();
        } catch (IOException exception) {
            logger.log(Level.WARNING, "Failed to write in file " + FILENAME);
        }
    }

    // private void getFileData() {
    //     Scanner scanner = new Scanner(FILENAME);
    //     while (scanner.hasNextLine()) {
    //         scanner.nextLine();
    //     }
    //     scanner.close();
    // }
    
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
