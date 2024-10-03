package com.dhanush;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ClearTripFlightScraper {
    private WebDriver driver;
    private WebDriverWait wait;

    public ClearTripFlightScraper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Open a new tab
        driver.switchTo().newWindow(WindowType.TAB);
    }

    public List<String> scrapeFlightDetails() throws InterruptedException {
        List<String> flightDetails = new ArrayList<>();

        try {
            // Open the flight booking page
            driver.get("https://www.cleartrip.com/flights");

            // Maximize the browser window
            driver.manage().window().maximize();

            // Print the title of the page
            System.out.println(driver.getTitle());

            // Close the login popup
            WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='c-pointer c-neutral-900']")));
            closeButton.click();

            // Click on the 'From' input field
            WebElement fromSpanElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@class='w-100p fs-16 fw-500 c-neutral-900']")));
            fromSpanElement.click();

            // Enter the source city character by character
            WebElement fromInputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@class='w-100p fs-16 fw-500 c-neutral-900']")));
            String city = "Bangalore";
            for (char c : city.toCharArray()) {
                fromInputField.sendKeys(String.valueOf(c));
                Thread.sleep(100);  // Small delay to simulate typing
            }
            Thread.sleep(1000);  // Let the suggestions load

            WebElement firstSuggestion = wait.until(ExpectedConditions.elementToBeClickable(By
                    .xpath("//ul[@id='ui-id-1']//li[contains(text(), 'Bangalore, IN - Kempegowda International Airport (BLR)')]")));
            firstSuggestion.click();
            Thread.sleep(1000);  // Ensure the selection is made

            // Wait for a moment to ensure the UI is stable
            Thread.sleep(1000);

            // Click on the 'To' input field
            WebElement toSpanElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@class='w-100p fs-16 fw-500 c-neutral-900']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", toSpanElement);

            // Enter the destination city character by character
            WebElement toInputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@class='w-100p fs-16 fw-500 c-neutral-900']")));
            String toCity = "Delhi, India";
            for (char c : toCity.toCharArray()) {
                toInputField.sendKeys(String.valueOf(c));
                Thread.sleep(100);  // Small delay to simulate typing
            }
            Thread.sleep(1000);  // Let the suggestions load

            WebElement secondSuggestion = wait.until(ExpectedConditions.elementToBeClickable(By
                    .xpath("//ul[@id='ui-id-2']//li[contains(text(), 'New Delhi, IN - Indira Gandhi Airport (DEL)')]")));
            secondSuggestion.click();
            Thread.sleep(1000);  // Ensure the selection is made

            // Click the search button to search for flights using JavaScript
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='sc-aXZVg ibgoAF']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

            // Wait for the results to load
            System.out.println("Searching for flights...");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flightForm")));

            // Locate the elements containing the flight details
            List<WebElement> flightElements = driver.findElements(By.className("listItem"));
            System.out.println("Number of flight elements found: " + flightElements.size());

            for (WebElement flightElement : flightElements) {
                try {
                    String operator = flightElement.findElement(By.className("airlineName")).getText();
                    String flightNumber = flightElement.findElement(By.className("flightNumber")).getText();
                    String price = flightElement.findElement(By.className("price")).getText();
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