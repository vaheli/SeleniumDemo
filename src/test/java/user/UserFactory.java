package user;

import io.qameta.allure.Step;
import utils.PropertyReader;

public class UserFactory {
    public static User whitAdminPermission() {
        return new User(PropertyReader.getProperty("saucedemo.user"),
                PropertyReader.getProperty("saucedemo.password"));
    }

    public static User whitInvalidLoginPermission() {
        return new User(PropertyReader.getProperty("saucedemo.user_invalid"),
                PropertyReader.getProperty("saucedemo.password"));
    }

    public static User whitInvalidPasswordPermission() {
        return new User(PropertyReader.getProperty("saucedemo.user"),
                PropertyReader.getProperty("saucedemo.password_invalid"));
    }

    public static User whitLockedPermission() {
        return new User(PropertyReader.getProperty("saucedemo.user_locked"),
                PropertyReader.getProperty("saucedemo.password"));
    }

    public static User whitEmptyLoginPermission() {
        return new User("",
                PropertyReader.getProperty("saucedemo.password"));
    }

    public static User whitEmptyPasswordPermission() {
        return new User(PropertyReader.getProperty("saucedemo.user"),
                "");
    }

    @Step("Логинимся под кредами пользователя: {saucedemo.user_with_caps}, {saucedemo.password}")
    public static User whitCapsLoginPermission() {
        return new User(PropertyReader.getProperty("saucedemo.user_with_caps"),
                PropertyReader.getProperty("saucedemo.password"));
    }
}
