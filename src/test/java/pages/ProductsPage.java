package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

public class ProductsPage extends BasePage {
    private static final By INVENTORY_ITEM_BUTTON =
            By.xpath(INVENTORY_ITEM_BUTTON_PATTERN.formatted("contains(@class, 'btn_inventory')"));

    public ProductsPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @Step("Добавляем товар: {itemName}")
    public void addItemToCart(String itemName) {
        String xpath = getItemByName(itemName);
        driver.findElement(By.xpath(xpath)).click();

    }

    @Step("Добавляем товары по количеству")
    public void addItemToCarts(Integer count) {
        List<WebElement> addToCartButtons = driver.findElements(INVENTORY_ITEM_BUTTON);

        for (int i = 0; i < count && i < addToCartButtons.size(); i++) {
            addToCartButtons.get(i).click();
        }
    }

    @Step("Добавляем товары по списку")
    public ProductsPage addItemsToCarts(List<String> items) {

        for (String itemName : items) {
            String xpath = getItemByName(itemName);
            driver.findElement(By.xpath(xpath)).click();
        }

        return this;
    }

    @Step("Удаляем товар из корзины")
    public String removeItemFromCart(String itemName) {
        String xpath = getItemByName(itemName);
        return driver.findElement(By.xpath(xpath)).getText();
    }

    @Step("Получаем локатор товара по названию товара")
    private String getItemByName(String itemName) {
        String condition = "text()='" + itemName + "'";
        return INVENTORY_ITEM_BUTTON_PATTERN.formatted(condition);
    }
}
