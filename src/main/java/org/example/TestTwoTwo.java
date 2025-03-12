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

public class TestTwoTwo {

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
    public void selectElementsTab() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'some-overlay-class')]")));
        WebElement widgetsTab = driver.findElement(By.xpath("//h5[text()='Elements']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", widgetsTab);
        System.out.println("Selected Tab: " + widgetsTab.getText());
        widgetsTab.click();
    }

    @Test(dependsOnMethods = "selectElementsTab")
    public void chooseWebTables(){
        WebElement progressBarTab = driver.findElement(By.xpath("//span[text()='Web Tables']"));
        progressBarTab.click();
    }

    @Test(dependsOnMethods = "chooseWebTables")
    public void addElements(){
        WebElement pagenumber = driver.findElement(By.xpath("/descendant::span[@class='-totalPages']"));
        while(!pagenumber.getText().equals("2")) {
            WebElement addbutton = driver.findElement(By.id("addNewRecordButton"));
            addbutton.click();

            WebElement firstname = driver.findElement(By.id("firstName"));
            firstname.sendKeys("a");
            WebElement lastname = driver.findElement(By.id("lastName"));
            lastname.sendKeys("a");
            WebElement email = driver.findElement(By.id("userEmail"));
            email.sendKeys("a@a.com");
            WebElement age = driver.findElement(By.id("age"));
            age.sendKeys("1");
            WebElement salary = driver.findElement(By.id("salary"));
            salary.sendKeys("1");
            WebElement department = driver.findElement(By.id("department"));
            department.sendKeys("a");

            WebElement submitbutton = driver.findElement(By.id("submit"));
            submitbutton.click();
        }
    }

    @Test (dependsOnMethods = "addElements")
    public void clickNext(){
        WebElement next = driver.findElement(By.xpath("/descendant::button[text()='Next']"));
        next.click();
    }

    @Test (dependsOnMethods = "clickNext")
    public void clickDelete(){
        WebElement deletes = driver.findElement(By.xpath("/descendant::span[@title='Delete']"));
        deletes.click();
    }

    @Test (dependsOnMethods = "clickDelete")
    public void backToFirstPage(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(new By.ByXPath("/descendant::div[@class='ajax-loading-block-window']")));

        WebElement page = driver.findElement(By.xpath("/descendant::span[@class='-totalPages']"));
        String pageText = page.getText().trim();

        Assert.assertEquals(pageText, "1", "The page did not reload");
        System.out.println("Page number: " + pageText);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
