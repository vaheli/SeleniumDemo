package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;

import java.util.List;

public class CheckoutOverviewPage extends BasePage{
    private static final By ITEM_TOTAL = By.xpath("//*[@data-test='subtotal-label']");
    private static final By ITEM_PRICE = By.xpath("//*[@data-test='inventory-item-price']");
    private static final By FINISH = By.id("finish");
    private static final By CANCEL = By.id("cancel");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    @Step("Нажимаем кнопку 'Finish' для завершения заказа")
    public CheckoutCompletePage clickFinishButton() {
        getFinishButton().click();
        return new CheckoutCompletePage(driver);
    }

    @Step("Нажимаем кнопку 'Cancel' для отмены заказа")
    public ProductsPage clickCancelButton() {
        getCancelButton().click();
        return new ProductsPage(driver);
    }

    @Step("Получаем цвет фона кнопки 'Finish'")
    public String getFinishButtonBackgroundColor() {
        return getWebElement(FINISH).getCssValue("background-color");
    }

    @Step("Получаем сумму всех цен товаров в корзине")
    public double getTotalItemsPrice() {
        List<WebElement> allItemsPrice = driver.findElements(ITEM_PRICE);
        double totalPrice = 0;
        for (WebElement itemPrice : allItemsPrice) {
            totalPrice += Double.parseDouble(itemPrice.getText().replaceAll("[^0-9.]", ""));
        }
        return totalPrice;
    }

    @Step("Получаем итоговую сумму со страницы")
    public double getItemTotal() {
        String itemTotalText = getWebElement(ITEM_TOTAL).getText();
        return Double.parseDouble(itemTotalText.replaceAll("[^0-9.]", ""));
    }

    @Step("Получаем локатор кнопки 'Finish'")
    private WebElement getFinishButton() {
        return getWebElement(FINISH);
    }

    @Step("Получаем локатор кнопки 'Cancel'")
    private WebElement getCancelButton() {
        return getWebElement(CANCEL);
    }
}
