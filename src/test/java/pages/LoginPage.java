package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import user.User;

public class LoginPage extends BasePage {
    private static final String ATTRIBUTE_USERNAME = "username";
    private static final String ATTRIBUTE_PASSWORD = "password";

    private final By loginInput = By.xpath(DATA_TEXT_PATTERN.formatted(ATTRIBUTE_USERNAME));
    private final By passwordInput = By.xpath(DATA_TEXT_PATTERN.formatted(ATTRIBUTE_PASSWORD));
    private final By submitButton = By.xpath(DATA_TEXT_PATTERN.formatted("login-button"));
    private final By error = By.xpath(DATA_TEXT_PATTERN.formatted("error"));
    private final By errorIconUsername = By.xpath(DATA_ICON_PATTERN.formatted(ATTRIBUTE_USERNAME));
    private final By errorIconPassword = By.xpath(DATA_ICON_PATTERN.formatted(ATTRIBUTE_PASSWORD));

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Логинимся под кредами пользователя = {user}")
    public void login(User user) {
        enterCredentials(user);
    }

    @Step("Нажимаем кнопку 'Login' и переходим на страницу товаров")
    public ProductsPage clickLoginButton() {
        driver.findElement(submitButton).click();
        return new ProductsPage(driver);
    }

    @Step("Получаем текст ошибки")
    public String getErrorText() {
        return getWebElement(error).getText();
    }

    @Step("Проверяем отображение сообщения об ошибке")
    public boolean isErrorDisplayed() {
        return isErrorElementDisplayed(error);
    }

    @Step("Проверяем отображение иконки ошибки для поля логина")
    public boolean isUsernameErrorIconDisplayed() {
        return isErrorElementDisplayed(errorIconUsername);
    }

    @Step("Проверяем отображение иконки ошибки для поля пароля")
    public boolean isPasswordErrorIconDisplayed() {
        return isErrorElementDisplayed(errorIconPassword);
    }

    @Step("Получаем элемент об ошибки по локатору")
    public boolean isErrorElementDisplayed(By locator) {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator)).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Step("Заполняем поля ввода логина: '{user.login}' и пароля: '{user.password}'")
    private void enterCredentials(User user) {
        driver.findElement(loginInput).sendKeys(user.getLogin());
        driver.findElement(passwordInput).sendKeys(user.getPassword());
    }
}
