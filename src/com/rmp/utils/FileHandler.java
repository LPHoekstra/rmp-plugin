package com.rmp.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import org.bukkit.Bukkit;

public class FileHandler {
    private final String FILENAME;

    public FileHandler(String fileName) {
        this.FILENAME = fileName;
    }

    /**
     * Write in a file the string passed in param.
     * @param stringToWrite
     * @return true if is successfully write in file, otherwise return false.
     */
    public boolean writeFile(String stringToWrite) {
        try {
            FileWriter fileWriter = new FileWriter(FILENAME);

            fileWriter.write(stringToWrite);
            
            fileWriter.close();
            return true;
        } catch (IOException exception) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to write in file: " + FILENAME);
            return false;
        }
    }

    /**
     * Get the data written in the file.
     * @return the data from the file in string.
     */
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
