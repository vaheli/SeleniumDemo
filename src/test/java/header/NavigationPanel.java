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
    private static final By cartLink = By.cssSelector(".shopping_cart_link");
    private static final By burgerMenu = By.cssSelector("#react-burger-menu-btn");
    private static final By menuItemLocator = By.cssSelector(".menu-item");
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
        driver.findElement(burgerMenu).click();
    }

    @Step("Кликаем 'Корзина покупок'")
    public void clickShoppingCart() {
        wait.until(ExpectedConditions.presenceOfElementLocated(cartLink)).click();
    }

    @Step("Получаем локатор заголовка страницы")
    public String getHeaderTitle() {
        return getTitle(HEADER_TITLE);
    }

    @Step("Получаем локатор название страницы")
    public String getPageTitle() {
        return getTitle(PAGE_TITLE);
    }

    @Step("Получаем количество ссылок 'Бургер-Меню'")
    public int getMenuItemsCount() {
        List<WebElement> menuItems = driver.findElements(menuItemLocator);

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

    @Step("Проверяем, отображается значок 'Корзины товаров'")
    public boolean isShoppingBadgePresentWithWait() {
        try {
            return getShoppingCartWait().isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Step("Проверяем цвет фона значка 'Корзины товаров'")
    public String getCartBadgeBackgroundColor() {
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
