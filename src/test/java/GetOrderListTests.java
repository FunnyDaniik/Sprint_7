import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderListTests {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/"; // Устанавливаем базовый URI
    }

    @Test
    public void getOrderListWithNonExistentCourierIdTest() {
        // Отправляем GET-запрос на получение списка заказов с несуществующим courierId

        Response response = given()
                .log().ifValidationFails() // Логируем запрос и ответ, если валидация не прошла
                .header("Content-type", "application/json") // Устанавливаем заголовок Content-Type
                .get("/api/v1/orders"); // Выполняем GET запрос к эндпоинту /api/v1/orders

        // Проверяем, что статус код ответа равен 200
        response.then().assertThat().statusCode(200);

        // Если статус код 200, проверяем тело ответа
        response.then().assertThat().body("orders", notNullValue()); // Проверяем, что в теле ответа поле "orders" не равно null
    }
}
