package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.testng.annotations.*;
import pages.*;

import java.time.Duration;

public class BaseTest {
    static final String USERNAME_VALID = "standard_user";
    static final String PASSWORD_VALID = "secret_sauce";

    WebDriver driver;
    LoginPage loginPage;
    ProductsPage productsPage;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("guest");
        options.addArguments("headless");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(7));

        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
    }

    @AfterMethod
    public void close() {
        driver.quit();
    }
}
