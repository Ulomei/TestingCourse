package org.example;

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
import java.util.List;

public class TestOne {

    WebDriver driver;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void openWebsite() {
        driver.get("https://demowebshop.tricentis.com/");
        Assert.assertTrue(driver.getTitle().contains("Demo Web Shop"));
    }

    @Test(dependsOnMethods = "openWebsite")
    public void clickOnGiftCards() {
        WebElement giftCardsLink = driver.findElement(By.xpath("//a[@href='/gift-cards']"));
        giftCardsLink.click();
        Assert.assertTrue(driver.getTitle().contains("Gift Cards"));
    }

    @Test(dependsOnMethods = "clickOnGiftCards")
    public void selectProduct() {
        WebElement selectGC = driver.findElement(new By.ByXPath("/descendant::div[@class='item-box'][descendant::span[contains(@class,'price')][text()>'99']]/descendant::a[contains(@href,'physical-gift-card')]"));
        selectGC.click();
        Assert.assertTrue(driver.getTitle().contains("Physical Gift Card"));
    }

    @Test(dependsOnMethods = "selectProduct")
    public void fillInRecipientAndYourName() {
        WebElement recipientNameField = driver.findElement(By.id("giftcard_4_RecipientName"));
        recipientNameField.sendKeys("Vardenis");

        WebElement yourNameField = driver.findElement(By.id("giftcard_4_SenderName"));
        yourNameField.sendKeys("Pavardenis");
    }

    @Test(dependsOnMethods = "fillInRecipientAndYourName")
    public void enterQuantity() {
        WebElement qtyField = driver.findElement(By.id("addtocart_4_EnteredQuantity"));
        qtyField.clear();
        qtyField.sendKeys("5000");
    }

    @Test(dependsOnMethods = "enterQuantity")
    public void addToCart() {
        WebElement addToCartButton = driver.findElement(By.id("add-to-cart-button-4"));
        addToCartButton.click();
    }

    @Test(dependsOnMethods = "addToCart")
    public void addToWishList() {
        var wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(new By.ByXPath("/descendant::div[@class='ajax-loading-block-window']")));
        WebElement addToWishListButton = driver.findElement(By.id("add-to-wishlist-button-4"));
        addToWishListButton.click();
    }

    @Test(dependsOnMethods = "addToWishList")
    public void clickOnJewelry() {
        WebElement jewelryLink = driver.findElement(By.xpath("/descendant::div[@class='listbox']/descendant::a[@href='/jewelry']"));
        jewelryLink.click();
    }

    @Test(dependsOnMethods = "clickOnJewelry")
    public void createYourOwnJewelry() {
        WebElement createJewelryLink = driver.findElement(By.xpath("/descendant::a[@href='/create-it-yourself-jewelry']"));
        createJewelryLink.click();
    }

    @Test(dependsOnMethods = "createYourOwnJewelry")
    public void selectJewelryOptions() {
        WebElement materialDropdown = driver.findElement(By.id("product_attribute_71_9_15"));
        materialDropdown.click();

        WebElement lengthDropdown = driver.findElement(By.id("product_attribute_71_10_16"));
        lengthDropdown.sendKeys("80");

        WebElement pendantDropdown = driver.findElement(By.id("product_attribute_71_11_17_50"));
        pendantDropdown.click();
    }

    @Test(dependsOnMethods = "selectJewelryOptions")
    public void enterJewelryQuantity() {
        WebElement jewelryQtyField = driver.findElement(By.id("addtocart_71_EnteredQuantity"));
        jewelryQtyField.clear();
        jewelryQtyField.sendKeys("26");
    }

    @Test(dependsOnMethods = "enterJewelryQuantity")
    public void addJewelryToCart() {
        WebElement addJewelryToCartButton = driver.findElement(By.id("add-to-cart-button-71"));
        addJewelryToCartButton.click();
    }

    @Test(dependsOnMethods = "addJewelryToCart")
    public void addJewelryToWishList() {
        var wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(new By.ByXPath("/descendant::div[@class='ajax-loading-block-window']")));
        WebElement addJewelryToWishListButton = driver.findElement(By.id("add-to-wishlist-button-71"));
        addJewelryToWishListButton.click();
    }

    @Test(dependsOnMethods = "addJewelryToWishList")
    public void goToWishlist() {
        var wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(new By.ByXPath("/descendant::div[@class='ajax-loading-block-window']")));
        WebElement wishListLink = driver.findElement(By.className("ico-wishlist"));
        wishListLink.click();
    }

    @Test(dependsOnMethods = "goToWishlist")
    public void checkAddToCartCheckbox() {
        List<WebElement> checkboxes = driver.findElements(By.xpath("//input[@name='addtocart']"));

        // Click each checkbox
        for (WebElement checkbox : checkboxes) {
            if (!checkbox.isSelected()) { // Ensure we don't click an already selected checkbox
                checkbox.click();
            }
        }


        // Verify that both checkboxes are selected
        for (WebElement checkbox : checkboxes) {
           Assert.assertTrue(checkbox.isSelected(), "Checkbox should be selected.");
        }
    }

    @Test(dependsOnMethods = "checkAddToCartCheckbox")
    public void addAllToCart() {
        WebElement addToCartButton = driver.findElement(By.xpath("/descendant::input[@name='addtocartbutton']"));
        addToCartButton.click();
    }

    @Test(dependsOnMethods = "addAllToCart")
    public void verifySubTotal() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement subTotalElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("product-price")));
        String subTotalText = subTotalElement.getText();
        Assert.assertEquals(subTotalText, "1002600.00");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
