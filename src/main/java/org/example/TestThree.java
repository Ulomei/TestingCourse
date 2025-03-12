package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

public class TestThree {

    private static final String email = "test" + UUID.randomUUID().toString().substring(0, 6) + "@gmail.com";
    private static final String password = "Test123";
    private WebDriver driver;

    //User Registration
    @BeforeSuite
    public void registerUser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://demowebshop.tricentis.com/");

        driver.findElement(By.xpath("/descendant::a[@href='/login']")).click();
        driver.findElement(By.xpath("/descendant::input[@class = 'button-1 register-button']")).click();

        driver.findElement(By.id("gender-male")).click();
        driver.findElement(By.id("FirstName")).sendKeys("a");
        driver.findElement(By.id("LastName")).sendKeys("a");
        driver.findElement(By.id("Email")).sendKeys(email);
        driver.findElement(By.id("Password")).sendKeys(password);
        driver.findElement(By.id("ConfirmPassword")).sendKeys(password);
        driver.findElement(By.id("register-button")).click();

        WebElement continueBtn = driver.findElement(By.xpath("/descendant::input[@class = 'button-1 register-continue-button']"));
        if (continueBtn.isDisplayed())
            System.out.println("Registration is done!");
        Assert.assertTrue(continueBtn.isDisplayed(), "Registration failed!");
        continueBtn.click();

        driver.quit();
    }

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    public void login() {
        driver.get("https://demowebshop.tricentis.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement loginLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/descendant::a[@href='/login']")));
        loginLink.click();

        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Email")));
        emailField.sendKeys(email);

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Password")));
        passwordField.sendKeys(password);

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/descendant::input[@class='button-1 login-button']")));
        loginButton.click();
        //driver.findElement(By.xpath("/descendant::a[@href='/login']")).click();
        //driver.findElement(By.id("Email")).sendKeys(email);
        //driver.findElement(By.id("Password")).sendKeys(password);
        //driver.findElement(By.xpath("/descendant::input[@class='button-1 login-button']")).click();
    }

    public void addProductsToCart(String filePath) throws IOException {
        driver.findElement(By.xpath("/descendant::div[@class='listbox']/descendant::a[@href='/digital-downloads']")).click();
        var wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String productName;

        while ((productName = reader.readLine()) != null) {
            try {
                WebElement product = driver.findElement(By.xpath("/descendant::div[@class= 'item-box'][descendant::a[text()= '" + productName + "']]/descendant::input"));
                product.click();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(new By.ByXPath("/descendant::div[@class='ajax-loading-block-window']")));
                System.out.println("Product: "+productName);
            } catch (Exception e) {
                System.out.println("Product not found: "+productName);
            }
        }
        reader.close();
    }

    public void checkout() {
        driver.findElement(By.xpath("/descendant::a[@href= '/cart']/descendant::span[@class='cart-label']")).click();
        driver.findElement(By.id("termsofservice")).click();
        driver.findElement(By.id("checkout")).click();

        try{
            WebElement dropdowncountry = driver.findElement(By.id("BillingNewAddress_CountryId"));
            Select dropdown = new Select(dropdowncountry);
            dropdown.selectByContainsVisibleText("Lithuania");

            WebElement city = driver.findElement(By.id("BillingNewAddress_City"));
            city.sendKeys("Vilnius");

            WebElement address = driver.findElement(By.id("BillingNewAddress_Address1"));
            address.sendKeys("A");

            WebElement zip = driver.findElement(By.id("BillingNewAddress_ZipPostalCode"));
            zip.sendKeys("00000");

            WebElement phone = driver.findElement(By.id("BillingNewAddress_PhoneNumber"));
            phone.sendKeys("+370666666666");
        }
        catch (Exception e){
            System.out.println();
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement continueBtn = driver.findElement(By.xpath("/descendant::input[@title = 'Continue']"));
        wait.until(ExpectedConditions.elementToBeClickable(continueBtn));
        continueBtn.click();

        WebElement paymentmethod = driver.findElement(By.xpath("/descendant::input[@class='button-1 payment-method-next-step-button']"));
        wait.until(ExpectedConditions.elementToBeClickable(paymentmethod));
        paymentmethod.click();

        WebElement paymentinformation = driver.findElement(By.xpath("/descendant::input[@class='button-1 payment-info-next-step-button']"));
        wait.until(ExpectedConditions.elementToBeClickable(paymentinformation));
        paymentinformation.click();

        WebElement confirmorder = driver.findElement(By.xpath("/descendant::input[@class='button-1 confirm-order-next-step-button']"));
        wait.until(ExpectedConditions.elementToBeClickable(confirmorder));
        confirmorder.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/descendant::div[contains(@class, 'order-completed')]/descendant::a[contains(@href, '/orderdetails/')]")));

        WebElement success = driver.findElement(By.xpath("/descendant::div[@class='section order-completed']/descendant::div[@class='title']/descendant::strong"));
        Assert.assertTrue(success.getText().contains("Your order has been successfully processed!"),
                "Order placement failed!");
        System.out.println("It worked! "+success.getText());
    }

    @Test
    public void testOrderWithData1() throws IOException {
        login();
        addProductsToCart("/Users/gabri/Desktop/data1.txt");
        checkout();
    }

    @Test
    public void testOrderWithData2() throws IOException {
        login();
        addProductsToCart("/Users/gabri/Desktop/data2.txt");
        checkout();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

