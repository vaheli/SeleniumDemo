package header;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

import static pages.BasePage.DATA_TEXT_PATTERN;

public class NavigationPanel {
    private static final By PAGE_TITLE = By.xpath(DATA_TEXT_PATTERN.formatted("title"));
    private static final By HEADER_TITLE = By.cssSelector(".app_logo");
    private static final By BURGER_MENU = By.cssSelector("#react-burger-menu-btn");
    private static final By MENU_ITEMS = By.cssSelector(".menu-item");
    private static final By SHOPPING_CART_BADGE = By.xpath(DATA_TEXT_PATTERN.formatted("shopping-cart-badge"));
    private static final By SHOPPING_CART_LINK = By.xpath(DATA_TEXT_PATTERN.formatted("shopping-cart-link"));

    private final WebDriver driver;
    WebDriverWait wait;

    public NavigationPanel(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.driver = driver;
    }

    @Step("Открываем 'Бургер-Меню'")
    public void clickBurgerMenu() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(BURGER_MENU)).click();
    }

    @Step("Кликаем 'Корзина покупок'")
    public <T> T clickShoppingCart(Class<T> pageClass) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(SHOPPING_CART_LINK)).click();
        try {
            return pageClass.getDeclaredConstructor(WebDriver.class).newInstance(driver);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать экземпляр страницы: " + pageClass.getName(), e);
        }
    }

    @Step("Получаем заголовок хедера")
    public String getHeaderTitle() {
        return getTitle(HEADER_TITLE);
    }

    @Step("Получаем локатор название страницы")
    public String getPageTitle() {
        return getTitle(PAGE_TITLE);
    }

    @Step("Получаем количество ссылок 'Бургер-Меню'")
    public int getMenuItemsCount() {
        List<WebElement> menuItems = driver.findElements(MENU_ITEMS);

        return menuItems.size();
    }

    @Step("Получаем количество добавленных товаров из значка 'Корзины товаров'")
    public String getShoppingCount() {
        try {
            return getShoppingCartWait().getText();
        } catch (TimeoutException e) {
            return "0";
        }
    }

    @Step("Проверяем, пуста ли корзина")
    public boolean isCartEmpty() {
        return getShoppingCount().isEmpty() || getShoppingCount().equals("0");
    }

    @Step("Проверяем, отображается значок 'Корзины товаров'")
    public boolean isShoppingBadgePresentWithWait() {
        try {
            return getShoppingCartWait().isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Step("Проверяем цвет фона значка 'Корзины товаров'")
    public String getBadgeColor() {
        return getShoppingCartBadgeWait().getCssValue("background-color");
    }

    @Step("Получаем текст из локатора: {locator}")
    private String getTitle(By locator) {
        return driver.findElement(locator).getText();
    }

    @Step("Получаем кнопку 'Корзины товаров'")
    public WebElement getShoppingCartWait() {
        return waitForElement(SHOPPING_CART_LINK);
    }

    @Step("Получаем значок на 'Корзину товаров'")
    private WebElement getShoppingCartBadgeWait() {
        return waitForElement(SHOPPING_CART_BADGE);
    }

    @Step("Ожидаем появления элемента: {locator}")
    private WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
}
