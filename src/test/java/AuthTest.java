import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class AuthTest {


    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        $("input[name='login']").setValue(registeredUser.getLogin());
        $("input[name='password']").setValue(registeredUser.getPassword());
        $(".button__text").click();
        $x("//h2[contains(text(), 'Личный кабинет')]").shouldHave(Condition.exactText("Личный кабинет"),
                Duration.ofSeconds(15));

    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = DataGenerator.Registration.getUser("active");
        $("input[name='login']").setValue(notRegisteredUser.getLogin());
        $("input[name='password']").setValue(notRegisteredUser.getPassword());
        $(".button__text").click();
        $("[class='notification__content']").shouldHave(Condition.exactText(("Ошибка! Неверно указан логин или пароль")),
                Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = DataGenerator.Registration.getRegisteredUser("blocked");
        $("input[name='login']").setValue(blockedUser.getLogin());
        $("input[name='password']").setValue(blockedUser.getPassword());
        $(".button__text").click();
        $("[class='notification__content']").shouldHave(Condition.exactText(("Ошибка! Пользователь заблокирован")),
                Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongLogin = DataGenerator.getRandomLogin();
        $("input[name='login']").setValue(wrongLogin);
        $("input[name='password']").setValue(registeredUser.getPassword());
        $(".button__text").click();
        $("[class='notification__content']").shouldHave(Condition.exactText(("Ошибка! Неверно указан логин или пароль")),
                Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongPassword = DataGenerator.getRandomPassword();
        $("input[name='login']").setValue(registeredUser.getLogin());
        $("input[name='password']").setValue(wrongPassword);
        $(".button__text").click();
        $("[class='notification__content']").shouldHave(Condition.exactText(("Ошибка! Неверно указан логин или пароль")),
                Duration.ofSeconds(15));
    }

}