package com.dhanush;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class PaytmFlightScraper {
    private WebDriver driver;
    private WebDriverWait wait;

    public PaytmFlightScraper() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        this.driver = new ChromeDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public List<String> scrapeFlightDetails() throws InterruptedException {
        List<String> flightDetails = new ArrayList<>();

        try {
            // Open the flight booking page
            driver.get("https://tickets.paytm.com/flights");

            // Maximize the browser window
            driver.manage().window().maximize();

            // Print the title of the page
            System.out.println(driver.getTitle());

            // Click on the 'From' input field
            WebElement fromSpanElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("srcCode")));
            fromSpanElement.click();

            // Enter the source city character by character
            WebElement fromInputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='text']")));
            String city = "Bengaluru, Karnataka, India";
            for (char c : city.toCharArray()) {
                fromInputField.sendKeys(String.valueOf(c));
                Thread.sleep(100);  // Small delay to simulate typing
            }
            Thread.sleep(1000);  // Let the suggestions load

            WebElement firstSuggestion = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, '_2wpHf') and contains(text(), 'Bengaluru, Karnataka, India')]")));
            firstSuggestion.click();
            Thread.sleep(1000);  // Ensure the selection is made

            // Wait for a moment to ensure the UI is stable
            Thread.sleep(1000);

            // Click on the 'To' input field
            WebElement toSpanElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("destCode")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", toSpanElement);

            // Enter the destination city character by character
            WebElement toInputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='text']")));
            String toCity = "Delhi, India";
            for (char c : toCity.toCharArray()) {
                toInputField.sendKeys(String.valueOf(c));
                Thread.sleep(100);  // Small delay to simulate typing
            }
            Thread.sleep(1000);  // Let the suggestions load

            WebElement secondSuggestion = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, '_2wpHf') and contains(text(), 'Delhi, India')]")));
            secondSuggestion.click();
            Thread.sleep(1000);  // Ensure the selection is made

            // Click the search button to search for flights using JavaScript
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("flightSearch")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

            // Wait for the results to load
            System.out.println("Searching for flights...");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flightsList")));

            // Click the "Non-Stop" filter checkbox
            WebElement nonStopCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.id("nonStop")));
            if (!nonStopCheckbox.isSelected()) {
                nonStopCheckbox.click();
            }

            // Wait for the results to update
            Thread.sleep(2000);

            WebElement flightsListContainer = driver.findElement(By.id("flightsList"));

            // Locate the elements containing the flight details
            List<WebElement> flightElements = flightsListContainer.findElements(By.className("_1gMv6"));
            System.out.println("Number of flight elements found: " + flightElements.size());

            for (WebElement flightElement : flightElements) {
                try {
                    String operator = flightElement.findElement(By.className("_2cP56")).getText();

                    // Click the "Flight details" button to reveal hidden details
                    WebElement detailsButton = flightElement.findElement(By.className("_3U68I"));
                    detailsButton.click();
                    Thread.sleep(1000);  // Wait for the details to be revealed

                    // Extract the required information
                    String flightNumber = flightElement.findElement(By.xpath(".//span[contains(@id, 'inboundflightnumber')]")).getText();
                    String price = flightElement.findElement(By.xpath(".//div[contains(@class,'_2PJH4')]")).getText();
                    flightDetails.add("Operator: " + operator + ", Flight Number: " + flightNumber + ", Price: " + price);
                } catch (Exception e) {
                    System.out.println("Error extracting flight details: " + e.getMessage());
                }
            }
        } finally {
            driver.quit();
        }

        return flightDetails;
    }
}