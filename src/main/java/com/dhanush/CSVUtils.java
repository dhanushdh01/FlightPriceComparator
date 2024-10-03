package com.dhanush;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVUtils {
    public static void saveToCSV(List<String> flightDetails, String fileName) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            // Write header
            String[] header = {"Operator", "Flight Number", "Price"};
            writer.writeNext(header);

            // Write flight details
            for (String details : flightDetails) {
                String[] data = details.split(", ");
                writer.writeNext(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}