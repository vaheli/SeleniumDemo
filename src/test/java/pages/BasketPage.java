package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;

import java.util.*;

public class BasketPage extends BasePage {
    private static final By GOODS_TITLE = By.cssSelector(".inventory_item_name");
    private static final By CHECKOUT_BUTTON = By.cssSelector("#checkout");
    private static final By CONTINUE_SHOPPING = By.cssSelector("#continue-shopping");

    public BasketPage(WebDriver driver) {
        super(driver);
    }

    @Step("Получаем название товаров")
    public List<String> getProductsNames() {
        List<WebElement> allProducts = driver.findElements(GOODS_TITLE);
        List<String> productNames = new ArrayList<>();
        for (WebElement product : allProducts) {
            productNames.add(product.getText());
        }
        return productNames;
    }

    @Step("Нажимаем на кнопку 'Continue Shopping'")
    public ProductsPage clickContinueShoppingButton() {
        getContinueShopping().click();
        return new ProductsPage(driver);
    }

    @Step("Проверяем, что кнопка 'Checkout' отображается")
    public boolean isDisplayedCheckoutButton() {
        return getCheckoutButton().isDisplayed();
    }

    @Step("Нажимаем на кнопку 'Checkout'")
    public CheckoutPage clickCheckoutButton() {
        getCheckoutButton().click();
        return new CheckoutPage(driver);
    }

    @Step("Получаем текст кнопки 'Continue Shopping'")
    public String getContinueShoppingButtonText() {
        return getContinueShopping().getText();
    }

    @Step("Получаем локатор кнопки 'Checkout'")
    public WebElement getCheckoutButton() {
        return getWebElement(CHECKOUT_BUTTON);
    }

    @Step("Получаем локатор кнопки 'Continue Shopping'")
    public WebElement getContinueShopping() {
        return getWebElement(CONTINUE_SHOPPING);
    }

    @Step("Получаем цвет фона кнопки 'Checkout'")
    public String getCheckoutBackgroundColor() {
        return getWebElement(CHECKOUT_BUTTON).getCssValue("background-color");
    }
}
