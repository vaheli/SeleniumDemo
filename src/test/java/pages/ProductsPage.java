package pages;

import org.openqa.selenium.*;

public class ProductsPage {
    private final By title = By.xpath("//*[@class='title']");
    WebDriver driver;

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getTitle() {
        return driver.findElement(title).getText();
    }
}
