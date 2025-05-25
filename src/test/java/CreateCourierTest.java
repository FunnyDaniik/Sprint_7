import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.Courier;
import ru.praktikum.CourierSteps;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateCourierTest {
    // Поля для хранения данных тестового курьера
    private String login;  // Логин курьера
    private String password; // Пароль курьера
    private String firstName; // Имя курьера
    private CourierSteps courierCreate; // Объект для работы с API курьеров
    private int courierId; // Идентификатор курьера

    // Выполняется перед каждым тестом
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/"; // Устанавливаем базовый URI для RestAssured
        login = RandomStringUtils.randomAlphanumeric(3, 20); // Генерируем случайный логин
        password = RandomStringUtils.randomAlphanumeric(8, 20); // Генерируем случайный пароль
        firstName = RandomStringUtils.randomAlphanumeric(4, 28); // Генерируем случайное имя
        courierCreate = new CourierSteps(); // Создаем экземпляр класса CourierSteps для выполнения запросов
    }

    @After
    public void tearDown() {
        try {
            if (login != null && password != null) {
                // 1. Логинимся, чтобы получить ID курьера
                Response loginResponse = courierCreate.sendPostRequestCourierLogin(
                        new Courier(login, password, firstName));

                if (loginResponse.statusCode() == 200) {
                    courierId = loginResponse.jsonPath().getInt("id");

                    // 2. Удаляем курьера по полученному ID
                    Response deleteResponse = courierCreate.sendRequestDeleteCourier(courierId);

                    if (deleteResponse.statusCode() == 200) {
                        System.out.println("Курьер успешно удален, ID: " + courierId);
                    } else {
                        System.out.println("Не удалось удалить курьера. Код: " +
                                deleteResponse.statusCode());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при удалении курьера: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Create Courier Test")
    @Description("Создание учетной записи курьера, ручка /api/v1/courier")
    public void createCourierTest() {
        // Отправляем POST-запрос на создание курьера
        courierCreate.sendPostRequestCourierCreate(new Courier(login, password, firstName))
                .then()
                .log().ifValidationFails() // Логируем запрос и ответ, если валидация не прошла
                .assertThat()
                .statusCode(201) // Проверяем, что статус код равен 201 (Created)
                .body("ok", equalTo(true)); // Проверяем, что в теле ответа поле "ok" равно true
    }

    // Ожидаемый результат: "Этот логин уже используется"
    // Фактический результат: "Этот логин уже используется. Попробуйте другой."
    @Test
    @DisplayName("Courier Conflict Test")
    @Description("Проверка статус кода и сообщения об ошибке, при создании курьера с не уникальным логином")
    public void createDuplicateCourierTest() {
        // Создаем курьера первый раз (успешно)
        courierCreate.sendPostRequestCourierCreate(new Courier(login, password, firstName))
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));

        // Пытаемся создать курьера с тем же логином (должна быть ошибка)
        courierCreate.sendPostRequestCourierCreate(new Courier(login, password, firstName))
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(409) // Проверяем, что статус код равен 409 (Conflict)
                .body("message", equalTo("Этот логин уже используется")); // Проверяем, что сообщение об ошибке соответствует ожидаемому
    }

    @Test
    @DisplayName("Create Courier Without Login")
    @Description("Создание учетной записи курьера без логина, ручка /api/v1/courier")
    public void createCourierWithoutLogin() {
        // Отправляем POST-запрос на создание курьера без логина
        courierCreate.sendPostRequestCourierCreate(new Courier(null, password, firstName))
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(400) // Проверяем, что статус код равен 400 (Bad Request)
                .body("message", equalTo("Недостаточно данных для создания учетной записи")); // Проверяем сообщение об ошибке
    }

    @Test
    @DisplayName("Create Courier Without Password")
    @Description("Создание учетной записи курьера без пароля, ручка /api/v1/courier")
    public void createCourierWithoutPassword() {
        // Отправляем POST-запрос на создание курьера без пароля
        courierCreate.sendPostRequestCourierCreate(new Courier(login, null, firstName))
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(400) // Проверяем, что статус код равен 400 (Bad Request)
                .body("message", equalTo("Недостаточно данных для создания учетной записи")); // Проверяем сообщение об ошибке
    }

    @Test
    @DisplayName("Create Courier Without First Name")
    @Description("Создание учетной записи курьера без имени, ручка /api/v1/courier")
    public void createCourierWithoutFirstName() {
        courierCreate.sendPostRequestCourierCreate(new Courier(login, password, null))
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(201) // Проверка, что статус код 201 (Created)
                .body("ok", equalTo(true)); // Проверка, что "ok" равно true
    }

    @Test
    @DisplayName("Create Courier Without Login And Password")
    @Description("Создание учетной записи курьера без логина и пароля, ручка /api/v1/courier")
    public void createCourierWithoutLoginAndPassword() {
        courierCreate.sendPostRequestCourierCreate(new Courier(null, null, firstName))
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(400) // Проверка, что статус код 400 (Bad Request)
                .body("message", equalTo("Недостаточно данных для создания учетной записи")); // Проверка сообщения об ошибке
    }
}
