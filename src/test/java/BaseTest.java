import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.testng.annotations.*;

public class BaseTest {
    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("headless");
        driver = new ChromeDriver(options);

        driver.get("https://www.saucedemo.com/");
    }

    @AfterMethod
    public void close() {
        driver.quit();
    }
}
