package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

/**
 * Страница товаров (Products Page).
 * Содержит методы для взаимодействия с товарами,
 * проверки корзины и отображения элементов на странице товаров.
 */
public class ProductsPage extends BasePage {

    private static final By TITLE = By.xpath(DATA_TEXT_PATTERN.formatted("title"));
    private static final By SHOPPING_CART_BADGE = By.xpath(DATA_TEXT_PATTERN.formatted("shopping-cart-badge"));
    private static final By INVENTORY_ITEM_BUTTON =
            By.xpath(INVENTORY_ITEM_BUTTON_PATTERN.formatted("contains(@class, 'btn_inventory')"));

    /**
     * Конструктор страницы товаров.
     *
     * @param driver драйвер Selenium для взаимодействия с браузером
     */
    public ProductsPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    /**
     * Возвращает заголовок страницы товаров.
     *
     * @return текст заголовка
     */
    public String getTitle() {
        return driver.findElement(TITLE).getText();
    }

    /**
     * Возвращает количество товаров в корзине (значение на значке).
     *
     * @return количество товаров в корзине
     */
    public String getShoppingCount() {
        try {
            return getShoppingCartWait().getText();
        } catch (TimeoutException e) {
            return "0";
        }
    }

    /**
     * Добавляет товар в корзину по его названию.
     *
     * @param itemName название товара
     */
    public void addItemToCart(String itemName) {
        String xpath = getItemByName(itemName);
        driver.findElement(By.xpath(xpath)).click();
    }

    /**
     * Добавляет указанное количество товаров в корзину.
     *
     * @param count количество товаров для добавления
     */
    public void addItemToCarts(Integer count) {
        List<WebElement> addToCartButtons = driver.findElements(INVENTORY_ITEM_BUTTON);

        for (int i = 0; i < count && i < addToCartButtons.size(); i++) {
            addToCartButtons.get(i).click();
        }
    }

    /**
     * Удаляет товар из корзины по его названию.
     *
     * @param itemName название товара
     * @return текст кнопки после удаления (обычно "Remove")
     */
    public String removeItemFromCart(String itemName) {
        String xpath = getItemByName(itemName);
        return driver.findElement(By.xpath(xpath)).getText();
    }

    /**
     * Проверяет наличие значка корзины с ожиданием.
     *
     * @return true если значок отображается, иначе false
     */
    public boolean isShoppingBadgePresentWithWait() {
        try {
            return getShoppingCartWait().isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Возвращает цвет фона значка корзины.
     *
     * @return цвет фона в формате CSS value
     */
    public String getCartBadgeBackgroundColor() {
        return getShoppingCartWait().getCssValue("background-color");
    }

    /**
     * Генерирует XPath для поиска товара по названию.
     *
     * @param itemName название товара
     * @return XPath выражение для поиска товара
     */
    private static String getItemByName(String itemName) {
        String condition = "text()='" + itemName + "'";
        return INVENTORY_ITEM_BUTTON_PATTERN.formatted(condition);
    }

    /**
     * Ожидает появления значка корзины.
     *
     * @return элемент значка корзины
     */
    private WebElement getShoppingCartWait() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(SHOPPING_CART_BADGE));
    }
}
