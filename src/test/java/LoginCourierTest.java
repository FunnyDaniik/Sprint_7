import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.Courier;
import ru.praktikum.CourierSteps;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class LoginCourierTest {
    private String login; // Логин курьера
    private String password; // Пароль курьера
    private CourierSteps courierLogin; // Объект класса CourierSteps для выполнения запросов
    @Before
    public void setUp() { // Метод, выполняющийся перед каждым тестом
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/"; // Устанавливаем базовый URI для RestAssured
        login = RandomStringUtils.randomAlphanumeric(3,20); // Генерируем случайный логин
        password = RandomStringUtils.randomAlphanumeric(8,20); // Генерируем случайный пароль

        courierLogin = new CourierSteps(); // Создаем экземпляр класса CourierSteps

        courierLogin.sendPostRequestCourierCreate(new Courier(login,password)) // Отправляем POST-запрос для создания курьера
                .then() // Начинаем цепочку проверок
                .statusCode(201) // Проверяем, что статус-код ответа равен 201
                .body("ok", is(true)); // Проверяем, что в теле ответа поле "ok" равно true
    }

    @Test
    @DisplayName("Login Courier Test") // Название теста для отчетов
    @Description("Создание учетной записи и авторизация курьера, ручка /api/v1/courier/login") // Описание теста для отчетов
    public void loginCourierTest() { // Тест для проверки успешной авторизации курьера
        courierLogin.sendPostRequestCourierLogin(new Courier(login,password)) // Отправляем POST-запрос для авторизации курьера
                .then() // Начинаем цепочку проверок
                .statusCode(200) // Проверяем, что статус-код ответа равен 200
                .body("id", notNullValue()); // Проверяем, что в теле ответа поле "id" не равно null
    }

    @Test
    @DisplayName("Login Courier Without Login Test") // Название теста для отчетов
    @Description("Авторизация курьера без логина, ручка /api/v1/courier/login") // Описание теста для отчетов
    public void loginCourierWithoutLoginTest() { // Тест для проверки авторизации курьера без логина
        courierLogin.sendPostRequestCourierLogin(new Courier("",password)) // Отправляем POST-запрос для авторизации курьера без логина
                .then() // Начинаем цепочку проверок
                .statusCode(400) // Проверяем, что статус-код ответа равен 400
                .body("message", equalTo("Недостаточно данных для входа")); // Проверяем, что в теле ответа поле "message" равно "Недостаточно данных для входа"
    }

    @Test
    @DisplayName("Login Courier Without Password Test") // Название теста для отчетов
    @Description("Авторизация курьера без пароля, ручка /api/v1/courier/login") // Описание теста для отчетов
    public void  loginCourierWithoutPasswordTest() { // Тест для проверки авторизации курьера без пароля
        courierLogin.sendPostRequestCourierLogin(new Courier(login,"")) // Отправляем POST-запрос для авторизации курьера без пароля
                .then() // Начинаем цепочку проверок
                .statusCode(400) // Проверяем, что статус-код ответа равен 400
                .body("message", equalTo("Недостаточно данных для входа")); // Проверяем, что в теле ответа поле "message" равно "Недостаточно данных для входа"
    }

    @Test
    @DisplayName("Login Courier With Non Existent Login Test") // Название теста для отчетов
    @Description("Авторизация курьера с несуществующим логином, ручка /api/v1/courier/login") // Описание теста для отчетов
    public void loginCourierWithNonExistentLoginTest() { // Тест для проверки авторизации курьера с несуществующим логином
        courierLogin.sendPostRequestCourierLogin(new Courier("qwerty",password)) // Отправляем POST-запрос для авторизации курьера с несуществующим логином
                .then() // Начинаем цепочку проверок
                .statusCode(404) // Проверяем, что статус-код ответа равен 404
                .body("message", equalTo("Учетная запись не найдена")); // Проверяем, что в теле ответа поле "message" равно "Учетная запись не найдена"
    }

    @Test
    @DisplayName("Login Courier With Non Existent Password Test") // Название теста для отчетов
    @Description("Авторизация курьера с несуществующим паролем, ручка /api/v1/courier/login") // Описание теста для отчетов
    public void loginCourierWithNonExistentPasswordTest() { // Тест для проверки авторизации курьера с несуществующим паролем
        courierLogin.sendPostRequestCourierLogin(new Courier(login,"qwerty")) // Отправляем POST-запрос для авторизации курьера с несуществующим паролем
                .then() // Начинаем цепочку проверок
                .statusCode(404) // Проверяем, что статус-код ответа равен 404
                .body("message", equalTo("Учетная запись не найдена")); // Проверяем, что в теле ответа поле "message" равно "Учетная запись не найдена"
    }

    @Test
    @DisplayName("Login Courier With Wrong Login Test")
    @Description("Авторизация курьера с неправильным логином, ручка /api/v1/courier/login")
    public void loginCourierWithWrongLoginTest() {
        String wrongLogin = login + "_"; // Создаем "неправильный" логин, добавив символ в конец
        courierLogin.sendPostRequestCourierLogin(new Courier(wrongLogin, password))
                .then()
                .statusCode(404) // Ожидаем статус код 404 (Not Found)
                .body("message", equalTo("Учетная запись не найдена")); // Проверяем сообщение об ошибке
    }

    @Test
    @DisplayName("Login Courier With Wrong Password Test")
    @Description("Авторизация курьера с неправильным паролем, ручка /api/v1/courier/login")
    public void loginCourierWithWrongPasswordTest() {
        String wrongPassword = password + "_"; // Создаем "неправильный" пароль, добавив символ в конец
        courierLogin.sendPostRequestCourierLogin(new Courier(login, wrongPassword))
                .then()
                .statusCode(404) // Ожидаем статус код 404 (Not Found)
                .body("message", equalTo("Учетная запись не найдена")); // Проверяем сообщение об ошибке
    }

    @After
    public void tearDown(){ // Метод, выполняющийся после каждого теста
        Integer id = courierLogin.sendPostRequestCourierLogin(new Courier(login,password)) // Отправляем POST-запрос для авторизации курьера
                .then() // Начинаем цепочку проверок
                .extract() // Извлекаем данные из ответа
                .path("id"); // Извлекаем значение поля "id"
        if (id != null) { // Проверяем, что значение id не равно null
            courierLogin.sendRequestDeleteCourier(id) // Отправляем DELETE-запрос для удаления курьера
                    .then() // Начинаем цепочку проверок
                    .statusCode(200); // Проверяем, что статус-код ответа равен 200
        }
    }
}
