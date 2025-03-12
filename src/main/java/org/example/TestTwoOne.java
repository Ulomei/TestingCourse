package org.example;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class TestTwoOne {

    WebDriver driver;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void openWebsite() {
        driver.get("https://demoqa.com/");
        Assert.assertTrue(driver.getTitle().contains("DEMOQA"));

    }

    @Test(dependsOnMethods = "openWebsite")
    public void closeCookieConsent() {
        try {
            WebElement closeAd = driver.findElement(By.xpath("//iframe[contains(@id, 'google_ads_iframe')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='none';", closeAd);
            System.out.println("Ad closed");
        } catch (Exception e) {
            System.out.println("No ad found.");
        }
    }

    @Test(dependsOnMethods = "closeCookieConsent")
    public void selectWidgetsTab() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'some-overlay-class')]")));
        WebElement widgetsTab = driver.findElement(By.xpath("//h5[text()='Widgets']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", widgetsTab);
        System.out.println("Selected Tab: " + widgetsTab.getText());
        widgetsTab.click();
    }

    @Test(dependsOnMethods = "selectWidgetsTab")
    public void chooseProgramBar(){
        WebElement progressBarTab = driver.findElement(By.xpath("//span[text()='Progress Bar']"));
        progressBarTab.click();
    }

    @Test(dependsOnMethods = "chooseProgramBar")
    public void clickStartButton() {

        WebElement startButton = driver.findElement(By.id("startStopButton"));
        startButton.click();
    }

    @Test(dependsOnMethods = "clickStartButton")
    public void waitForProgressBarToReach100() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement progressBar = driver.findElement(By.xpath("//div[@role='progressbar']"));
        wait.until(ExpectedConditions.attributeContains(progressBar, "aria-valuenow", "100"));

        System.out.println("Progress Bar reached 100%");
        var waitForButton = new WebDriverWait(driver, Duration.ofSeconds(10));
        waitForButton.until(ExpectedConditions.invisibilityOfElementLocated(new By.ByXPath("/descendant::div[@class='ajax-loading-block-window']")));
        WebElement resetButton = driver.findElement(By.id("resetButton"));
        resetButton.click();
    }

    @Test(dependsOnMethods = "waitForProgressBarToReach100")
    public void ensureProgressBarIsEmpty() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait for the progress bar to be reset to 0%
        WebElement progressBar = driver.findElement(By.xpath("//div[@role='progressbar']"));
        wait.until(ExpectedConditions.attributeContains(progressBar, "aria-valuenow", "0"));

        // Assert that the progress bar is empty (0%)
        String progressBarValue = progressBar.getAttribute("aria-valuenow");
        Assert.assertEquals(progressBarValue, "0", "The progress bar is not empty (0%) after reset.");
        System.out.println("Progress bar is empty (0%) after reset.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
