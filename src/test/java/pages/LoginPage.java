package pages;

import org.openqa.selenium.*;

/**
 * Страница авторизации (Login Page).
 * Содержит методы для ввода учетных данных, авторизации
 * и проверки сообщений об ошибках.
 */
public class LoginPage extends BasePage {

    private static final String ATTRIBUTE_USERNAME = "username";
    private static final String ATTRIBUTE_PASSWORD = "password";

    private final By loginInput = By.xpath(DATA_TEXT_PATTERN.formatted(ATTRIBUTE_USERNAME));
    private final By passwordInput = By.xpath(DATA_TEXT_PATTERN.formatted(ATTRIBUTE_PASSWORD));
    private final By submitButton = By.xpath(DATA_TEXT_PATTERN.formatted("login-button"));
    private final By error = By.xpath(DATA_TEXT_PATTERN.formatted("error"));
    private final By errorIconUsername = By.xpath(DATA_ICON_PATTERN.formatted(ATTRIBUTE_USERNAME));
    private final By errorIconPassword = By.xpath(DATA_ICON_PATTERN.formatted(ATTRIBUTE_PASSWORD));

    /**
     * Конструктор страницы входа.
     *
     * @param driver драйвер Selenium для взаимодействия с браузером
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Выполняет авторизацию с указанными учетными данными.
     *
     * @param username имя пользователя
     * @param password пароль
     */
    public void login(String username, String password) {
        enterCredentials(username, password);
        driver.findElement(submitButton).click();
    }

    /**
     * Вводит учетные данные в соответствующие поля.
     *
     * @param username имя пользователя
     * @param password пароль
     */
    public void enterCredentials(String username, String password) {
        driver.findElement(loginInput).sendKeys(username);
        driver.findElement(passwordInput).sendKeys(password);
    }

    /**
     * Возвращает текст сообщения об ошибке.
     *
     * @return текст ошибки
     */
    public String getErrorText() {
        return getElement(error).getText();
    }

    /**
     * Проверяет, отображается ли сообщение об ошибке.
     *
     * @return true если ошибка отображается, иначе false
     */
    public boolean isErrorDisplayed() {
        return isErrorElementDisplayed(error);
    }

    /**
     * Проверяет, отображается ли иконка ошибки для поля имени пользователя.
     *
     * @return true если иконка отображается, иначе false
     */
    public boolean isUsernameErrorIconDisplayed() {
        return isErrorElementDisplayed(errorIconUsername);
    }

    /**
     * Проверяет, отображается ли иконка ошибки для поля пароля.
     *
     * @return true если иконка отображается, иначе false
     */
    public boolean isPasswordErrorIconDisplayed() {
        return isErrorElementDisplayed(errorIconPassword);
    }

    /**
     * Находит элемент по локатору.
     *
     * @param locator локатор элемента
     * @return найденный элемент
     */
    public WebElement getElement(By locator) {
        return driver.findElement(locator);
    }

    /**
     * Проверяет, отображается ли элемент по локатору.
     *
     * @param locator локатор элемента
     * @return true если элемент отображается, иначе false
     */
    public boolean isErrorElementDisplayed(By locator) {
        try {
            return getElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
