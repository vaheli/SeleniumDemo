package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;

public class CheckoutPage extends BasePage {
    private static final By FIRST_NAME = By.id("first-name");
    private static final By LAST_NAME = By.id("last-name");
    private static final By POSTAL_CODE = By.id("postal-code");
    private final static By CONTINUE = By.id("continue");
    private static final By CSS_SELECTOR_CONTINUE = By.cssSelector(".btn_secondary");
    private final static By CANSEL = By.id("cancel");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    @Step("Заполняем поля ввода: имя, фамилия и почтовый индекс")
    public void fillCheckoutForm(String firstName, String lastName, String postalCode) {
        getFieldFirstName().sendKeys(firstName);
        getFieldLastName().sendKeys(lastName);
        getFieldPostalCode().sendKeys(postalCode);
    }

    @Step("Получаем placeholder поля 'Имя'")
    public String getFirstNamePlaceholderValue() {
        return getFieldFirstName().getAttribute("placeholder");
    }

    @Step("Получаем placeholder поля 'Фамилия' ")
    public String getLastNamePlaceholderValue() {
        return getFieldLastName().getAttribute("placeholder");
    }

    @Step("Проверяем поле ввода почтового индекса")
    public boolean isPostalCodeValid(String value) {
        getFieldPostalCode().sendKeys(value);
        String postalCode = getFieldPostalCode().getAttribute("value");
        assert postalCode != null;
        return !postalCode.matches("^[A-Za-zА-Яа-яЁё]+$");
    }

    @Step("Нажимаем кнопку 'Continue' для перехода к предварительному просмотру заказа")
    public CheckoutOverviewPage clickContinueButton() {
        getContinueButton().click();
        return new CheckoutOverviewPage(driver);
    }

    @Step("Нажимаем кнопку 'Cancel' для возврата в корзину")
    public BasketPage clickCancelButton() {
        getCancelButton().click();
        return new BasketPage(driver);
    }

    @Step("Получаем локатор кнопки 'Continue'")
    public WebElement getContinueButton() {
        return getWebElement(CONTINUE);
    }

    @Step("Получаем цвет фона кнопки 'Continue'")
    public String getContinueButtonColor() {
        return getWebElement(CSS_SELECTOR_CONTINUE).getCssValue("background-color");
    }

    @Step("Получаем локатор кнопки 'Cancel'")
    private WebElement getCancelButton() {
        return getWebElement(CANSEL);
    }

    @Step("Получаем локатор поля ввода 'Имя'")
    private WebElement getFieldFirstName() {
        return getWebElement(FIRST_NAME);
    }

    @Step("Получаем локатор поля ввода 'Фамилия'")
    private WebElement getFieldLastName() {
        return getWebElement(LAST_NAME);
    }

    @Step("Получаем локатор поля ввода 'Почтового индекса'")
    private WebElement getFieldPostalCode() {
        return getWebElement(POSTAL_CODE);
    }
}
