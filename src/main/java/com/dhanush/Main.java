package com.dhanush;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        PaytmFlightScraper scraper = new PaytmFlightScraper();
        try {
            List<String> flightDetails = scraper.scrapeFlightDetails();
            System.out.println("Flight search results:");
            for (String details : flightDetails) {
                System.out.println(details);
            }
            CSVUtils.saveToCSV(flightDetails, "flight_details.csv");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ClearTripFlightScraper clearTripFlightScraper = new ClearTripFlightScraper();
        try {
            List<String> flightDetails = clearTripFlightScraper.scrapeFlightDetails();
            System.out.println("Flight search results:");
            for (String details : flightDetails) {
                System.out.println(details);
            }
            CSVUtils.saveToCSV(flightDetails, "clearTripflight_details.csv");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}