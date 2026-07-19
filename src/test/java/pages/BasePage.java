package pages;

import header.NavigationPanel;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.PropertyReader;

import java.time.Duration;

public class BasePage {
    public static final String BASE_URL = PropertyReader.getProperty("saucedemo.url");
    public static final String DATA_TEXT_PATTERN = "//*[@data-test='%s']";
    public static final String DATA_ICON_PATTERN = "//div[input[@data-test='%s']]//*[@data-icon='circle-xmark']";
    public static final String INVENTORY_ITEM_BUTTON_PATTERN =
            "//*[%s]//ancestor::div[@data-test='inventory-item']//child::button";

    WebDriver driver;
    WebDriverWait wait;
    public NavigationPanel navigationPanel;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.navigationPanel = new NavigationPanel(driver);
    }

    @Step("Открываем страницу авторизации")
    public void open() {
        driver.get(BASE_URL);
    }

    @Step("Открываем страницу BASE_URL + {url}")
    public void open(String url) {
        driver.get(BASE_URL + url);
    }

    @Step("Получаем элемент по локатору")
    public WebElement getWebElement(By locator) {
        return driver.findElement(locator);
    }


}
