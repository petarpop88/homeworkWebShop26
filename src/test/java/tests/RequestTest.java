package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckOutPage;
import pages.InventoryPage;
import pages.LoginPage;

public class RequestTest {

    private LoginPage loginPage;
    private WebDriver driver;
    private WebDriverWait driverWait;
    private InventoryPage inventoryPage;
    private CheckOutPage checkOutPage;
    private CartPage cartPage;

    //jos ostalo da za test
    // 5. Konfirmacija kupovine (provera totala, konfirmacione poruke)
    // 6. Logout i provera otvaranja https://www.saucedemo.com/cart.html bez login-a

    @BeforeClass
    public void beforeClass() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\HP\\Local\\webdriver/chromedriver.exe");
        driver = new ChromeDriver();
        //driver.get("https://www.saucedemo.com/");
        loginPage = new LoginPage(driver, driverWait);
        inventoryPage = new InventoryPage(driver, driverWait);
        checkOutPage = new CheckOutPage (driver,driverWait);
        cartPage = new CartPage(driver, driverWait);

    }

    @BeforeMethod
        public void beforeMethod (){
        driver.get("https://www.saucedemo.com/");
        driver.manage().deleteAllCookies();
        loginPage.login("standard_user", "secret_sauce");

    }

    @Test (priority = 1)
    public void testLoginPage() {
        String expectedResult = "PRODUCTS";
        WebElement actualResult = driver.findElement(By.xpath("//*[@id=\"header_container\"]/div[2]/span"));
        Assert.assertEquals(expectedResult, actualResult.getText());
    }

    @Test (priority = 2)
    public void testAddProductToCart (){
        inventoryPage.addProductToCart();
        WebElement cartIcon = driver.findElement(By.xpath("//*[@id=\"shopping_cart_container\"]/a"));
        Assert.assertTrue(cartIcon.isDisplayed());
    }

    @Test (priority = 3)
    public void testIsProductInCart () {
        WebElement cartIcon = driver.findElement(By.xpath("//*[@id=\"shopping_cart_container\"]/a"));
        cartIcon.click();

        String expectedResult = "Sauce Labs Onesie";
        WebElement actualResult = driver.findElement(By.xpath("//*[@id=\"item_2_title_link\"]/div"));
        Assert.assertEquals(expectedResult, actualResult.getText());

    }
    @Test (priority = 4)
    public void testCheckOut () {
        String expectedResult = "CHECKOUT: COMPLETE!";

        WebElement cartIcon = driver.findElement(By.xpath("//*[@id=\"shopping_cart_container\"]/a"));
        cartIcon.click();

        WebElement checkOut = driver.findElement(By.xpath("//*[@id=\"checkout\"]"));
        checkOut.click();

        checkOutPage.myInfo("Petar", "Popovic", "11400");

        WebElement finish = driver.findElement(By.id("finish"));
        finish.click();

        WebElement actualResult = driver.findElement(By.xpath("//*[@id=\"header_container\"]/div[2]/span"));

        Assert.assertEquals(expectedResult, actualResult.getText());

        }

    @AfterClass
            public void afterClass(){
        driver.quit();
    }

}