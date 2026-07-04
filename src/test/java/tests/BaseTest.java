package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.testng.annotations.*;
import pages.*;

import java.time.Duration;

/**
 * Базовый тестовый класс.
 * Настраивает Selenium WebDriver и инициализирует страницы
 * перед выполнением тестов. Закрывает браузер после завершения тестов.
 */
public class BaseTest {

    WebDriver driver;
    LoginPage loginPage;
    ProductsPage productsPage;

    /**
     * Настраивает драйвер и инициализирует страницы перед каждым тестом.
     * Запускает Chrome в headless режиме с максимальным размером окна.
     */
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

    /**
     * Закрывает браузер после выполнения каждого теста.
     */
    @AfterMethod
    public void close() {
        driver.quit();
    }
}
