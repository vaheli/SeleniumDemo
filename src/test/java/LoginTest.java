import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.time.Duration;

import static org.testng.Assert.*;

public class LoginTest {
    WebDriver driver;

    @BeforeMethod
    public void getBrowser() {
        driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/");
    }

    @AfterMethod
    public void closeBrowser() {
        driver.quit();
    }

    @Test
    public void checkLogin() {
        driver.findElement(By.cssSelector("#user-name")).sendKeys("standard_user");
        assertEquals(driver.findElement(By.cssSelector("#user-name")).getAttribute("value"), "standard_user");
    }

    @Test
    public void checkPassword() {
        driver.findElement(By.cssSelector("#password")).sendKeys("secret_sauce");
        assertEquals(driver.findElement(By.cssSelector("#password")).getAttribute("value"), "secret_sauce");

    }

    @Test
    public void checkLoginButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(0));
        driver.findElement(By.xpath("//*[@id = 'user-name']")).sendKeys("standard_user");
        driver.findElement(By.xpath("//*[@id = 'password']")).sendKeys("secret_sauce");
        assertEquals(driver.findElement(By.xpath("//*[@id = 'login-button']")).getAttribute("data-test"), "login-button");
        driver.findElement(By.cssSelector("#login-button")).click();
        wait.until(ExpectedConditions.urlContains("inventory.html"));
        assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/inventory.html");
    }
}
