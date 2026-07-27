package tests;

import header.NavigationPanel;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Step;
import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.*;
import org.testng.ITestContext;
import org.testng.annotations.*;
import pages.*;
import utils.TestListener;

import java.time.Duration;

@Listeners({AllureTestNg.class, TestListener.class})
public class BaseTest {
    public static final int TEST_REPEAT_COUNT = 2;
    WebDriver driver;
    NavigationPanel navigationPanel;
    LoginPage loginPage;
    ProductsPage productsPage;
    BasketPage basketPage;
    CheckoutPage checkoutPage;
    CheckoutOverviewPage checkoutOverviewPage;
    CheckoutCompletePage  checkoutCompletePage;

    @BeforeMethod
    @Parameters({"browser"})
    public void setUp(@Optional("chrome") String browser, ITestContext context) {
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("start-maximized");
            options.addArguments("guest");
            options.addArguments("headless");
            driver = new ChromeDriver(options);

        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxProfile profile = new FirefoxProfile();
            profile.setPreference("browser.startup.maximize", true);
            profile.setPreference("browser.privatebrowsing.autostart", true);
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setProfile(profile);
            firefoxOptions.addArguments("-headless");
            driver = new FirefoxDriver(firefoxOptions);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(7));
        context.setAttribute("driver", driver);

        navigationPanel = new NavigationPanel(driver);
        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
        basketPage = new BasketPage(driver);
        checkoutPage = new CheckoutPage(driver);
        checkoutOverviewPage = new CheckoutOverviewPage(driver);
        checkoutCompletePage = new CheckoutCompletePage(driver);
    }

    @Step("Закрываем браузер")
    @AfterMethod
    public void close() {
        driver.quit();
    }
}
