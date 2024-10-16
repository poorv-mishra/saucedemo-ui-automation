package com.saucedemo.tests;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SauceDemoTest {
    private WebDriver driver;

    @BeforeClass
    public void setUp() {
        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        // Implicit wait
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(10));
    }

    @Test
    public void testAddProductToCartAndCheckout() {
        // Open the website
        driver.get("https://www.saucedemo.com/");

        // Login
        driver.findElement(By.id("user-name")).sendKeys("problem_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // Verify login success by checking presence of products
        Assert.assertTrue(driver.findElement(By.className("inventory_list")).isDisplayed(), "Login failed or products not displayed");

        // Select "Sauce Labs Fleece Jacket"
        driver.findElement(By.xpath("//div[text()='Sauce Labs Fleece Jacket']")).click();

        // Add to cart
        driver.findElement(By.id("add-to-cart-sauce-labs-fleece-jacket")).click();

        // Go to cart
        driver.findElement(By.className("shopping_cart_link")).click();

        // Verify product in cart
        WebElement cartItem = driver.findElement(By.className("cart_item"));
        Assert.assertTrue(cartItem.isDisplayed(), "Cart is empty or item not found");

        // Checkout
        driver.findElement(By.id("checkout")).click();

        // Enter checkout information
        driver.findElement(By.id("first-name")).sendKeys("Poorv");
        driver.findElement(By.id("last-name")).sendKeys("Mishra");
        driver.findElement(By.id("postal-code")).sendKeys("Poorv@123");
        driver.findElement(By.id("continue")).click();

        // Print Payment Information and Total Price
        String paymentInfo = driver.findElement(By.xpath("//div[@class='summary_info_label summary_payment_info_label']")).getText();
        String totalPrice = driver.findElement(By.className("summary_total_label")).getText();
        System.out.println("Payment Info: " + paymentInfo);
        System.out.println("Total Price: " + totalPrice);

        // Finish checkout
        driver.findElement(By.id("finish")).click();

        // Verify order completion
        String confirmation = driver.findElement(By.className("complete-header")).getText();
        Assert.assertEquals(confirmation, "THANK YOU FOR YOUR ORDER", "Order not completed successfully");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
