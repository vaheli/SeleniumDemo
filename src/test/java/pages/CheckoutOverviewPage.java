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

    @Step("Получаем локатор кнопки 'Finish'")
    public WebElement getFinishButton() {
        return getWebElement(FINISH);
    }

    @Step("Получаем локатор кнопки 'Cancel'")
    public WebElement getCancelButton() {
        return getWebElement(CANCEL);
    }

    @Step("Получаем цвет фона кнопки 'Finish'")
    public String getFinishButtonBackgroundColor() {
        return getWebElement(FINISH).getCssValue("background-color");
    }

    @Step("Получаем общий итог всех товаров в корзине")
    public double getItemsPrice() {
        List<WebElement> allItemsPrice = driver.findElements(ITEM_PRICE);
        double totalPrice = 0;
        for (WebElement itemPrice : allItemsPrice) {
            totalPrice += Double.parseDouble(itemPrice.getText().replaceAll("[^0-9.]", ""));
        }
        return totalPrice;
    }

    @Step("Получаем общую сумму цен всех товаров в корзине")
    public double getItemTotal() {
        String itemTotalText = getWebElement(ITEM_TOTAL).getText();
        return Double.parseDouble(itemTotalText.replaceAll("[^0-9.]", ""));
    }
}
