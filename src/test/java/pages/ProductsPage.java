package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

public class ProductsPage extends BasePage {
    private static final String ATTRIBUTE_TITLE = "title";
    private static final String ATTRIBUTE_BADGE = "shopping-cart-badge";
    private static final String ATTRIBUTE_BACKGROUND_COLOR = "background-color";
    private static final String BUTTON_ADD_TO_CART = "Add to cart";
    private static final String BUTTON_REMOVE = "Remove";

    private static final By TITLE = By.xpath(DATA_TEXT_PATTERN.formatted(ATTRIBUTE_TITLE));
    private static final By ADD_TO_CART_TEXT = By.xpath(TEXT_PATTERN.formatted(BUTTON_ADD_TO_CART));
    private static final By SHOPPING_CART_BADGE = By.xpath(DATA_TEXT_PATTERN.formatted(ATTRIBUTE_BADGE));

    public ProductsPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public String getTitle() {
        return driver.findElement(TITLE).getText();
    }

    public String getShoppingCount() {
        try {
            return getShoppingCartWait().getText();
        } catch (TimeoutException  e) {
            return "0";
        }
    }

    public void addItemToCart(String item) {
        By addToCart = getItemButtonLocator(item, BUTTON_ADD_TO_CART);
        driver.findElement(addToCart).click();
    }

    public void addItemToCart(Integer count) {
        List<WebElement> addToCartButtons = driver.findElements(ADD_TO_CART_TEXT);

        for (int i = 0; i < count && i < addToCartButtons.size(); i ++) {
            addToCartButtons.get(i).click();
        }
    }

    public String removeItemFromCart(String item) {
        By removeCart = getItemButtonLocator(item, BUTTON_REMOVE);
        return driver.findElement(removeCart).getText();
    }

    public boolean isShoppingBadgePresentWithWait() {
        try {
            return getShoppingCartWait().isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getCartBadgeBackgroundColor() {
        return getShoppingCartBadge().getCssValue(ATTRIBUTE_BACKGROUND_COLOR);
    }

    private WebElement getShoppingCartBadge() {
        return driver.findElement(SHOPPING_CART_BADGE);
    }

    private WebElement getShoppingCartWait() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(SHOPPING_CART_BADGE));
    }


    private By getItemButtonLocator(String item, String buttonText) {
        return By.xpath(INVENTORY_ITEM_BUTTON_XPATH.formatted(item, buttonText));
    }
}
