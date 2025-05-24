import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.Orders;

import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateOrderTests {

    private final String[] color; // Массив цветов для параметризованного теста
    private final int statusCode;
    private Orders order;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/"; // Устанавливаем базовый URI
        // Создаем объект Orders с данными, которые не меняются
        order = new Orders(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                "4",
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                null // Цвет будет установлен в параметризованном тесте
        );
    }

    // Параметризация теста: передаем разные наборы цветов
    @Parameterized.Parameters
    public static Collection<Object[]> getColorData() {
        return Arrays.asList(new Object[][] {
                {new String[]{"BLACK"}, 201}, // Один цвет: черный
                {new String[]{"GREY"}, 201}, // Один цвет: серый
                {new String[]{"BLACK", "GREY"}, 201}, // Оба цвета
                {null, 201}, // Без указания цвета
                {new String[]{}, 201} // Без указания цвета (пустой массив)
        });
    }

    public CreateOrderTests(String[] color, int statusCode) {
        this.color = color; // Инициализируем массив цветов из параметров
        this.statusCode = statusCode;
    }

    @Test
    public void createOrderTest() {
        order.setColor(color); // Устанавливаем цвет в объекте Orders

        // Отправляем POST-запрос на создание заказа и получаем ответ
        Response response = given()
                .log().all() // Логируем все детали запроса
                .header("Content-Type", "application/json") // Устанавливаем заголовок Content-Type
                .body(order) // Устанавливаем тело запроса (объект Orders будет автоматически преобразован в JSON)
                .post("/api/v1/orders"); // Отправляем запрос на ручку /api/v1/orders

        // Логируем все детали ответа
        response.then().log().all();

        // Проверяем статус код ответа
        assertEquals(statusCode, response.getStatusCode()); // Проверяем, что статус код соответствует ожидаемому (201)
        // Проверяем, что в теле ответа есть поле "track"
        response.then().assertThat().body("track", notNullValue()); // Проверяем, что поле "track" не равно null
    }
}
