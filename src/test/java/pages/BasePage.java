package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Базовая страница для всех страниц приложения.
 * Содержит общую логику для взаимодействия с элементами страницы
 * и основные константы для XPath-выражений.
 */
public class BasePage {

    private static final String BASE_URL = "https://www.saucedemo.com/";
    
    /**
     * Паттерн XPath для поиска элементов по атрибуту data-test.
     */
    public static final String DATA_TEXT_PATTERN = "//*[@data-test='%s']";
    
    /**
     * Паттерн XPath для поиска иконок ошибок по атрибуту data-test.
     */
    public static final String DATA_ICON_PATTERN = "//div[input[@data-test='%s']]//*[@data-icon='times-circle']";
    
    /**
     * Паттерн XPath для поиска кнопок inventory items.
     */
    public static final String INVENTORY_ITEM_BUTTON_PATTERN =
            "//*[%s]//ancestor::div[@data-test='inventory-item']//child::button";

    WebDriver driver;
    WebDriverWait wait;

    /**
     * Конструктор базовой страницы.
     *
     * @param driver драйвер Selenium для взаимодействия с браузером
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    /**
     * Открывает главную страницу приложения.
     */
    public void open() {
        driver.get(BASE_URL);
    }
}
