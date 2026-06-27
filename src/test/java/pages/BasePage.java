package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    private static final String BASE_URL = "https://www.saucedemo.com/";
    public static final String TEXT_PATTERN = "//*[text()='%s']";
    public static final String DATA_TEXT_PATTERN = "//*[@data-test='%s']";
    public static final String DATA_ICON_PATTERN = "//div[input[@data-test='%s']]//*[@data-icon='times-circle']";
    public static final String INVENTORY_ITEM_BUTTON_XPATH =
            "//*[text()='%s']//ancestor::div[@data-test='inventory-item']//child::button[text()='%s']";

    WebDriver driver;
    WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void open() {
        driver.get(BASE_URL);
    }
}
