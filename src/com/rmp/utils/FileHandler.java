package com.rmp.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

public class FileHandler {
    private Logger logger = null;
    private final String FILENAME;

    public FileHandler(String fileName) {
        this.logger = Bukkit.getLogger();
        this.FILENAME = fileName;
    }

    public void writeFile(String stringToWrite) {
        try {
            FileWriter fileWriter = new FileWriter(FILENAME);

            fileWriter.write(stringToWrite);
            
            fileWriter.close();
        } catch (IOException exception) {
            logger.log(Level.WARNING, "Failed to write in file: " + FILENAME);
        }
    }

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
}
