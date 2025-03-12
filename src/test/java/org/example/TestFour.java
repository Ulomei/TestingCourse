package org.example;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.UUID;


//7) create a new review, rate a review with 'Was this review helpful?' - create 2 accounts, verify counter changes.
public class TestFour {
    WebDriver driver;
    private static final String title = "A very long long long long title" + UUID.randomUUID().toString().substring(0, 6);
    private static final String email1 = "test" + UUID.randomUUID().toString().substring(0, 6) + "@gmail.com";
    private static final String password1 = "Test123";

    private static final String email2 = "test" + UUID.randomUUID().toString().substring(0, 6) + "@gmail.com";
    private static final String password2 = "Test123";


    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://demowebshop.tricentis.com/");
    }

    public void registerUser(String email, String password) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.findElement(By.xpath("/descendant::a[@href='/login']")).click();
        driver.findElement(By.xpath("/descendant::input[@class = 'button-1 register-button']")).click();

        driver.findElement(By.id("gender-male")).click();
        driver.findElement(By.id("FirstName")).sendKeys("a");
        driver.findElement(By.id("LastName")).sendKeys("a");
        driver.findElement(By.id("Email")).sendKeys(email);
        driver.findElement(By.id("Password")).sendKeys(password);
        driver.findElement(By.id("ConfirmPassword")).sendKeys(password);
        driver.findElement(By.id("register-button")).click();

        WebElement continueBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/descendant::input[@class = 'button-1 register-continue-button']")));
        if (continueBtn.isDisplayed())
            System.out.println("Registration is done!");
        Assert.assertTrue(continueBtn.isDisplayed(), "Registration failed!");
        continueBtn.click();

    }

    public void accessReviews(){
        WebElement giftCardsLink = driver.findElement(By.xpath("//a[@href='/gift-cards']"));
        giftCardsLink.click();

        WebElement giftCard = driver.findElement(By.xpath("//a[@href='/5-virtual-gift-card']"));
        giftCard.click();

        WebElement addReview = driver.findElement(By.xpath("//a[@href='/productreviews/1']"));
        addReview.click();
    }

    @Test
    public void reviewTest() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        registerUser(email1, password1);
        accessReviews();

        driver.findElement(By.id("AddProductReview_Title")).sendKeys(title);
        driver.findElement(By.id("AddProductReview_ReviewText")).sendKeys("A lot of text :))");
        driver.findElement(By.name("add-review")).click();
        driver.findElement(By.xpath("//a[@href='/logout']")).click();

        registerUser(email2, password2);
        accessReviews();

        String xpathReviewTitle = String.format("//div[@class='review-title']/strong[text()='%s']", title);
        WebElement review = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathReviewTitle)));
        System.out.println("Before checking review");
        System.out.println("Title: " + title);

        try {
            String xpathReviewTitle2 = String.format("//strong[text()='%s']" +
                    "    /ancestor::div[@class='product-review-item']" +
                    "    /descendant::div[@class='product-review-helpfulness']", title);

            WebElement helpfulnessSection = review.findElement(By.xpath(xpathReviewTitle2));

            // Locate the "Yes" vote button and vote, but only within the context of this review
            WebElement yesVoteButton = helpfulnessSection.findElement(By.xpath(".//span[contains(@id, 'vote-yes')]"));
            WebElement yesVotesElement = helpfulnessSection.findElement(By.xpath(".//span[contains(@id, 'helpfulness-vote-yes')]"));

            int initialYesVotes = Integer.parseInt(yesVotesElement.getText().trim());
            System.out.println("Initial Yes Votes: " + initialYesVotes);

            yesVoteButton.click();

            wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(yesVotesElement, String.valueOf(initialYesVotes))));
            int updatedYesVotes = Integer.parseInt(yesVotesElement.getText().trim());
            System.out.println("Updated Yes Votes: " + updatedYesVotes);

            if (updatedYesVotes == initialYesVotes + 1) {
                System.out.println("Vote successfully counted!");
            } else {
                System.out.println("Vote was not counted. Please check.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
