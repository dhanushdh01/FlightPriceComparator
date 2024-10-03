package com.dhanush;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ClearTripFlightScraper {
    private WebDriver driver;
    private WebDriverWait wait;

    public ClearTripFlightScraper() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        this.driver = new ChromeDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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
            WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='pb-1 px-1 flex flex-middle nmx-1']")));
            closeButton.click();

            // Click on the 'From' input field
            WebElement fromSpanElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@class='w-100p fs-16 fw-500 c-neutral-900']")));
            fromSpanElement.click();

            // Enter the source city character by character
            WebElement fromInputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@class='w-100p fs-16 fw-500 c-neutral-900']")));
            String city = "Bangalore";
            for (char c : city.toCharArray()) {
                fromInputField.sendKeys(String.valueOf(c));
                Thread.sleep(1000);  // Small delay to simulate typing
            }
            Thread.sleep(2000);  // Let the suggestions load

            WebElement firstSuggestion = wait.until(ExpectedConditions.elementToBeClickable(By
                    .xpath(".//p[contains(@class,'tt-ellipsis o-hidden ws-nowrap fs-14 fw-500') and contains(text(), 'Bangalore, IN - Kempegowda International Airport (BLR)')]")));
            firstSuggestion.click();
            Thread.sleep(1000);  // Ensure the selection is made

            // Wait for a moment to ensure the UI is stable
            Thread.sleep(1000);

            // Click on the 'To' input fieldBy
            WebElement toSpanElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@class='w-100p fs-16 fw-500 c-neutral-900']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", toSpanElement);

            // Enter the destination city character by character
            WebElement toInputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@class='w-100p fs-16 fw-500 c-neutral-900']")));
            String toCity = "Delhi, India";
            for (char c : toCity.toCharArray()) {
                toInputField.sendKeys(String.valueOf(c));
                Thread.sleep(1000);  // Small delay to simulate typing
            }
            Thread.sleep(2000);  // Let the suggestions load

            WebElement secondSuggestion = wait.until(ExpectedConditions.elementToBeClickable(By
                    .xpath(".//p[contains(@class,'tt-ellipsis o-hidden ws-nowrap fs-14 fw-500') and contains(text(), 'New Delhi, IN - Indira Gandhi Airport (DEL)')]")));
            secondSuggestion.click();
            Thread.sleep(1000);  // Ensure the selection is made

            // Click the search button to search for flights using JavaScript
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='sc-dAlyuH cIApyz']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

            // Wait for the results to load
            System.out.println("Searching for flights...");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'ms-grid-row-2 ow-tuple-container__details-a  nmt-2')]")));

            // Click the "Non-Stop" filter checkbox
            WebElement nonStopCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(@class, 'checkbox__mark bs-border bc-neutral-500 bw-1 ba')]")));
            if (!nonStopCheckbox.isSelected()) {
                nonStopCheckbox.click();
            }

            // Wait for the results to update
            Thread.sleep(2000);

            WebElement flightsListContainer = driver.findElement(By.xpath(".//div[contains(@class, 'ba bc-neutral-100 br-4 px-4 tp-box-shadow td-200 p-relative flex flex-column hover:elevation-3 c-pointer px-6')]"));


// Locate the elements containing the flight details
            List<WebElement> flightElements = driver.findElements(By.xpath("//div[contains(@class, 'ms-grid-row-2 ow-tuple-container__details-a  nmt-2')]"));
            System.out.println("Number of flight elements found: " + flightElements.size());

            for (WebElement flightElement : flightElements) {
                try {
                    String operator = flightElement.findElement(By.xpath(".//p[contains(@class, 'fw-500 fs-2 c-neutral-900')]")).getText();

                    WebElement detailsButton = flightElement.findElement(By.className("tippy-tooltip-27"));
                    detailsButton.click();
                    Thread.sleep(1000);  // Wait for the details to be revealed

                    String flightNumber = flightElement.findElement(By.xpath(".//p[contains( @class,'c-neutral-400 fs-2')]")).getText();
                    String price = flightElement.findElement(By.xpath(".//div[contains(@class,'flex flex-column flex-center flex-right p-relative')]")).getText();
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