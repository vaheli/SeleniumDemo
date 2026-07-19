package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;

public class CheckoutCompletePage extends BasePage{
    private static final By ORDER_CONFIRMATION_TEXT = By.xpath("//*[@data-test='complete-header']");
    private static final By BACK_HOME_BUTTON = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    @Step("Получаем уведомление о успешном завершении заказа")
    public String getThankYouMessage() {
        return getWebElement(ORDER_CONFIRMATION_TEXT).getText();
    }

    @Step("Нажимаем на кнопку 'Back Home'")
    public void clickBackToProductsPage() {
        getWebElement(BACK_HOME_BUTTON).click();
    }
}
